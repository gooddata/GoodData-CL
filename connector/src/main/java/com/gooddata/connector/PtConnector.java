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
import com.gooddata.google.analytics.FeedDumper;
import com.gooddata.google.analytics.GaQuery;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.pivotal.PivotalApi;
import com.gooddata.processor.CliParams;
import com.gooddata.processor.Command;
import com.gooddata.processor.ProcessingContext;
import com.gooddata.util.CSVWriter;
import com.gooddata.util.FileUtil;
import com.google.gdata.client.ClientLoginAccountType;
import com.google.gdata.client.analytics.AnalyticsService;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

/**
 * GoodData Pivotal Tracker Connector
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class PtConnector extends AbstractConnector implements Connector {

    private static Logger l = Logger.getLogger(PtConnector.class);

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

    public void extract(String a) {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    public boolean processCommand(Command c, CliParams cli, ProcessingContext ctx) throws ProcessingException {
        l.debug("Processing command "+c.getCommand());
        try {
            if(c.match("DownloadPivotalTrackerData")) {
                downloadPivotalTrackerData(c, cli, ctx);
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

    /**
     * Downloads the PT data
     * @param c command
     * @param p command line arguments
     * @param ctx current processing context
     * @throws java.io.IOException in case of IO issues
     */
    private void downloadPivotalTrackerData(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        String usr = c.getParamMandatory("username");
        String psw = c.getParamMandatory("password");
        String id = c.getParamMandatory("projectId");
        String fl = c.getParamMandatory("dir");

        File dir = new File(fl);
        if(!dir.exists() || !dir.isDirectory()) {
            throw new InvalidParameterException("The dir parameter in the DownloadPivotalTrackerData command must be an existing directory.");
        }

        PivotalApi papi = new PivotalApi(usr, psw, id);
        papi.signin();
        //papi.getToken();
        File ptf = FileUtil.getTempFile();
        papi.getCsvData(ptf.getAbsolutePath());
        papi.parse(ptf.getAbsolutePath(),
                dir.getAbsolutePath() + System.getProperty("file.separator") + "stories.csv",
                dir.getAbsolutePath() + System.getProperty("file.separator") + "labels.csv",
                dir.getAbsolutePath() + System.getProperty("file.separator") + "labelsToStories.csv",
                dir.getAbsolutePath() + System.getProperty("file.separator") + "snapshots.csv", new DateTime());

        l.info("Pivotal Tracker data successfully downloaded (id: " + id + ") into " + dir.getAbsolutePath());
    }


}
