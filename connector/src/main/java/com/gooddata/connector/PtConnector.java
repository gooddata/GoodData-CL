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

import com.gooddata.exception.*;
import com.gooddata.integration.model.Column;
import com.gooddata.integration.model.SLI;
import com.gooddata.modeling.generator.MaqlGenerator;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.pivotal.PivotalApi;
import com.gooddata.processor.CliParams;
import com.gooddata.processor.Command;
import com.gooddata.processor.ProcessingContext;
import com.gooddata.transform.Transformer;
import com.gooddata.util.CSVReader;
import com.gooddata.util.CSVWriter;
import com.gooddata.util.FileUtil;
import com.gooddata.util.StringUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GoodData Pivotal Tracker Connector
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class PtConnector extends AbstractConnector implements Connector {

    private static Logger l = Logger.getLogger(PtConnector.class);

    private SourceSchema labelSchema;
    private SourceSchema labelToStorySchema;
    private SourceSchema storySchema;

    private String username;
    private String password;
    private String pivotalProjectId;


    /**
     * Creates a new Google Analytics Connector
     */
    protected PtConnector() {
    }

     /**
      * Creates a new Google Analytics Connector
      * @return a new instance of the GA connector
      *
     */
    public static PtConnector createConnector() {
        return new PtConnector();
    }


    /**
     * Initializes schemas
     * @param labelConfig label config file
     * @param labelToStoryConfig  labelToStory config file
     * @param storyConfig  story config file
     */
    public void initSchema(String labelConfig, String labelToStoryConfig, String storyConfig)
        throws IOException {
        labelSchema = SourceSchema.createSchema(new File(labelConfig));
        expandDates(labelSchema);
        labelToStorySchema = SourceSchema.createSchema(new File(labelToStoryConfig));
        expandDates(labelToStorySchema);
        storySchema = SourceSchema.createSchema(new File(storyConfig));
        expandDates(storySchema);
    }

    /**
     * {@inheritDoc}
     */
    public void extract(String dir, boolean transform) {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    public void extract(SourceSchema schema, String inputFile, String dir)
            throws IOException {
        l.debug("Extracting Pivotal data.");
        Map<String, String> r = new HashMap<String, String>();

        File dataFile = new File(dir + System.getProperty("file.separator") + "data.csv");
        l.debug("Extracting PT data to file=" + dataFile.getAbsolutePath());
        CSVWriter cw = FileUtil.createUtf8CsvEscapingWriter(dataFile);
        CSVReader cr = FileUtil.createUtf8CsvReader(new File(inputFile));
        // skip header
        cr.readNext();
        Transformer t = Transformer.create(schema);
        String[] header = t.getHeader(true);
        cw.writeNext(header);
        String[] row = cr.readNext();
        int rowCnt = 0;
        while (row != null) {
                rowCnt++;
                if(row.length == 1 && row[0].length() == 0) {
                    row = cr.readNext();
                    continue;
                }
                row = t.transformRow(row, 10);
                cw.writeNext(row);
                cw.flush();
                row = cr.readNext();
        }
        cw.close();
        cr.close();
        l.debug("Extracted "+rowCnt+" rows of Pivotal data.");
    }


    private void transfer(SourceSchema schema, String inputFile, Command c, String pid,
        boolean waitForFinish, CliParams p, ProcessingContext ctx) throws IOException, InterruptedException {
        File tmpDir = FileUtil.createTempDir();
        File tmpZipDir = FileUtil.createTempDir();
        String archiveName = tmpDir.getName();
        MDC.put("GdcDataPackageDir",archiveName);
        String archivePath = tmpZipDir.getAbsolutePath() + System.getProperty("file.separator") +
            archiveName + ".zip";

        // get information about the data loading package
        String ssn = schema.getName();
        SLI sli = ctx.getRestApi(p).getSLIById("dataset." + ssn, pid);
        List<Column> sliColumns = ctx.getRestApi(p).getSLIColumns(sli.getUri());
        List<Column> columns = populateColumnsFromSchema(schema);
        if(sliColumns.size() > columns.size())
            throw new InvalidParameterException("The GoodData data loading interface (SLI) expects more columns.");
        String incremental = c.getParam("incremental");
        c.paramsProcessed();

        if(incremental != null && incremental.length() > 0 &&
                incremental.equalsIgnoreCase("true")) {
            l.debug("Using incremental mode.");
            setIncremental(columns);
        }

        // extract the data to the CSV that is going to be transferred to the server
        extract(schema, inputFile ,tmpDir.getAbsolutePath());
        this.deploy(sli, columns, tmpDir.getAbsolutePath(), archivePath);
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
        MDC.remove("GdcDataPackageDir");
    }

    /**
     * {@inheritDoc}
     */

    public void extractAndTransfer(Command c, String pid, Connector cc,  boolean waitForFinish, CliParams p, ProcessingContext ctx)
    	throws IOException, InterruptedException {
        l.debug("Extracting data.");

        File mainDir = FileUtil.createTempDir();

        PivotalApi papi = new PivotalApi(getUsername(), getPassword(), getPivotalProjectId());
        papi.signin();
        File ptf = FileUtil.getTempFile();
        papi.getCsvData(ptf.getAbsolutePath());
        String sp = mainDir.getAbsolutePath() + System.getProperty("file.separator") + "stories.csv";
        String lp = mainDir.getAbsolutePath() + System.getProperty("file.separator") + "labels.csv";
        String ltsp = mainDir.getAbsolutePath() + System.getProperty("file.separator") + "labelsToStories.csv";
        papi.parse(ptf.getAbsolutePath(), sp, lp, ltsp, new DateTime(),3);

        transfer(getStorySchema(), sp, c, pid, waitForFinish, p, ctx);
        transfer(getLabelSchema(), lp, c, pid, waitForFinish, p, ctx);
        transfer(getLabelToStorySchema(), ltsp, c, pid, waitForFinish, p, ctx);
        
        //cleanup
        l.debug("Cleaning the temporary files.");
        FileUtil.recursiveDelete(mainDir);

    }

    /**
     * {@inheritDoc}
     */
    public boolean processCommand(Command c, CliParams cli, ProcessingContext ctx) throws ProcessingException {
        l.debug("Processing command "+c.getCommand());
        try {
            if(c.match("LoadPivotalTracker") || c.match("UsePivotalTracker")) {
                loadPivotalTracker(c, cli, ctx);
            }
            else {
                l.debug("No match passing the command "+c.getCommand()+" further.");
                return super.processCommand(c, cli, ctx);
            }
        }
        catch (IOException e) {
            throw new ProcessingException(e);
        }
        l.debug("Processed command "+c.getCommand());
        return true;
    }

    public String generateMaqlCreate() {
        StringBuilder sb = new StringBuilder();
    	MaqlGenerator mg = new MaqlGenerator(storySchema);
        sb.append(mg.generateMaqlCreate());
        mg = new MaqlGenerator(labelSchema);
        sb.append(mg.generateMaqlCreate());
        mg = new MaqlGenerator(labelToStorySchema);
        sb.append(mg.generateMaqlCreate());
        return sb.toString();
    }


    /**
     * Downloads the PT data
     * @param c command
     * @param p command line arguments
     * @param ctx current processing context
     * @throws java.io.IOException in case of IO issues
     */
    private void loadPivotalTracker(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        setUsername(c.getParamMandatory("username"));
        setPassword(c.getParamMandatory("password"));
        setPivotalProjectId(c.getParamMandatory("pivotalProjectId"));
        String lc = c.getParamMandatory("labelConfigFile");
        String lsc = c.getParamMandatory("labelToStoryConfigFile");
        String sc = c.getParamMandatory("storyConfigFile");
        c.paramsProcessed();

        File lcf = new File(lc);
        File lscf = new File(lsc);
        File scf = new File(sc);

        initSchema(lcf.getAbsolutePath(),lscf.getAbsolutePath(), scf.getAbsolutePath());

        // sets the current connector
        ctx.setConnector(this);
        setProjectId(ctx);
        
        l.info("Pivotal Tracker Connector Loaded (id: " + getPivotalProjectId() + ")");
    }

    public SourceSchema getLabelSchema() {
        return labelSchema;
    }

    public void setLabelSchema(SourceSchema labelSchema) {
        this.labelSchema = labelSchema;
    }

    public SourceSchema getLabelToStorySchema() {
        return labelToStorySchema;
    }

    public void setLabelToStorySchema(SourceSchema labelToStorySchema) {
        this.labelToStorySchema = labelToStorySchema;
    }

    public SourceSchema getStorySchema() {
        return storySchema;
    }

    public void setStorySchema(SourceSchema storySchema) {
        this.storySchema = storySchema;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPivotalProjectId() {
        return pivotalProjectId;
    }

    public void setPivotalProjectId(String pivotalProjectId) {
        this.pivotalProjectId = pivotalProjectId;
    }
}
