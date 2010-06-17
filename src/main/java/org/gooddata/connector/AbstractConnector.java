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
 * GoodData abstract connector implements functionality that can be reused in several connectors.
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

    /**
     * Default constructor
     */
    protected AbstractConnector() {
    }

    /**
     * GoodData abstract connector.
     * @param backend initialized connector backend
     */
    protected AbstractConnector(ConnectorBackend backend) {
        setConnectorBackend(backend);
    }

    /**
     * {@inheritDoc}
     */
    public String generateMaql() {
        MaqlGenerator mg = new MaqlGenerator(schema);
        return mg.generateMaql();
    }
    
    /**
     * {@inheritDoc}
     */
    public String generateMaql(List<SourceColumn> columns) {
        MaqlGenerator mg = new MaqlGenerator(schema);
        return mg.generateMaql(columns);
    }

    /**
     * {@inheritDoc}
     */
    public SourceSchema getSchema() {
        return schema;
    }

    /**
     * {@inheritDoc}
     */
    public void setSchema(SourceSchema schema) {
        this.schema = schema;
    }

    /**
     * {@inheritDoc}
     */
    public ConnectorBackend getConnectorBackend() {
        return connectorBackend;
    }

    /**
     * {@inheritDoc}
     */
    public void setConnectorBackend(ConnectorBackend connectorBackend) {
        this.connectorBackend = connectorBackend;
    }

    /**
     * {@inheritDoc}
     */
    public void dropSnapshots() {
        getConnectorBackend().dropSnapshots();
    }

    /**
     * {@inheritDoc}
     */
    public String listSnapshots() {
        return getConnectorBackend().listSnapshots();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInitialized() {
        return getConnectorBackend().isInitialized();
    }

    /**
     * {@inheritDoc}
     */
    public int getLastSnapshotId() {
        return getConnectorBackend().getLastSnapshotId();
    }


    /**
     * {@inheritDoc}
     */
    public void deploy(DLI dli, List<DLIPart> parts, String dir, String archiveName)
            throws IOException {
        getConnectorBackend().deploy(dli, parts, dir, archiveName);
    }

    /**
     * {@inheritDoc}
     */
    public void deploySnapshot(DLI dli, List<DLIPart> parts, String dir, String archiveName, int[] snapshotIds)
            throws IOException {
        getConnectorBackend().deploySnapshot(dli, parts, dir, archiveName, snapshotIds);
    }

    /**
     * {@inheritDoc}
     */
    public void initialize()  {
        getConnectorBackend().initialize();
    }

    /**
     * {@inheritDoc}
     */
    public void transform() {
        getConnectorBackend().transform();
    }

    /**
     * {@inheritDoc}
     */
    public abstract void extract() throws IOException;


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
     * {@inheritDoc}
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

    /**
     * Generates the MAQL
     * @param c command
     * @param p cli parameters
     * @param ctx current context
     * @throws IOException IO issues
     */
    private void generateMAQL(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        Connector cc = ctx.getConnector();
        String maqlFile = c.getParamMandatory("maqlFile");
        String maql = cc.generateMaql();
        FileUtil.writeStringToFile(maql, maqlFile);
    }

    /**
     * Executes MAQL
     * @param c command
     * @param p cli parameters
     * @param ctx current context
     * @throws IOException IO issues
     */
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

    /**
     * Transfers the data to GoodData project
     * @param c command
     * @param p cli parameters
     * @param ctx current context
     * @throws IOException IO issues
     * @throws InterruptedException internal problem with making file writable
     */
    private void transferData(Command c, CliParams p, ProcessingContext ctx) throws IOException, InterruptedException {
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

    /**
     * Extract data from the internal database and transfer them to a GoodData project
     * @param c command
     * @param p cli parameters
     * @param ctx current context
     * @param dli data loading interface
     * @param parts DLI parts
     * @param snapshots transferred snapshots
     * @param waitForFinish synchronous execution flag
     * @throws IOException IO issues
     * @throws InterruptedException internal problem with making file writable
     */
    private void extractAndTransfer(Command c, String pid, Connector cc, DLI dli, List<DLIPart> parts,
        int[] snapshots, boolean waitForFinish, CliParams p, ProcessingContext ctx) throws IOException, InterruptedException {
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

    /**
     * Checks the status of data integration process in the GoodData platform
     * @param taskUri the uri where the task status is determined
     * @param tmpDir temporary dir where the temporary data reside. This directory will be deleted.
     * @param p cli parameters
     * @param ctx current context
     * @throws IOException IO issues
     * @throws InterruptedException internal problem with making file writable
     */
    private void checkLoadingStatus(String taskUri, String tmpDir, CliParams p, ProcessingContext ctx) throws InterruptedException,IOException {
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

    /**
     * Makes a directory writable on the Unix machines
     * @param tmpDir the directory that will be made writable
     */
    private void makeWritable(File tmpDir) {
        try {
            Runtime.getRuntime().exec("chmod -R 777 "+tmpDir.getAbsolutePath());
        }
        catch (IOException e) {
            l.debug("CHMOD execution failed. No big deal perhaps you are running Windows.", e);
        }
    }

    /**
     * Transfers the last snapshot of data to the GoodData project
     * @param c command
     * @param p cli parameters
     * @param ctx current context
     * @throws IOException IO issues
     * @throws InterruptedException internal problem with making file writable
     */
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

    /**
     * Transfers selected snapshots to the GoodData project
     * @param c command
     * @param p cli parameters
     * @param ctx current context
     * @throws IOException IO issues
     * @throws InterruptedException internal problem with making file writable
     */
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

    /**
     * Drops all snapshots (drop the entire project database)
     * @param c command
     * @param p cli parameters
     * @param ctx current context
     */
    private void dropSnapshots(Command c, CliParams p, ProcessingContext ctx) {
        Connector cc = ctx.getConnector();
        cc.dropSnapshots();
    }

    /**
     * Lists the current snapshots
     * @param c command
     * @param p cli parameters
     * @param ctx current context
     */
    private void listSnapshots(Command c, CliParams p, ProcessingContext ctx) {
        Connector cc = ctx.getConnector();
        l.info((cc.listSnapshots()));
    }

    /**
     * Updates the config with additional columns
     * @param c command
     * @param p cli parameters
     * @param ctx current context
     * @throws IOException IO issues
     */
    private void updateConfig(Command c, CliParams p, ProcessingContext ctx) throws IOException {
    	final String csvHeaderFile = c.getParamMandatory( "csvHeaderFile");
    	final String configFile = c.getParamMandatory( "configFile");
    	final String defaultLdmType = c.getParamMandatory( "defaultLdmType");
    	final String folder = c.getParam( "defaultFolder");

    	CsvConnector.saveConfigTemplate(configFile, csvHeaderFile, defaultLdmType, folder);
    }

    /**
     * Generate the MAQL for new columns 
     * @param c command
     * @param p cli parameters
     * @param ctx current context
     * @throws IOException IO issue
     */
    private void generateUpdateMaql(Command c, CliParams p, ProcessingContext ctx) throws IOException {
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
     * @param parts DLI parts
     * @param schema former source schema
     * @return list of new columns
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

    /**
     * Sets the incremental loading status for a part
     * @param parts DLI part
     */
    private void setIncremental(List<DLIPart> parts) {
        for(DLIPart part : parts) {
            if(part.getFileName().startsWith(N.FCT_PFX)) {
                part.setLoadMode(DLIPart.LM_INCREMENTAL);
            }
        }
    }

    /**
     * Sets the project id from context
     * @param ctx process context
     * @throws InvalidParameterException if the project id isn't initialized
     */
    protected void setProjectId(ProcessingContext ctx) throws InvalidParameterException {
        String pid = ctx.getProjectId();
        if(pid != null && pid.length() > 0)
            this.getConnectorBackend().setProjectId(pid);
        else
            throw new InvalidParameterException("No project is active. Please activate project via CreateProject or " +
                    "OpenProject command. ");
    }

}
