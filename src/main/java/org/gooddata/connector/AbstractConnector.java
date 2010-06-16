package org.gooddata.connector;

import com.gooddata.connector.CsvConnector;
import com.gooddata.exception.*;
import com.gooddata.integration.model.DLI;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.modeling.generator.MaqlGenerator;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.connector.model.PdmSchema;
import com.gooddata.naming.N;
import com.gooddata.processor.CliParams;
import com.gooddata.processor.Command;
import com.gooddata.processor.ProcessingContext;
import com.gooddata.util.FileUtil;
import com.gooddata.util.StringUtil;
import org.apache.log4j.Logger;
import org.gooddata.connector.backend.ConnectorBackend;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * GoodData
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public abstract class AbstractConnector implements Connector {

    private static Logger l = Logger.getLogger(AbstractConnector.class);

    /**
     * The LDM schema of the data source
     */
    protected SourceSchema schema;

    // Connector backend
    private ConnectorBackend connectorBackend;

    protected AbstractConnector() {
    }

    /**
     * GoodData CSV connector. This constructor creates the connector from a config file
     * @throws com.gooddata.exception.InitializationException issues with the initialization
     * @throws com.gooddata.exception.MetadataFormatException issues with the metadata definitions
     * @throws IOException in case of an IO issue
     */
    protected AbstractConnector(ConnectorBackend backend) {
        setConnectorBackend(backend);
    }

    /**
     * Generates the MAQL for the data source
     * @return the MAQL in string format
     */
    public String generateMaql() {
        MaqlGenerator mg = new MaqlGenerator(schema);
        return mg.generateMaql();
    }
    
    /**
     * Generates the MAQL for the specified columns of the data source
     * @return the MAQL in string format
     */
    public String generateMaql(List<SourceColumn> columns) {
        MaqlGenerator mg = new MaqlGenerator(schema);
        return mg.generateMaql(columns);
    }

    /**
     * LDM schema getter
     * @return LDM schema
     */
    public SourceSchema getSchema() {
        return schema;
    }

    /**
     * LDM schema setter
     * @param schema LDM schema
     */
    public void setSchema(SourceSchema schema) {
        this.schema = schema;
    }

    /**
     * Connector backend getter
     * @return the connector backend
     */
    public ConnectorBackend getConnectorBackend() {
        return connectorBackend;
    }

    /**
     * Connector backend setter
     * @param connectorBackend the connector backend
     */
    public void setConnectorBackend(ConnectorBackend connectorBackend) {
        this.connectorBackend = connectorBackend;
    }

    /**
     * Drops all snapshots
     */
    public void dropSnapshots() {
        getConnectorBackend().dropSnapshots();
    }

    /**
     * Lists the current snapshots
     * @return list of snapshots as String
     * @throws com.gooddata.exception.InternalErrorException in case of internal issues (e.g. uninitialized schema)
     */
    public String listSnapshots() throws InternalErrorException {
        return getConnectorBackend().listSnapshots();
    }

    /**
     * Figures out if the connector is initialized
     * @return the initialization status
     */
    public boolean isInitialized() {
        return getConnectorBackend().isInitialized();
    }

    /**
     * Get last snapshot number
     * @return last snapshot number
     * @throws InternalErrorException in case of internal issues (e.g. uninitialized schema)
     */
    public int getLastSnapshotId() throws InternalErrorException {
        return getConnectorBackend().getLastSnapshotId();
    }


    /**
     * Create the GoodData data package with the ALL data
     * @param dli the Data Loading Interface that contains the required data structures
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     * @param archiveName the name of the target ZIP archive
     * @throws IOException IO issues
     * @throws com.gooddata.exception.ModelException in case of PDM schema issues
     */
    public void deploy(DLI dli, List<DLIPart> parts, String dir, String archiveName)
            throws IOException, ModelException {
        getConnectorBackend().deploy(dli, parts, dir, archiveName);
    }

    /**
     * Create the GoodData data package with the data from specified snapshots
     * @param dli the Data Loading Interface that contains the required data structures
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     * @param archiveName the name of the target ZIP archive
     * @param snapshotIds snapshot ids that are going to be loaded (if NULL, all snapshots are going to be loaded)
     * @throws IOException IO issues
     * @throws ModelException in case of PDM schema issues
     */
    public void deploySnapshot(DLI dli, List<DLIPart> parts, String dir, String archiveName, int[] snapshotIds)
            throws IOException, ModelException {
        getConnectorBackend().deploySnapshot(dli, parts, dir, archiveName, snapshotIds);
    }

    /**
     * Initializes the Derby database schema that is going to be used for the data normalization
     * @throws com.gooddata.exception.ModelException imn case of PDM schema issues
     */
    public void initialize() throws ModelException {
        getConnectorBackend().initialize();
    }

    /**
     * Perform the data normalization (generate lookups) in the Derby database. The database must contain the required
     * tables
     * @throws ModelException in case of PDM schema issues
     */
    public void transform() throws ModelException {        
        getConnectorBackend().transform();
    }

    /**
     * Extracts the source data CSV to the Derby database where it is going to be transformed
     * @throws ModelException in case of PDM schema issues
     */
    public abstract void extract() throws ModelException, IOException;


    /**
     * Initializes the source and PDM schemas from the config file
     * @param configFileName the config file
     * @throws IOException in cas the config file doesn't exists
     */
    protected void initSchema(String configFileName) throws IOException {
        schema = SourceSchema.createSchema(new File(configFileName));
        connectorBackend.setPdm(PdmSchema.createSchema(schema));
    }

    /**
     * Processes single command
     * @param c command to be processed
     * @param cli parameters (commandline params)
     * @param ctx processing context
     * @return true if the command has been processed, false otherwise
     */
    public boolean processCommand(Command c, CliParams cli, ProcessingContext ctx) throws ProcessingException {
        try {
            if(c.match("GenerateMaql")) {
                generateMAQL(c, cli, ctx);
            }
            else if(c.match("ExecuteMaql")) {
                executeMAQL(c, cli, ctx);
            }
            else if(c.match("ListSnapshots")) {
                listSnapshots(c, cli, ctx);
            }
            else if(c.match("DropSnapshots")) {
                dropSnapshots(c, cli, ctx);
            }
            else if(c.match("TransferData")) {
                transferData(c, cli, ctx);
            }
            else if(c.match("TransferSnapshots")) {
                transferSnapshots(c, cli, ctx);
            }
            else if(c.match("TransferLastSnapshot")) {
                transferLastSnapshot(c, cli, ctx);
            }
            else if (c.match( "UpdateConfig")) {
                updateConfig(c, cli, ctx);
            }
            else if (c.match( "GenerateUpdateMaql")) {
                generateUpdateMaql(c, cli, ctx);
            }
            else
                return false;
            return true;
        }
        catch (IOException e) {
            throw new ProcessingException(e);
        }
        catch (InterruptedException e) {
            throw new ProcessingException(e);
        }
    }

    private void generateMAQL(Command c, CliParams p, ProcessingContext ctx) throws InvalidArgumentException, IOException {
        Connector cc = ctx.getConnector();
        String maqlFile = c.getParamMandatory("maqlFile");
        String maql = cc.generateMaql();
        FileUtil.writeStringToFile(maql, maqlFile);
    }

    private void executeMAQL(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        String pid = ctx.getProjectId();
        final String maqlFile = c.getParamMandatory("maqlFile");
        final String ifExistsStr = c.getParam("ifExists");
        final boolean ifExists = (ifExistsStr != null && "true".equalsIgnoreCase(ifExistsStr));
        final File mf = FileUtil.getFile(maqlFile, ifExists);
        if (mf != null) {
	        final String maql = FileUtil.readStringFromFile(maqlFile);
	        ctx.getRestApi(p).executeMAQL(pid, maql);
        }
    }

    private void transferData(Command c, CliParams p, ProcessingContext ctx) throws InvalidArgumentException, ModelException, IOException, GdcRestApiException, InterruptedException {
        Connector cc = ctx.getConnector();
        String pid = ctx.getProjectId();
        // connector's schema name
        String ssn = StringUtil.formatShortName(cc.getSchema().getName());
        cc.initialize();
        // retrieve the DLI
        DLI dli = ctx.getRestApi(p).getDLIById("dataset." + ssn, pid);
        List<DLIPart> parts= ctx.getRestApi(p).getDLIParts("dataset." + ssn, pid);
        // target directories and ZIP names

        String incremental = c.getParam("incremental");
        if(incremental != null && incremental.length() > 0 && incremental.equalsIgnoreCase("true")) {
            setIncremental(parts);
        }
        boolean waitForFinish = true;
        if(c.checkParam("waitForFinish")) {
            String w = c.getParam( "waitForFinish");
            if(w != null && w.equalsIgnoreCase("false"))
                waitForFinish = false;
        }
        extractAndTransfer(c, pid, cc, dli, parts, null, waitForFinish, p, ctx);
    }

    private void extractAndTransfer(Command c, String pid, Connector cc, DLI dli, List<DLIPart> parts,
        int[] snapshots, boolean waitForFinish, CliParams p, ProcessingContext ctx) throws IOException, ModelException, GdcRestApiException, InvalidArgumentException, InterruptedException {
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
        ctx.getFtpApi(p).transferDir(archivePath);
        // kick the GooDData server to load the data package to the project
        String taskUri = ctx.getRestApi(p).startLoading(pid, archiveName);
        if(waitForFinish) {
            checkLoadingStatus(taskUri, tmpDir.getName(), p, ctx);
        }
        //cleanup
        FileUtil.recursiveDelete(tmpDir);
        FileUtil.recursiveDelete(tmpZipDir);
    }

    private void checkLoadingStatus(String taskUri, String tmpDir, CliParams p, ProcessingContext ctx) throws HttpMethodException, GdcLoginException, InterruptedException, GdcUploadErrorException, IOException {
        String status = "";
        while(!status.equalsIgnoreCase("OK") && !status.equalsIgnoreCase("ERROR") && !status.equalsIgnoreCase("WARNING")) {
            status = ctx.getRestApi(p).getLoadingStatus(taskUri);
            l.debug("Loading status = "+status);
            Thread.sleep(500);
        }
        if(!status.equalsIgnoreCase("OK")) {
            l.info("Data loading failed. Status: "+status);
            Map<String,String> result = ctx.getFtpApi(p).getTransferLogs(tmpDir);
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

    private void transferLastSnapshot(Command c, CliParams p, ProcessingContext ctx) throws InterruptedException, IOException {
        Connector cc = ctx.getConnector();
        String pid = ctx.getProjectId();
        // connector's schema name
        String ssn = StringUtil.formatShortName(cc.getSchema().getName());

        cc.initialize();
        // retrieve the DLI
        DLI dli = ctx.getRestApi(p).getDLIById("dataset." + ssn, pid);
        List<DLIPart> parts= ctx.getRestApi(p).getDLIParts("dataset." + ssn, pid);

        String incremental = c.getParam("incremental");
        if(incremental != null && incremental.length() > 0 &&
                incremental.equalsIgnoreCase("true")) {
            setIncremental(parts);
        }
        boolean waitForFinish = true;
        if(c.checkParam("waitForFinish")) {
            String w = c.getParam( "waitForFinish");
            if(w != null && w.equalsIgnoreCase("false"))
                waitForFinish = false;
        }
        extractAndTransfer(c, pid, cc, dli, parts, new int[] {cc.getLastSnapshotId()+1}, waitForFinish, p, ctx);
    }

    private void transferSnapshots(Command c, CliParams p, ProcessingContext ctx) throws InterruptedException, IOException {
        Connector cc = ctx.getConnector();
        String pid = ctx.getProjectId();
        String firstSnapshot = c.getParamMandatory("firstSnapshot");
        String lastSnapshot = c.getParamMandatory("lastSnapshot");
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
            DLI dli = ctx.getRestApi(p).getDLIById("dataset." + ssn, pid);
            List<DLIPart> parts= ctx.getRestApi(p).getDLIParts("dataset." +ssn, pid);

            String incremental = c.getParam("incremental");
            if(incremental != null && incremental.length() > 0 &&
                    incremental.equalsIgnoreCase("true"))
                setIncremental(parts);
            boolean waitForFinish = true;
            if(c.checkParam("waitForFinish")) {
                String w = c.getParam( "waitForFinish");
                if(w != null && w.equalsIgnoreCase("false"))
                    waitForFinish = false;
            }
            extractAndTransfer(c, pid, cc, dli, parts, snapshots, waitForFinish, p, ctx);
        }
        else
            throw new InvalidParameterException(c.getCommand()+": The firstSnapshot can't be higher than the lastSnapshot.");
    }

    private void dropSnapshots(Command c, CliParams p, ProcessingContext ctx) {
        Connector cc = ctx.getConnector();
        cc.dropSnapshots();
    }

    private void listSnapshots(Command c, CliParams p, ProcessingContext ctx) {
        Connector cc = ctx.getConnector();
        l.info((cc.listSnapshots()));
    }

    private void updateConfig(Command c, CliParams p, ProcessingContext ctx) throws InvalidArgumentException, IOException {
    	final String csvHeaderFile = c.getParamMandatory( "csvHeaderFile");
    	final String configFile = c.getParamMandatory( "configFile");
    	final String defaultLdmType = c.getParamMandatory( "defaultLdmType");
    	final String folder = c.getParam( "defaultFolder");

    	CsvConnector.saveConfigTemplate(configFile, csvHeaderFile, defaultLdmType, folder);
    }

    private void generateUpdateMaql(Command c, CliParams p, ProcessingContext ctx) throws InvalidArgumentException, IOException, GdcLoginException, HttpMethodException {
    	final String configFile = c.getParamMandatory( "configFile");
    	final SourceSchema schema = SourceSchema.createSchema(new File(configFile));

    	final String pid = ctx.getProjectId();
    	final String maqlFile = c.getParamMandatory( "maqlFile");
    	final String dataset = schema.getDatasetName();

    	Connector cc = ctx.getConnector();
        List<DLIPart> parts = ctx.getRestApi(p).getDLIParts(dataset, pid);

        final List<SourceColumn> newColumns = findNewAttributes(parts, schema);
        if (!newColumns.isEmpty()) {
        	final String maql = cc.generateMaql(newColumns);
        	FileUtil.writeStringToFile(maql, maqlFile);
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

    protected void checkProjectId() throws InvalidParameterException {
        String pid = getConnectorBackend().getProjectId();
        if(pid == null || pid.length()<=0)
            throw new InvalidParameterException("No project is active. Please activate project via CreateProject or " +
                    "OpenProject command. ");
    }



}
