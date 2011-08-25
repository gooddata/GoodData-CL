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

import com.gooddata.exception.InvalidParameterException;
import com.gooddata.exception.ProcessingException;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.processor.CliParams;
import com.gooddata.processor.Command;
import com.gooddata.processor.ProcessingContext;
import com.gooddata.sugar.SugarCrmWrapper;
import com.gooddata.transform.Transformer;
import com.gooddata.util.CSVReader;
import com.gooddata.util.CSVWriter;
import com.gooddata.util.FileUtil;
import com.gooddata.util.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.jaxen.JaxenException;

import javax.xml.soap.SOAPException;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * GoodData SugarCrm Connector
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class SugarCrmConnector extends AbstractConnector implements Connector {

    private static Logger l = Logger.getLogger(SugarCrmConnector.class);

    // Sugar CRM username
    private String username;
    // Sugar CRM password
    private String password;
    //  fields
    private String fields;
    // entity
    private String entity;
    // Sugar CRM hostname
    private String hostname;


    /**
     * Creates a new SugarCrm connector
     */
    protected SugarCrmConnector() {
        super();
    }

   /**
     * Creates a new SugarCrm connector
     * @return a new instance of the SugarCrm connector
     */
    public static SugarCrmConnector createConnector() {
        return new SugarCrmConnector();
    }

    protected DecimalFormat nf = new DecimalFormat("###.00");

    /**
     * {@inheritDoc}
     */
    public void extract(String file, boolean transform) throws IOException {
        l.debug("Extracting SugarCrm data.");
        try {
            SugarCrmWrapper m = new SugarCrmWrapper(getHostname(), getUsername(), getPassword());
            m.connect();
            l.debug("Executing SugarCrm query entity: "+getEntity()+" fields: "+getFields());
            if(fields != null && fields.length() > 0) {
                String[] fs = fields.split(",");
                List<String> cfs = new ArrayList<String>();
                List<String> lfs = new ArrayList<String>();
                for(int i=0; i<fs.length; i++) {
                    if(fs[i].contains("."))
                        lfs.add(fs[i].trim());
                    else
                        cfs.add(fs[i].trim());
                }
                File dt = FileUtil.getTempFile();
                m.getAllEntries(getEntity(), cfs.toArray(new String[]{}), lfs.toArray(new String[]{}), "", dt.getAbsolutePath());
                int rowCnt = copyAndTransform(FileUtil.createUtf8CsvReader(dt), FileUtil.createUtf8CsvWriter(new File(file)), transform, 10);
                l.info("Finished SugarCrm query execution. Retrieved "+rowCnt+" rows of data.");
            }
            else {
                throw new InvalidParameterException("The SugarCrm fields parameter must contain the comma separated list " +
                        "of the entity fields.");
            }
        }
        catch (SOAPException e) {
            throw new IOException(e);
        }
        catch (JaxenException e) {
            throw new IOException(e);
        }
    }



   /**
     * SugarCrm username getter
     * @return SugarCrm username
     */
    public String getUsername() {
        return username;
    }

    /**
     * SugarCrm username setter
     * @param username SugarCrm username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * SugarCrm password getter
     * @return SugarCrm password
     */
    public String getPassword() {
        return password;
    }

    /**
     * SugarCrm password setter
     * @param password SugarCrm password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * {@inheritDoc}
     */
    public boolean processCommand(Command c, CliParams cli, ProcessingContext ctx) throws ProcessingException {
        l.debug("Processing command "+c.getCommand());
        try {
            if(c.match("LoadSugarCrm") || c.match("UseSugarCrm")) {
                loadSugarCrm(c, cli, ctx);
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
     * Loads SugarCrm data command processor
     * @param c command
     * @param p command line arguments
     * @param ctx current processing context
     * @throws java.io.IOException in case of IO issues
     */
    private void loadSugarCrm(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        String configFile = c.getParamMandatory("configFile");
        String usr = c.getParamMandatory( "username");
        String psw = c.getParamMandatory( "password");
        String e = c.getParamMandatory("entity");
        String f = c.getParamMandatory("fields");
        String host = c.getParamMandatory("host");
        c.paramsProcessed();

        File conf = FileUtil.getFile(configFile);
        initSchema(conf.getAbsolutePath());
        setUsername(usr);
        setPassword(psw);
        setEntity(e);
        setFields(f);
        setHostname(host);
        ctx.setConnector(this);
        setProjectId(ctx);
        l.info("Sugar CRM Connector successfully loaded (entity: " + e + "fields: "+StringUtil.previewString(f, 256)+").");
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}
