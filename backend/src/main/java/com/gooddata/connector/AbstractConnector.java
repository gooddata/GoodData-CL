/*
 * Copyright (c) 2009, GoodData Corporation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice, this list of conditions and
 *        the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *        and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *     * Neither the name of the GoodData Corporation nor the names of its contributors may be used to endorse
 *        or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.gooddata.connector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.gooddata.connector.backend.ConnectorBackend;
import com.gooddata.connector.model.PdmSchema;
import com.gooddata.exception.InvalidCommandException;
import com.gooddata.exception.InvalidParameterException;
import com.gooddata.exception.ProcessingException;
import com.gooddata.integration.model.DLI;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.modeling.generator.MaqlGenerator;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.naming.N;
import com.gooddata.processor.CliParams;
import com.gooddata.processor.Command;
import com.gooddata.processor.ProcessingContext;
import com.gooddata.util.FileUtil;
import com.gooddata.util.StringUtil;

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
        l.debug("Executing maql generation.");
        MaqlGenerator mg = new MaqlGenerator(schema);
        String maql = mg.generateMaql();
        l.debug("Finished maql generation maql:\n"+maql);
        return maql;
    }
    
    /**
     * {@inheritDoc}
     */
    public String generateMaql(List<SourceColumn> columns) {
        l.debug("Executing maql generation for columns "+columns);
        MaqlGenerator mg = new MaqlGenerator(schema);
        String maql = mg.generateMaql(columns);
        l.debug("Finished maql generation maql:\n"+maql);
        return maql;
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
        l.debug("Processing command "+c.getCommand());
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
            else if(c.match("TransferData") || c.match("TransferAllSnapshots")) {
                transferData(c, cli, ctx);
            }
            else if(c.match("TransferSnapshots")) {
                transferSnapshots(c, cli, ctx);
            }
            else if(c.match("TransferLastSnapshot")) {
                transferLastSnapshot(c, cli, ctx);
            }
            else if (c.match( "GenerateUpdateMaql")) {
                generateUpdateMaql(c, cli, ctx);
            }
            else {
                l.debug("No match for command "+c.getCommand());
                return false;
            }
            l.debug("Command "+c.getCommand()+" processed.");
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
        Connector cc = ctx.getConnectorMandatory();
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
        l.debug("Executing MAQL.");
        String pid = ctx.getProjectIdMandatory();
        final String maqlFile = c.getParamMandatory("maqlFile");
        final String ifExistsStr = c.getParam("ifExists");
        final boolean ifExists = (ifExistsStr != null && "true".equalsIgnoreCase(ifExistsStr));
        final File mf = FileUtil.getFile(maqlFile, ifExists);
        if (mf != null) {
	        final String maql = FileUtil.readStringFromFile(maqlFile);
	        ctx.getRestApi(p).executeMAQL(pid, maql);
        }
        l.debug("Finished MAQL execution.");
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
        l.debug("Transferring data.");
        Connector cc = ctx.getConnectorMandatory();
        String pid = ctx.getProjectIdMandatory();
        // connector's schema name
        String ssn = StringUtil.formatShortName(cc.getSchema().getName());
        cc.initialize();
        
        boolean waitForFinish = true;
        if(c.checkParam("waitForFinish")) {
            String w = c.getParam( "waitForFinish");
            if(w != null && w.equalsIgnoreCase("false"))
                waitForFinish = false;
        }
        extractAndTransfer(c, pid, cc, null, waitForFinish, p, ctx);
        l.debug("Data transfer finished.");
    }

    /**
     * Extract data from the internal database and transfer them to a GoodData project
     * @param c command
     * @param pid project id
     * @param cc connector
     * @param p cli parameters
     * @param ctx current context
     * @param snapshots transferred snapshots
     * @param waitForFinish synchronous execution flag
     * @throws IOException IO issues
     * @throws InterruptedException internal problem with making file writable
     */
    private void extractAndTransfer(Command c, String pid, Connector cc, int[] snapshots, boolean waitForFinish, CliParams p, ProcessingContext ctx)
    	throws IOException, InterruptedException
    {
        // connector's schema name
        String ssn = StringUtil.formatShortName(cc.getSchema().getName());
        l.debug("Extracting data.");
        File tmpDir = FileUtil.createTempDir();
        FileUtil.makeWritable(tmpDir);
        File tmpZipDir = FileUtil.createTempDir();
        String archiveName = tmpDir.getName();
        String archivePath = tmpZipDir.getAbsolutePath() + System.getProperty("file.separator") +
            archiveName + ".zip";
        // loads the CSV data to the embedded Derby SQL
        cc.extract();
        // normalize the data in the Derby
        cc.transform();

        // get information about the data loading package      
        DLI dli = ctx.getRestApi(p).getDLIById("dataset." + ssn, pid);
        List<DLIPart> parts= ctx.getRestApi(p).getDLIParts("dataset." +ssn, pid);

        String incremental = c.getParam("incremental");
        if(incremental != null && incremental.length() > 0 &&
                incremental.equalsIgnoreCase("true")) {
            l.debug("Using incremental mode.");
            setIncremental(parts);
        }
        
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
        l.debug("Cleaning the temporary files.");
        FileUtil.recursiveDelete(tmpDir);
        FileUtil.recursiveDelete(tmpZipDir);
        l.debug("Data extract finished.");
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
        l.debug("Checking data transfer status.");
        String status = "";
        while(!status.equalsIgnoreCase("OK") && !status.equalsIgnoreCase("ERROR") && !status.equalsIgnoreCase("WARNING")) {
            status = ctx.getRestApi(p).getLoadingStatus(taskUri);
            l.debug("Loading status = "+status);
            Thread.sleep(500);
        }
        l.debug("Data transfer finished with status "+status);
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
     * Transfers the last snapshot of data to the GoodData project
     * @param c command
     * @param p cli parameters
     * @param ctx current context
     * @throws IOException IO issues
     * @throws InterruptedException internal problem with making file writable
     */
    private void transferLastSnapshot(Command c, CliParams p, ProcessingContext ctx) throws InterruptedException, IOException {
        l.debug("Transfering last snapshot.");
        Connector cc = ctx.getConnectorMandatory();
        String pid = ctx.getProjectIdMandatory();

        cc.initialize();
        boolean waitForFinish = true;
        if(c.checkParam("waitForFinish")) {
            String w = c.getParam( "waitForFinish");
            if(w != null && w.equalsIgnoreCase("false"))
                waitForFinish = false;
        }
        extractAndTransfer(c, pid, cc, new int[] {cc.getLastSnapshotId()+1}, waitForFinish, p, ctx);
        l.debug("Last Snapshot transfer finished.");
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
        Connector cc = ctx.getConnectorMandatory();
        String pid = ctx.getProjectIdMandatory();
        String firstSnapshot = c.getParamMandatory("firstSnapshot");
        String lastSnapshot = c.getParamMandatory("lastSnapshot");
        l.debug("Transfering snapshots "+firstSnapshot+" - " + lastSnapshot);
        int fs,ls;
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

            boolean waitForFinish = true;
            if(c.checkParam("waitForFinish")) {
                String w = c.getParam( "waitForFinish");
                if(w != null && w.equalsIgnoreCase("false"))
                    waitForFinish = false;
            }
            extractAndTransfer(c, pid, cc, snapshots, waitForFinish, p, ctx);
            l.debug("Snapshots transfer finished.");
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
        Connector cc = ctx.getConnectorMandatory();
        cc.dropSnapshots();
    }

    /**
     * Lists the current snapshots
     * @param c command
     * @param p cli parameters
     * @param ctx current context
     */
    private void listSnapshots(Command c, CliParams p, ProcessingContext ctx) {
        Connector cc = ctx.getConnectorMandatory();
        l.info((cc.listSnapshots()));
    }

    /**
     * Generate the MAQL for new columns 
     * @param c command
     * @param p cli parameters
     * @param ctx current context
     * @throws IOException IO issue
     */
    private void generateUpdateMaql(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        l.debug("Updating MAQL.");
    	//final String configFile = c.getParamMandatory( "configFile");
    	//final SourceSchema schema = SourceSchema.createSchema(new File(configFile));
        Connector cc = ctx.getConnectorMandatory();
        SourceSchema schema = cc.getSchema();

    	final String pid = ctx.getProjectIdMandatory();
    	final String maqlFile = c.getParamMandatory( "maqlFile");
    	final String dataset = schema.getDatasetName();

        List<DLIPart> parts = ctx.getRestApi(p).getDLIParts(dataset, pid);

        final List<SourceColumn> newColumns = findNewAttributes(parts, schema);
        if (!newColumns.isEmpty()) {
        	final String maql = cc.generateMaql(newColumns);
        	FileUtil.writeStringToFile(maql, maqlFile);
        }
        l.debug("MAQL update finished.");
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
        String pid = ctx.getProjectIdMandatory();
        if(pid != null && pid.length() > 0)
            this.getConnectorBackend().setProjectId(pid);
        else
            throw new InvalidCommandException("No project is active. Please activate project via CreateProject or " +
                    "OpenProject command. ");
    }

}
