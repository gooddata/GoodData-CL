package com.gooddata.processor;

import java.io.*;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gooddata.connector.JdbcConnector;
import com.gooddata.processor.parser.DIScriptParser;
import com.gooddata.processor.parser.ParseException;

import com.gooddata.connector.TimeDimensionConnector;
import com.gooddata.exception.*;
import com.gooddata.modeling.generator.MaqlGenerator;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.naming.N;
import com.gooddata.util.StringUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.gooddata.connector.Connector;
import org.gooddata.connector.backend.AbstractConnectorBackend;

import com.gooddata.connector.CsvConnector;
import com.gooddata.connector.GaConnector;
import com.gooddata.google.analytics.GaQuery;
import com.gooddata.integration.ftp.GdcFTPApiWrapper;
import com.gooddata.integration.model.Column;
import com.gooddata.integration.model.DLI;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.integration.rest.GdcRESTApiWrapper;
import com.gooddata.integration.rest.configuration.NamePasswordConfiguration;
import com.gooddata.exception.GdcLoginException;
import com.gooddata.util.CsvUtil;
import com.gooddata.util.FileUtil;

/**
 * The GoodData Data Integration CLI processor.
 *
 * @author jiri.zaloudek
 * @author Zdenek Svoboda <zd@gooddata.org>
 * @version 1.0
 */
public class GdcDI {

    private static Logger l = Logger.getLogger(GdcDI.class);

	private String ftpHost;
	private String host;
	private String userName;
	private String password;
    private String httpProtocol = "http";    

    private String dbUserName;
    private String dbPassword;

    private GdcRESTApiWrapper _restApi = null;
    private GdcFTPApiWrapper _ftpApi = null;

    private String projectId = null;
    private Connector connector = null;

    private int backend = AbstractConnectorBackend.CONNECTOR_BACKEND_DERBY_SQL;

    private static long  LOCK_EXPIRATION_TIME = 1000 * 3600; // 1 hour
    
    private GdcDI(final String host, final String userName, final String password) {
    	String ftpHost = null;
        // Create the FTP host automatically
        String[] hcs = host.split("\\.");
        if(hcs != null && hcs.length > 0) {
            for(String hc : hcs) {
                if(ftpHost != null && ftpHost.length()>0)
                    ftpHost += "." + hc;
                else
                    ftpHost = hc + N.FTP_SRV_SUFFIX;
            }
        }
        else {
            throw new IllegalArgumentException("Invalid format of the GoodData REST API host: " + host);
        }

        this.host = host;
        this.ftpHost = ftpHost;
        this.userName = userName;
        this.password = password;
    }

    private void setDbUserName(String usr) {
    	this.dbUserName = usr;
    }

    private void setDbPassword(String psw) {
    	this.dbPassword = psw;
    }

    public void execute(final String commandsStr) throws Exception {

        List<Command> cmds = new ArrayList<Command>();

        cmds.addAll(parseCmd(commandsStr));

        for(Command command : cmds) {
        	processCommand(command);
        }
    }

    private GdcRESTApiWrapper getRestApi() throws GdcLoginException {
    	if (_restApi == null) {
    		if (userName == null) {
    			throw new IllegalArgumentException("Please specify the GoodData username (-u or --username) command-line option.");
    		}
    		if (password == null) {
    			throw new IllegalArgumentException("Please specify the GoodData password (-p or --password) command-line option.");
    		}
            final NamePasswordConfiguration httpConfiguration = new NamePasswordConfiguration(
            		getHttpProtocol(), host,
                    userName, password);
            _restApi = new GdcRESTApiWrapper(httpConfiguration);
            _restApi.login();
    	}
    	return _restApi;
    }

    private GdcFTPApiWrapper getFtpApi() {
    	if (_ftpApi == null) {
	        l.debug("Using the GoodData FTP host '" + ftpHost + "'.");

	        NamePasswordConfiguration ftpConfiguration = new NamePasswordConfiguration("ftp",
	                ftpHost, userName, password);

	        _ftpApi = new GdcFTPApiWrapper(ftpConfiguration);
    	}
    	return _ftpApi;
    }

    /**
     * The main CLI processor
     * @param args command line argument
     * @throws Exception any issue
     */
    public static void main(String[] args) {

        try {
            PropertyConfigurator.configure(System.getProperty("log4j.configuration"));

            String host = "dli.getgooddata.com";

            Options o = new Options();

            o.addOption("u", "username", true, "GoodData username");
            o.addOption("p", "password", true, "GoodData password");
            o.addOption("b", "backend", true, "Database backend DERBY or MYSQL");
            o.addOption("d", "dbusername", true, "Database backend username (not required for the local Derby SQL)");
            o.addOption("c", "dbpassword", true, "Database backend password (not required for the local Derby SQL)");
            o.addOption("h", "host", true, "GoodData host");
            o.addOption("t", "proto", true, "HTTP or HTTPS");
            o.addOption("i", "project", true, "GoodData project identifier (a string like nszfbgkr75otujmc4smtl6rf5pnmz9yl)");
            o.addOption("e", "execute", true, "Commands and params to execute before the commands in provided files");

            CommandLineParser parser = new GnuParser();
            CommandLine line = parser.parse(o, args);

            if(line.hasOption("host")) {
                host = line.getOptionValue("host");
            }
            else {
                l.debug("Using the default GoodData REST API host '" + host + "'.");
            }

            String userName = line.getOptionValue("username");
            String password = line.getOptionValue("password");

            GdcDI gdcDi = new GdcDI(host, userName, password);
            if (line.hasOption("project")) {
                gdcDi.setProjectId(line.getOptionValue("project"));
            }
            if (line.hasOption("execute")) {
                gdcDi.execute(line.getOptionValue("execute"));
            }
            if (line.hasOption("dbusername")) {
                gdcDi.setDbUserName(line.getOptionValue("dbusername"));
            }
            if (line.hasOption("dbpassword")) {
                gdcDi.setDbPassword(line.getOptionValue("dbpassword"));
            }
            if (line.hasOption("proto")) {
                if("HTTP".equalsIgnoreCase(line.getOptionValue("proto")))
                    gdcDi.setHttpProtocol("http");
                else if("HTTPS".equalsIgnoreCase(line.getOptionValue("proto")))
                    gdcDi.setHttpProtocol("https");
                else
                    printErrorHelpandExit("Invalid protocol parameter. Use HTTP or HTTPS.");
            }
            if (line.hasOption("backend")) {
                if("MYSQL".equalsIgnoreCase(line.getOptionValue("backend")))
                    gdcDi.setBackend(AbstractConnectorBackend.CONNECTOR_BACKEND_MYSQL);
                else if("DERBY".equalsIgnoreCase(line.getOptionValue("backend")))
                    gdcDi.setBackend(AbstractConnectorBackend.CONNECTOR_BACKEND_DERBY_SQL);
                else
                    printErrorHelpandExit("Invalid backend parameter. Use MYSQL or DERBY.");
            }
            if (line.getArgs().length == 0 && !line.hasOption("execute")) {
                printErrorHelpandExit("No command has been given, quitting.");
            }
            for (final String arg : line.getArgs()) {
                gdcDi.execute(FileUtil.readStringFromFile(arg));
            }
        } catch (final Exception e) {
            e.printStackTrace();
            printErrorHelpandExit(e.getMessage());
        }
    }

    /**
     * Parses the commands
     * @param cmd commands string
     * @return array of commands
     * @throws InvalidArgumentException in case there is an invalid command
     */
    protected static List<Command> parseCmd(String cmd) throws InvalidArgumentException {
        try {
            if(cmd != null && cmd.length()>0) {
                Reader r = new StringReader(cmd);
                DIScriptParser parser = new DIScriptParser(r);
                List<Command> commands = parser.parse();
                l.debug("Running "+commands.size()+" commands.");
                for(Command c : commands) {
                    l.debug("Command="+c.getCommand()+" params="+c.getParameters());
                }
                return commands;
            }
        }
        catch(ParseException e) {
            throw new InvalidArgumentException("Can't parse command '" + cmd + "'");
        }
        throw new InvalidArgumentException("Can't parse command (empty command).");    

    }

    /**
     * Returns the help for commands
     * @return help text
     */
    protected static String commandsHelp() {
        try {
        	final InputStream is = GdcDI.class.getResourceAsStream("/com/gooddata/processor/COMMANDS.txt");
        	if (is == null)
        		throw new IOException();
            return FileUtil.readStringFromStream(is);
        } catch (IOException e) {
            l.error("Could not read com/gooddata/processor/COMMANDS.txt");
        }
        return "";
    }

    /**
     * Prints an err message, help and exits with status code 1
     * @param err the err message
     */
    protected static void printErrorHelpandExit(String err) {
        l.error("ERROR: " + err);
        l.info("\n\n"+commandsHelp());
        System.exit(1);
    }


    protected boolean match(Command c, String cms) {
        if(c.getCommand().equalsIgnoreCase(cms))
            return true;
        else
            return false;
    }
    
    protected String getParam(Command c, String p) {
        return (String)c.getParameters().get(p);
    }

    protected boolean checkParam(Command c, String p) {
        String v = (String)c.getParameters().get(p);
        if(v == null || v.length() == 0) {
            return false;
        }
        return true;
    }

    protected void error(Command c, String msg) throws InvalidArgumentException {
        printErrorHelpandExit(c.getCommand()+": "+msg);
    }

    protected String getParamMandatory(Command c, String p) throws InvalidArgumentException {
        String v = (String)c.getParameters().get(p);
        if(v == null || v.length() == 0) {
            error(c, "Command parameter '"+p+"' is required.");
        }
        return v;
    }

    protected String getProjectId(Command c) throws InvalidArgumentException {
        if(projectId == null || projectId.length() == 0) {
            error(c, "Please create or open project by using CreateProject or OpenProject commands.");
        }
        return projectId;
    }

    protected void setProjectId(String pid) {
        projectId = pid;
    }

    protected Connector getConnector(Command c) throws InvalidArgumentException {
        if(connector == null) {
            error(c, "No connector. Please use a LoadXXX command to create connector first.");
        }
        return connector;
    }

    protected void setConnector(Connector cc) {
        connector = cc;
    }

    protected File getFile(Command c, String fileName) throws InvalidArgumentException {
        File f = new File(fileName);
        if(!f.exists())
            error(c, "File '" + fileName + "' doesn't exist.");
        return f;
    }

    /**
     * Executes the command
     * @param c to execute
     * @throws Exception general error
     */
    private void processCommand(Command c) throws Exception {
        if(match(c,"CreateProject")) {
            createProject(c);
        }
        else if(match(c,"OpenProject")) {
            setProjectId(getParamMandatory(c,"id"));
        }
        else if(match(c,"GenerateCsvConfig")) {
            generateCsvConfig(c);
        }
        else if(match(c,"LoadCsv")) {
            loadCsv(c);
        }
        else if(match(c,"GenerateGaConfig")) {
            generateGAConfig(c);
        }
        else if(match(c,"LoadGa")) {
            loadGA(c);
        }
        else if(match(c,"LoadTimeDimension")) {
            loadTD(c);
        }
        else if(match(c,"GenerateMaql")) {
            generateMAQL(c);
        }
        else if(match(c,"ExecuteMaql")) {
            executeMAQL(c);
        }
        else if(match(c,"ListSnapshots")) {
            listSnapshots(c);
        }
        else if(match(c,"DropSnapshots")) {
            dropSnapshots(c);
        }
        else if(match(c,"UploadDir")) {
            uploadDir(c);
        }
        else if(match(c,"TransferData")) {
            transferData(c);
        }
        else if(match(c,"TransferSnapshots")) {
            transferSnapshots(c);
        }
        else if(match(c,"TransferLastSnapshot")) {
            transferLastSnapshot(c);
        }
        else if(match(c,"StoreProject")) {
            storeProject(c);
        }
        else if(match(c,"RetrieveProject")) {
            retrieveProject(c);
        }
        else if(match(c, "Lock")) {
        	lock(c);
        }
        else if (match(c, "ProcessNewColumns")) {
        	processNewColumns(c);
        }
        else if(match(c,"GenerateJdbcConfig")) {
            generateJdbcConfig(c);
        }
        else if(match(c,"LoadJdbc")) {
            loadJdbc(c);
        }
    }

    private void transferLastSnapshot(Command c) throws InvalidArgumentException, ModelException, IOException, InternalErrorException, GdcRestApiException, InterruptedException {
        Connector cc = getConnector(c);
        String pid = getProjectId(c);
        // connector's schema name
        String ssn = StringUtil.formatShortName(cc.getSchema().getName());

        cc.initialize();
        // retrieve the DLI
        DLI dli = getRestApi().getDLIById("dataset." + ssn, pid);
        List<DLIPart> parts= getRestApi().getDLIParts("dataset." + ssn, pid);

        String incremental = getParam(c,"incremental");
        if(incremental != null && incremental.length() > 0 &&
                incremental.equalsIgnoreCase("true")) {
            setIncremental(parts);
        }
        boolean waitForFinish = true;
        if(checkParam(c,"waitForFinish")) {
            String w = getParam(c, "waitForFinish");
            if(w != null && w.equalsIgnoreCase("false"))
                waitForFinish = false;
        }
        extractAndTransfer(c, pid, cc, dli, parts, new int[] {cc.getLastSnapshotId()+1}, waitForFinish);
    }

    private void transferSnapshots(Command c) throws InvalidArgumentException, ModelException, IOException, GdcRestApiException, InterruptedException {
        Connector cc = getConnector(c);
        String pid = getProjectId(c);
        String firstSnapshot = getParamMandatory(c,"firstSnapshot");
        String lastSnapshot = getParamMandatory(c,"lastSnapshot");
        int fs = 0,ls = 0;
        try  {
            fs = Integer.parseInt(firstSnapshot);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("TransferSnapshots: The 'firstSnapshot' (" + firstSnapshot +
                    ") parameter is not a number.");
        }
        try {
            ls = Integer.parseInt(lastSnapshot);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("TransferSnapshots: The 'lastSnapshot' (" + lastSnapshot +
                    ") parameter is not a number.");
        }
        int cnt = ls - fs;
        if(cnt >= 0) {
            int[] snapshots = new int[cnt];
            for(int i = 0; i < cnt; i++) {
                snapshots[i] = fs + i;
            }
            // connector's schema name
            String ssn = StringUtil.formatShortName(cc.getSchema().getName());

            cc.initialize();
            // retrieve the DLI
            DLI dli = getRestApi().getDLIById("dataset." + ssn, pid);
            List<DLIPart> parts= getRestApi().getDLIParts("dataset." +ssn, pid);

            String incremental = getParam(c,"incremental");
            if(incremental != null && incremental.length() > 0 &&
                    incremental.equalsIgnoreCase("true"))
                setIncremental(parts);
            boolean waitForFinish = true;
            if(checkParam(c,"waitForFinish")) {
                String w = getParam(c, "waitForFinish");
                if(w != null && w.equalsIgnoreCase("false"))
                    waitForFinish = false;
            }
            extractAndTransfer(c, pid, cc, dli, parts, snapshots, waitForFinish);
        }
        else
            error(c,"The firstSnapshot can't be higher than the lastSnapshot.");
    }

    private void transferData(Command c) throws InvalidArgumentException, ModelException, IOException, GdcRestApiException, InterruptedException {
        Connector cc = getConnector(c);
        String pid = getProjectId(c);
        // connector's schema name
        String ssn = StringUtil.formatShortName(cc.getSchema().getName());
        cc.initialize();
        // retrieve the DLI
        DLI dli = getRestApi().getDLIById("dataset." + ssn, pid);
        List<DLIPart> parts= getRestApi().getDLIParts("dataset." + ssn, pid);
        // target directories and ZIP names

        String incremental = getParam(c,"incremental");
        if(incremental != null && incremental.length() > 0 && incremental.equalsIgnoreCase("true")) {
            setIncremental(parts);
        }
        boolean waitForFinish = true;
        if(checkParam(c,"waitForFinish")) {
            String w = getParam(c, "waitForFinish");
            if(w != null && w.equalsIgnoreCase("false"))
                waitForFinish = false;
        }
        extractAndTransfer(c, pid, cc, dli, parts, null, waitForFinish);
    }

    private void extractAndTransfer(Command c, String pid, Connector cc, DLI dli, List<DLIPart> parts,
        int[] snapshots, boolean waitForFinish) throws IOException, ModelException, GdcRestApiException, InvalidArgumentException, InterruptedException {
        File tmpDir = FileUtil.createTempDir();
        makeWritable(tmpDir);
        File tmpZipDir = FileUtil.createTempDir();
        String archiveName = tmpDir.getName();
        String archivePath = tmpZipDir.getAbsolutePath() + System.getProperty("file.separator") +
            archiveName + ".zip";
        // loads the CSV data to the embedded Derby SQL
        cc.extract();
        // normalize the data in the Derby
        cc.transform();
        // load data from the Derby to the local GoodData data integration package
        cc.deploySnapshot(dli, parts, tmpDir.getAbsolutePath(), archivePath, snapshots);
        // transfer the data package to the GoodData server
        getFtpApi().transferDir(archivePath);
        // kick the GooDData server to load the data package to the project
        String taskUri = getRestApi().startLoading(pid, archiveName);
        if(waitForFinish) {
            checkLoadingStatus(taskUri, tmpDir.getName());
        }
        //cleanup
        FileUtil.recursiveDelete(tmpDir);
        FileUtil.recursiveDelete(tmpZipDir);
    }

    private void checkLoadingStatus(String taskUri, String tmpDir) throws HttpMethodException, GdcLoginException, InterruptedException, GdcUploadErrorException, IOException {
        String status = "";
        while(!status.equalsIgnoreCase("OK") && !status.equalsIgnoreCase("ERROR") && !status.equalsIgnoreCase("WARNING")) {
            status = getRestApi().getLoadingStatus(taskUri);
            l.debug("Loading status = "+status);
            Thread.sleep(500);
        }
        if(!status.equalsIgnoreCase("OK")) {
            l.info("Data loading failed. Status: "+status);
            Map<String,String> result = getFtpApi().getTransferLogs(tmpDir);
            for(String file : result.keySet()) {
                l.info(file+":\n"+result.get(file));
            }
        }
        else
            l.info("Data successfully loaded.");
    }

    private void makeWritable(File tmpDir) {
        try {
            Runtime.getRuntime().exec("chmod -R 777 "+tmpDir.getAbsolutePath());
        }
        catch (IOException e) {
            l.debug("CHMOD execution failed. No big deal perhaps you are running Windows.", e);
        }
    }

    private void uploadDir(Command c) throws InvalidArgumentException, IOException, GdcRestApiException, InterruptedException {
        String pid = getProjectId(c);
        String path = getParamMandatory(c,"path");
        String dataset = getParamMandatory(c,"dataset");
        String reorderStr = getParam(c, "reorder");
        boolean reorder = (reorderStr != null) 
        	&& !"".equals(reorderStr) 
        	&& !"false".equalsIgnoreCase(reorderStr);
        // validate input dir
        File dir = getFile(c,path);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("UploadDir: " + path + " is not a directory.");
        }
        if (!(dir.canRead() && dir.canExecute() && dir.canWrite())) {
            throw new IllegalArgumentException("UploadDir: directory " + path + " is not r/w accessible.");
        }
        // generate manifest
        DLI dli = getRestApi().getDLIById(dataset, pid);
        List<DLIPart> parts = getRestApi().getDLIParts(dataset, pid);

        // prepare the zip file
        File tmpDir = FileUtil.createTempDir();
        for (final DLIPart part : parts) {
        	preparePartFile(part, dir, tmpDir, reorder);
        }
        File tmpZipDir = FileUtil.createTempDir();
        FileUtil.writeStringToFile(
        		dli.getDLIManifest(parts),
        		tmpDir + System.getProperty("file.separator")
        			 + GdcRESTApiWrapper.DLI_MANIFEST_FILENAME);
        String archiveName = tmpDir.getName();
        String archivePath = tmpZipDir.getAbsolutePath() +
                System.getProperty("file.separator") + archiveName + ".zip";
        FileUtil.compressDir(tmpDir.getAbsolutePath(), archivePath);
        
        // ftp upload
        getFtpApi().transferDir(archivePath);
        
        // kick the GoodData server to load the data package to the project
        String taskUri = getRestApi().startLoading(pid, archiveName);
        checkLoadingStatus(taskUri, tmpDir.getName());
    }

    private void dropSnapshots(Command c) throws InvalidArgumentException {
        Connector cc = getConnector(c);
        cc.dropSnapshots();
    }

    private void listSnapshots(Command c) throws InvalidArgumentException, InternalErrorException {
        Connector cc = getConnector(c);
        l.info((cc.listSnapshots()));
    }

    private void executeMAQL(Command c) throws InvalidArgumentException, IOException, GdcRestApiException {
        String pid = getProjectId(c);
        String maqlFile = getParamMandatory(c,"maqlFile");
        File mf = getFile(c,maqlFile);
        String maql = FileUtil.readStringFromFile(maqlFile);
        getRestApi().executeMAQL(pid, maql);
    }

    private void generateMAQL(Command c) throws InvalidArgumentException, IOException {
        Connector cc = getConnector(c);
        String maqlFile = getParamMandatory(c,"maqlFile");
        String maql = cc.generateMaql();
        FileUtil.writeStringToFile(maql, maqlFile);
    }

    private void loadGA(Command c)
            throws InvalidArgumentException, InitializationException, MetadataFormatException, IOException,
            ModelException {

        GaQuery gq = null;
        try {
            gq = new GaQuery();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        String pid = getProjectId(c);
        String configFile = getParamMandatory(c,"configFile");
        String usr = getParamMandatory(c,"username");
        String psw = getParamMandatory(c,"password");
        String id = getParamMandatory(c,"profileId");
        gq.setDimensions(getParamMandatory(c,"dimensions").replace("|",","));
        gq.setMetrics(getParamMandatory(c,"metrics").replace("|",","));
        gq.setStartDate(getParamMandatory(c,"startDate"));
        gq.setEndDate(getParamMandatory(c,"endDate"));
        if(checkParam(c,"filters"))
            gq.setFilters(getParam(c,"filters"));
        setConnector(GaConnector.createConnector(pid, configFile, usr, psw, id, gq,
                getBackend(), dbUserName, dbPassword));
    }

    private void loadTD(Command c)
            throws InvalidArgumentException, InitializationException, MetadataFormatException, IOException,
            ModelException {

        String ctx = "";
        if(checkParam(c,"context"))
            ctx = getParam(c, "context");
        setConnector(TimeDimensionConnector.createConnector(ctx));
    }

    private void generateGAConfig(Command c) throws InvalidArgumentException, IOException {
        String configFile = getParamMandatory(c,"configFile");
        String name = getParamMandatory(c,"name");
        String dimensions = getParamMandatory(c,"dimensions");
        String metrics = getParamMandatory(c,"metrics");
        File cf = new File(configFile);
        GaQuery gq = null;
        try {
            gq = new GaQuery();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        gq.setDimensions(dimensions);
        gq.setMetrics(metrics);
        GaConnector.saveConfigTemplate(name, configFile, gq);
    }

    private void generateJdbcConfig(Command c) throws InvalidArgumentException, IOException, SQLException {
        String configFile = getParamMandatory(c,"configFile");
        String name = getParamMandatory(c,"name");
        String usr = null;
        if(checkParam(c,"username"))
            usr = getParam(c,"username");
        String psw = null;
        if(checkParam(c,"password"))
            psw = getParam(c,"password");
        String drv = getParamMandatory(c,"driver");
        String url = getParamMandatory(c,"url");
        String query = getParamMandatory(c,"query");
        File cf = new File(configFile);

        JdbcConnector.saveConfigTemplate(name, configFile, usr, psw, drv, url, query);
    }

    private void loadJdbc(Command c) throws InvalidArgumentException, IOException, SQLException, ModelException,
            InitializationException, MetadataFormatException {
        String pid = getProjectId(c);
        String configFile = getParamMandatory(c,"configFile");
        String usr = null;
        if(checkParam(c,"username"))
            usr = getParam(c,"username");
        String psw = null;
        if(checkParam(c,"password"))
            psw = getParam(c,"password");
        String drv = getParamMandatory(c,"driver");
        String url = getParamMandatory(c,"url");
        String query = getParamMandatory(c,"query");
        setConnector(JdbcConnector.createConnector(pid, configFile, usr, psw, drv, url, query, getBackend(),
                dbUserName, dbPassword));
    }

    private void loadCsv(Command c)
            throws InvalidArgumentException, InitializationException, MetadataFormatException, IOException,
            ModelException {
        String pid = getProjectId(c);
        String configFile = getParamMandatory(c,"configFile");
        String csvDataFile = getParamMandatory(c,"csvDataFile");
        String hdr = getParamMandatory(c,"header");
        File conf = getFile(c,configFile);
        File csvf = getFile(c,csvDataFile);
        boolean hasHeader = false;
        if(hdr.equalsIgnoreCase("true"))
            hasHeader = true;
        setConnector(CsvConnector.createConnector(pid, configFile, csvDataFile, hasHeader, getBackend(), dbUserName,
                dbPassword));
    }

    private void generateCsvConfig(Command c) throws InvalidArgumentException, IOException {
        String configFile = getParamMandatory(c,"configFile");
        String csvHeaderFile = getParamMandatory(c,"csvHeaderFile");
        File cf = new File(configFile);
        File csvf = getFile(c,csvHeaderFile);
        CsvConnector.saveConfigTemplate(configFile, csvHeaderFile);
    }

    private void createProject(Command c) throws GdcRestApiException, InvalidArgumentException {
        try {
            String name = getParamMandatory(c,"name");
            setProjectId(getRestApi().createProject(name, name));
            String pid = getProjectId(c);
            l.info("Project id = '"+pid+"' created.");
        }
        catch (GdcRestApiException e) {
            l.error("Can't create project. You are most probably over the project count quota. " +
                    "Please try deleting few projects.");            
        }
    }

     private void storeProject(Command c) throws GdcRestApiException, InvalidArgumentException, IOException {
        String fileName = getParamMandatory(c,"fileName");
        String pid = getProjectId(c);
        FileUtil.writeStringToFile(pid, fileName);
    }

    private void retrieveProject(Command c) throws GdcRestApiException, InvalidArgumentException, IOException {
        String fileName = getParamMandatory(c,"fileName");
        setProjectId(FileUtil.readStringFromFile(fileName).trim());
    }
    
    private void lock(Command c) throws IOException, InvalidArgumentException {
    	final String path = getParamMandatory(c, "path");
    	final File lock = new File(path);
    	if (!lock.createNewFile()) {
    		if (System.currentTimeMillis() - lock.lastModified() > LOCK_EXPIRATION_TIME) {
    			lock.delete();
    			if (!lock.exists()) {
    				lock(c); // retry
    			}
    		}
    		printErrorHelpandExit("A concurrent process found using the " + path + " lock file.");	
    	}
    	lock.deleteOnExit();
    }
    
    private void processNewColumns(Command c) throws InvalidArgumentException, IOException, GdcRestApiException, ModelException, AssertionError {
    	final String csvHeaderFile = getParamMandatory(c, "csvHeaderFile");
    	final String configFile = getParamMandatory(c, "configFile");
    	final String defaultLdmType = getParamMandatory(c, "defaultLdmType");
    	final String folder = getParam(c, "defaultFolder");
    	final String pid = getProjectId(c);
    	final SourceSchema schemaOld = SourceSchema.createSchema(new File(configFile));
    	final String dataset = schemaOld.getDatasetName();
    	
    	CsvConnector.saveConfigTemplate(configFile, csvHeaderFile, defaultLdmType, folder);
    	final SourceSchema schemaNew = SourceSchema.createSchema(new File(configFile));

    	final MaqlGenerator mg = new MaqlGenerator(schemaNew);
        List<DLIPart> parts = getRestApi().getDLIParts(dataset, pid);

        final List<SourceColumn> newColumns = findNewAttributes(parts, schemaNew);
         
        if (!newColumns.isEmpty()) {
        	final String maql = mg.generateMaql(newColumns);
        	getRestApi().executeMAQL(pid, maql);
        }
    }
    
    /**
     * Finds the attributes with no appropriate part.
     * TODO: a generic detector of new facts, labels etc could be added too
     * @param parts
     * @param schema
     * @return
     */
    private List<SourceColumn> findNewAttributes(List<DLIPart> parts, SourceSchema schema) {
    	Set<String> fileNames = new HashSet<String>();
    	for (final DLIPart part : parts) {
    		fileNames.add(part.getFileName());
    	}
    	
    	final List<SourceColumn> result = new ArrayList<SourceColumn>();
    	for (final SourceColumn sc : schema.getColumns()) {
    		if (SourceColumn.LDM_TYPE_ATTRIBUTE.equals(sc.getLdmType())) {
    			final String filename = MaqlGenerator.createAttributeTableName(schema, sc) + ".csv";
    			if (!fileNames.contains(filename)) {
    				result.add(sc);
    			}
    		}
    	}
    	return result;
    }

    private void setIncremental(List<DLIPart> parts) {
        for(DLIPart part : parts) {
            if(part.getFileName().startsWith(N.FCT_PFX)) {
                part.setLoadMode(DLIPart.LM_INCREMENTAL);
            }
        }
    }

    public int getBackend() {
        return backend;
    }

    public void setBackend(int backend) {
        this.backend = backend;
    }

    /**
     * attempts to find a file corresponding to given part in the <tt>dir</tt>
     * directory and creates its upload ready version with properly ordered 
     * columns in the <tt>targetDir</tt>
     * 
     * @param part
     * @param dir
     * @param targetDir
     * @throws IOException 
     */
	private void preparePartFile(DLIPart part, File dir, File targetDir, boolean reorder) throws IOException {
		final InputStream is = new FileInputStream(dir.getAbsoluteFile() + System.getProperty("file.separator") + part.getFileName());
		final OutputStream os = new FileOutputStream(targetDir.getAbsoluteFile() + System.getProperty("file.separator") + part.getFileName());
		
		if (reorder) {
			final List<String> fields = new ArrayList<String>(part.getColumns().size());
			for (final Column c : part.getColumns()) {
				fields.add(c.getName());
			}
			CsvUtil.reshuffle(is, os, fields);
		} else {
			FileUtil.copy(is, os);
		}
	}

    public String getHttpProtocol() {
        return httpProtocol;
    }

    public void setHttpProtocol(String httpProtocol) {
        this.httpProtocol = httpProtocol;
    }
}