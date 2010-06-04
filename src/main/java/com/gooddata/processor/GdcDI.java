package com.gooddata.processor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.gooddata.connector.AbstractConnector;
import com.gooddata.connector.CsvConnector;
import com.gooddata.exceptions.InvalidArgumentException;
import com.gooddata.integration.ftp.GdcFTPApiWrapper;
import com.gooddata.integration.model.DLI;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.integration.rest.GdcRESTApiWrapper;
import com.gooddata.integration.rest.configuration.NamePasswordConfiguration;
import com.gooddata.integration.rest.exceptions.GdcLoginException;
import com.gooddata.util.FileUtil;

/**
 * The GoodData Data Integration CLI processor.
 *
 * @author jiri.zaloudek
 * @author Zdenek Svoboda <zd@gooddata.org>
 * @version 1.0
 */
public class GdcDI {

    private final GdcRESTApiWrapper restApi;
    private final GdcFTPApiWrapper ftpApi;
    
    private String projectId = null;
    private AbstractConnector connector = null;

    
    private GdcDI(final String host, final String userName, final String password) throws GdcLoginException {
    	String ftpHost = null;
        // Create the FTP host automatically
        String[] hcs = host.split("\\.");
        if(hcs != null && hcs.length > 0) {
            for(String hc : hcs) {
                if(ftpHost != null && ftpHost.length()>0)
                    ftpHost += "." + hc;
                else
                    ftpHost = hc + "-upload";
            }
        }
        else {
            throw new IllegalArgumentException("Invalid format of the GoodData REST API host: " + host);
        }

        System.out.println("Using the GoodData FTP host '" + ftpHost + "'.");

        NamePasswordConfiguration httpConfiguration = new NamePasswordConfiguration("https", host,
                userName, password);
        NamePasswordConfiguration ftpConfiguration = new NamePasswordConfiguration("ftp",
                ftpHost, userName, password);
        
        restApi = new GdcRESTApiWrapper(httpConfiguration);
        ftpApi = new GdcFTPApiWrapper(ftpConfiguration);

        restApi.login();
    }
    
    private void setProject(String projectId) {
    	this.projectId = projectId;
    }
    
    public void execute(final String commandsStr) throws Exception {

        List<Command> cmds = new ArrayList<Command>();
        
        cmds.addAll(parseCmd(commandsStr));

        for(Command command : cmds) {
        	processCommand(command);
        }
    }


    /**
     * The main CLI processor
     * @param args command line argument
     * @throws Exception any issue
     */
    public static void main(String[] args) throws Exception {

        String userName = null;
        String password = null;
        String host = "secure.gooddata.com";

        Options o = new Options();

        o.addOption("u", "username", true, "GoodData username");
        o.addOption("p", "password", true, "GoodData password");
        o.addOption("h", "host", true, "GoodData host");
        o.addOption("i", "project", true, "GoodData project identifier (a string like nszfbgkr75otujmc4smtl6rf5pnmz9yl)");
        o.addOption("e", "execute", true, "Commands and params to execute before the commands in provided files");

        CommandLineParser parser = new GnuParser();
        CommandLine line = parser.parse(o, args);

        try {
	        if(line.hasOption("username")) {
	            userName = line.getOptionValue("username");
	        }
	        else
	            printErrorHelpandExit("Please specify a GoodData username (-u or --username command-line options).\n", o);
	
	        if(line.hasOption("password")) {
	            password = line.getOptionValue("password");
	        }
	        else
	            printErrorHelpandExit("Please specify a GoodData password (-p or --password command-line options).\n", o);
	
	        if(line.hasOption("host")) {
	            host = line.getOptionValue("host");
	        }
	        else {
	            System.out.println("Using the default GoodData REST API host '" + host + "'.");
	        }
	        
	        GdcDI gdcDi = new GdcDI(host, userName, password);
	        if (line.hasOption("project")) {
	        	gdcDi.setProject(line.getOptionValue("project"));
	        }
	        if (line.hasOption("execute")) {
	        	gdcDi.execute(line.getOptionValue("execute"));
	        }
	    	if (line.getArgs().length == 0 && !line.hasOption("execute")) {
        		printErrorHelpandExit("No command has been given, quitting", o);
	    	}
	        for (final String arg : line.getArgs()) {
	        	gdcDi.execute(FileUtil.readStringFromFile(arg));
	        }
        } catch (final IllegalArgumentException e) {
        	printErrorHelpandExit(e.getMessage(), o);
        }
    }

    /**
     * Parses the commands
     * @param cmd commands string
     * @return array of commands
     * @throws InvalidArgumentException in case there is an invalid command
     */
    protected static List<Command> parseCmd(String cmd) throws InvalidArgumentException {
        if(cmd != null && cmd.length()>0) {
            List<Command> cmds = new ArrayList<Command>();
            String[] commands = cmd.split(";");
            for( String component : commands) {
                component = component.trim();
                if(component != null && component.length() > 0 && !component.startsWith("#")) {                    
                    Pattern p = Pattern.compile(".*?\\(.*?\\)");
                    Matcher m = p.matcher(component);
                    if(!m.matches())
                        throw new InvalidArgumentException("Invalid command: "+component);
                    p = Pattern.compile(".*?\\(");
                    m = p.matcher(component);
                    String command = "";
                    if(m.find()) {
                        command = m.group();
                        command = command.substring(0, command.length() - 1);
                    }
                    else {
                        throw new InvalidArgumentException("Can't extract command from: "+component);
                    }
                    p = Pattern.compile("\\(.*?\\)");
                    m = p.matcher(component);
                    Properties args = new Properties();
                    if(m.find()) {
                        String as = m.group();
                        as = as.substring(1,as.length()-1);
                        try {
                            args.load(new StringReader(as.replace(",","\n")));
                        }
                        catch (IOException e) {
                            throw new InvalidArgumentException(e.getMessage());
                        }
                    }
                    else {
                        throw new InvalidArgumentException("Can't extract command from: "+component);
                    }
                    cmds.add(new Command(command, args));
                }
            }
            return cmds;
        }
        throw new InvalidArgumentException("Can't parse command.");
    }

    /**
     * Returns the help for commands
     * @return help text
     */
    protected static String commandsHelp() throws Exception {
        try {
        	final InputStream is = GdcDI.class.getResourceAsStream("/com/gooddata/processor/COMMANDS.txt");
        	if (is == null)
        		throw new IOException();
            return FileUtil.readStringFromStream(is);
        } catch (IOException e) {
            throw new Exception("Could not read com/gooddata/processor/COMMANDS.txt");
        }
    }

    /**
     * Prints an err message, help and exits with status code 1
     * @param err the err message
     * @param o options
     */
    protected static void printErrorHelpandExit(String err, Options o) throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        System.out.println("ERROR: " + err);
        System.out.println(commandsHelp());
        System.exit(1);    
    }

    /**
     * Executes the command
     * @param command to execute
     * @throws Exception general error
     */
    private void processCommand(Command command) throws Exception {        
        if(command.getCommand().equalsIgnoreCase("CreateProject")) {
            String name = (String)command.getParameters().get("name");
            String desc = (String)command.getParameters().get("desc");
            if(name != null && name.length() > 0) {
                if(desc != null && desc.length() > 0)
                    projectId = restApi.createProject(name, desc);
                else
                    projectId = restApi.createProject(name, name);
                System.out.println("Project id = '"+projectId+"' created.");
            }
            else
                throw new IllegalArgumentException("CreateProject: Command requires the 'name' parameter.");

        }

        if(command.getCommand().equalsIgnoreCase("OpenProject")) {
            String id = (String)command.getParameters().get("id");
            if(id != null && id.length() > 0) {
                projectId = id;
            }
            else {
            	throw new IllegalArgumentException("OpenProject: Command requires the 'id' parameter.");
            }
        }

        if(command.getCommand().equalsIgnoreCase("GenerateCsvConfigTemplate")) {
            String configFile = (String)command.getParameters().get("configFile");
            String csvHeaderFile = (String)command.getParameters().get("csvHeaderFile");
            if(configFile != null && configFile.length() > 0) {
                File cf = new File(configFile);
                if(csvHeaderFile != null && csvHeaderFile.length() > 0) {
                    File csvf = new File(csvHeaderFile);
                    if(csvf.exists())  {
                        CsvConnector.saveConfigTemplate(configFile, csvHeaderFile);
                    }
                    else
                    	throw new IllegalArgumentException(
                            "GenerateCsvConfigTemplate: File '" + csvHeaderFile +
                            "' doesn't exists.");
                }
                else
                	throw new IllegalArgumentException(
                            "GenerateCsvConfigTemplate: Command requires the 'csvHeaderFile' parameter.");
            }
            else
            	throw new IllegalArgumentException("GenerateCsvConfigTemplate: Command requires the 'configFile' parameter.");
        }

        if(command.getCommand().equalsIgnoreCase("LoadCsv")) {
            String configFile = (String)command.getParameters().get("configFile");
            String csvDataFile = (String)command.getParameters().get("csvDataFile");
            if(configFile != null && configFile.length() > 0) {
                File conf = new File(configFile);
                if(conf.exists()) {
                    if(csvDataFile != null && csvDataFile.length() > 0) {
                        File csvf = new File(csvDataFile);
                        if(csvf.exists())  {
                            if(projectId != null) {
                                connector = CsvConnector.createConnector(projectId, configFile, csvDataFile);
                            }
                            else
                                throw new IllegalArgumentException(
                                "LoadCsv: No active project found. Use command 'CreateProject'" +
                                " or 'OpenProject' first.");
                        }
                        else
                            throw new IllegalArgumentException(
                                "LoadCsv: File '" + csvDataFile +
                                "' doesn't exists.");
                    }
                    else
                        throw new IllegalArgumentException(
                                "LoadCsv: Command requires the 'csvHeaderFile' parameter.");
                }
                else
                    throw new IllegalArgumentException(
                                "LoadCsv: File '" + configFile +
                                "' doesn't exists.");
            }
            else
                throw new IllegalArgumentException(
                    "LoadCsv: Command requires the 'configFile' parameter.");
        }

        if(command.getCommand().equalsIgnoreCase("GenerateMaql")) {
            String maqlFile = (String)command.getParameters().get("maqlFile");
            if(maqlFile != null && maqlFile.length() > 0) {
                if(connector != null) {
                    String maql = connector.generateMaql();
                    FileUtil.writeStringToFile(maql, maqlFile);
                }
                else
                	throw new IllegalArgumentException("GenerateMaql: No data source loaded. Use a 'LoadXXX' to load a data source.");


            }
            else
            	throw new IllegalArgumentException("GenerateMaql: Command requires the 'maqlFile' parameter.");

        }

        if(command.getCommand().equalsIgnoreCase("ExecuteMaql")) {
            String maqlFile = (String)command.getParameters().get("maqlFile");
            if(maqlFile != null && maqlFile.length() > 0) {
                File mf = new File(maqlFile);
                if(mf.exists()) {
                    if(projectId != null) {
                        String maql = FileUtil.readStringFromFile(maqlFile);
                        restApi.executeMAQL(projectId, maql);
                    }
                    else
                    	throw new IllegalArgumentException(
                                    "ExecuteMaql: No active project found. Use command 'CreateProject'" +
                                    " or 'OpenProject' first.");
                }
                else
                	throw new IllegalArgumentException(
                                    "ExecuteMaql: File '" + maqlFile +
                                    "' doesn't exists.");
            }
            else
            	throw new IllegalArgumentException("ExecuteMaql: Command requires the 'maqlFile' parameter.");

        }

        if(command.getCommand().equalsIgnoreCase("ListSnapshots")) {
            if(connector != null) {
                System.out.println(connector.listSnapshots());
            }
            else
            	throw new IllegalArgumentException("ListSnapshots: No data source loaded. Use a 'LoadXXX' to load a data source.");

        }

        if(command.getCommand().equalsIgnoreCase("DropSnapshots")) {
            if(connector != null) {
                connector.dropSnapshots();
            }
            else
            	throw new IllegalArgumentException("DropSnapshots: No data source loaded. Use a 'LoadXXX' to load a data source.");

        }

        if(command.getCommand().equalsIgnoreCase("TransferData")) {
            if(connector != null) {
                if(!connector.isInitialized())
                    connector.initialize();

                // retrieve the DLI
                DLI dli = restApi.getDLIById("dataset." + connector.getName().toLowerCase(), projectId);
                List<DLIPart> parts= restApi.getDLIParts("dataset." + connector.getName().toLowerCase(), projectId);
                // target directories and ZIP names

                String incremental = (String)command.getParameters().get("incremental");
                if(incremental != null && incremental.length() > 0 && incremental.equalsIgnoreCase("true")) {
                    for(DLIPart part : parts) {
                        if(part.getFileName().startsWith("f_")) {
                            part.setLoadMode(DLIPart.LM_INCREMENTAL);
                        }
                    }
                }

                File tmpDir = FileUtil.createTempDir();
                File tmpZipDir = FileUtil.createTempDir();
                String archiveName = tmpDir.getName();
                String archivePath = tmpZipDir.getAbsolutePath() + System.getProperty("file.separator") +
                        archiveName + ".zip";
                // loads the CSV data to the embedded Derby SQL
                connector.extract();
                // normalize the data in the Derby
                connector.transform();
                // load data from the Derby to the local GoodData data integration package
                connector.deploy(dli, parts, tmpDir.getAbsolutePath(), archivePath);
                // transfer the data package to the GoodData server
                ftpApi.transferDir(archivePath);
                // kick the GooDData server to load the data package to the project
                restApi.startLoading(projectId, archiveName);
            }
            else
            	throw new IllegalArgumentException("TransferData: No data source loaded. Use a 'LoadXXX' to load a data source.");

        }

        if(command.getCommand().equalsIgnoreCase("TransferSnapshots")) {
            String firstSnapshot = (String)command.getParameters().get("firstSnapshot");
            String lastSnapshot = (String)command.getParameters().get("lastSnapshot");
            if(firstSnapshot != null && firstSnapshot.length() > 0) {
                if(lastSnapshot != null && lastSnapshot.length() > 0) {
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
                        if(connector != null) {
                            if(!connector.isInitialized())
                                connector.initialize();
                            // retrieve the DLI
                            DLI dli = restApi.getDLIById("dataset." + connector.getName().toLowerCase(),
                                    projectId);
                            List<DLIPart> parts= restApi.getDLIParts("dataset." +
                                    connector.getName().toLowerCase(), projectId);

                            String incremental = (String)command.getParameters().get("incremental");
                            if(incremental != null && incremental.length() > 0 &&
                                    incremental.equalsIgnoreCase("true")) {
                                for(DLIPart part : parts) {
                                    if(part.getFileName().startsWith("f_")) {
                                        part.setLoadMode(DLIPart.LM_INCREMENTAL);
                                    }
                                }
                            }

                            // target directories and ZIP names
                            File tmpDir = FileUtil.createTempDir();
                            File tmpZipDir = FileUtil.createTempDir();
                            String archiveName = tmpDir.getName();
                            String archivePath = tmpZipDir.getAbsolutePath() +
                                    System.getProperty("file.separator") + archiveName + ".zip";
                            // loads the CSV data to the embedded Derby SQL
                            connector.extract();
                            // normalize the data in the Derby
                            connector.transform();
                            // load data from the Derby to the local GoodData data integration package
                            connector.deploySnapshot(dli, parts, tmpDir.getAbsolutePath(), archivePath,
                                    snapshots);
                            // transfer the data package to the GoodData server
                            ftpApi.transferDir(archivePath);
                            // kick the GooDData server to load the data package to the project
                            restApi.startLoading(projectId, archiveName);
                        }
                        else
                        	throw new IllegalArgumentException("TransferSnapshots: No data source loaded." +
                                        "Use a 'LoadXXX' to load a data source.");
                    }
                    else
                    	throw new IllegalArgumentException("TransferSnapshots: The 'lastSnapshot' (" + lastSnapshot +
                                ") parameter must be higher than the 'firstSnapshot' (" + firstSnapshot +
                                ") parameter.");
                }
                else
                	throw new IllegalArgumentException("TransferSnapshots: Command requires the 'lastSnapshot' parameter.");
            }
            else
            	throw new IllegalArgumentException("TransferSnapshots: Command requires the 'firstSnapshot' parameter.");
        }

        if(command.getCommand().equalsIgnoreCase("TransferLastSnapshot")) {
            if(connector != null) {
                if(!connector.isInitialized())
                    connector.initialize();
                // retrieve the DLI
                DLI dli = restApi.getDLIById("dataset." + connector.getName().toLowerCase(),
                        projectId);
                List<DLIPart> parts= restApi.getDLIParts("dataset." +
                        connector.getName().toLowerCase(), projectId);

                String incremental = (String)command.getParameters().get("incremental");
                if(incremental != null && incremental.length() > 0 &&
                        incremental.equalsIgnoreCase("true")) {
                    for(DLIPart part : parts) {
                        if(part.getFileName().startsWith("f_")) {
                            part.setLoadMode(DLIPart.LM_INCREMENTAL);
                        }
                    }
                }

                // target directories and ZIP names
                File tmpDir = FileUtil.createTempDir();
                File tmpZipDir = FileUtil.createTempDir();
                String archiveName = tmpDir.getName();
                String archivePath = tmpZipDir.getAbsolutePath() +
                        System.getProperty("file.separator") + archiveName + ".zip";
                // loads the CSV data to the embedded Derby SQL
                connector.extract();
                // normalize the data in the Derby
                connector.transform();
                // load data from the Derby to the local GoodData data integration package
                connector.deploySnapshot(dli, parts, tmpDir.getAbsolutePath(), archivePath,
                        new int[] {connector.getLastSnapshotId()});
                // transfer the data package to the GoodData server
                ftpApi.transferDir(archivePath);
                // kick the GooDData server to load the data package to the project
                restApi.startLoading(projectId, archiveName);
            }
            else
            	throw new IllegalArgumentException("TransferLastSnapshot: No data source loaded." +
                        "Use a 'LoadXXX' to load a data source.");
        }
    }
 
}
