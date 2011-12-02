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
import com.gooddata.chargify.ChargifyWrapper;
import com.gooddata.transform.Transformer;
import com.gooddata.util.CSVReader;
import com.gooddata.util.CSVWriter;
import com.gooddata.util.FileUtil;
import com.gooddata.util.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.jaxen.JaxenException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * GoodData Chargify Connector
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class ChargifyConnector extends AbstractConnector implements Connector {

    private static Logger l = Logger.getLogger(ChargifyConnector.class);

    // Chargify API token
    private String apiToken;
    //  fields
    private String fields;
    // entity
    private String entity;
    // Sugar CRM hostname
    private String domain;


    /**
     * Creates a new Chargify connector
     */
    protected ChargifyConnector() {
        super();
    }

   /**
     * Creates a new Chargify connector
     * @return a new instance of the Chargify connector
     */
    public static ChargifyConnector createConnector() {
        return new ChargifyConnector();
    }


    /**
     * {@inheritDoc}
     */
    public void extract(String file, boolean transform) throws IOException {
        l.debug("Extracting Chargify data.");
        try {
            ChargifyWrapper m = new ChargifyWrapper(getDomain(), getApiToken());
            l.debug("Executing Chargify query entity: "+getEntity()+" fields: "+getFields());
            if(fields != null && fields.length() > 0) {
                String[] fs = fields.split(",");
                for(int i=0; i<fs.length; i++)
                    fs[i] = fs[i].trim();
                File dt = FileUtil.getTempFile();
                m.getAllData(getEntity(), fs, dt.getAbsolutePath());
                int rowCnt = copyAndTransform(FileUtil.createUtf8CsvReader(dt), FileUtil.createUtf8CsvWriter(new File(file)), transform, 10);
                l.info("Finished Chargify query execution. Retrieved "+rowCnt+" rows of data.");
            }
            else {
                throw new InvalidParameterException("The Chargify fields parameter must contain the comma separated list " +
                        "of the entity fields.");
            }
        }
        catch (SAXException e) {
            throw new IOException(e);
        }
        catch (XPathExpressionException e) {
            throw new IOException(e);
        }
        catch (ParserConfigurationException e) {
            throw new IOException(e);
        }
    }


    /**
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param d the domain to set
     */
    public void setDomain(String d) {
        this.domain = d;
    }

    /**
     * {@inheritDoc}
     */
    public boolean processCommand(Command c, CliParams cli, ProcessingContext ctx) throws ProcessingException {
        l.debug("Processing command "+c.getCommand());
        try {
            if(c.match("LoadChargify") || c.match("UseChargify")) {
                loadChargify(c, cli, ctx);
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
     * Loads Chargify data command processor
     * @param c command
     * @param p command line arguments
     * @param ctx current processing context
     * @throws java.io.IOException in case of IO issues
     */
    private void loadChargify(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        String configFile = c.getParamMandatory("configFile");
        String apiKey = c.getParamMandatory( "apiKey");
        String e = c.getParamMandatory("entity");
        String f = c.getParamMandatory("fields");
        String domain = c.getParamMandatory("domain");
        c.paramsProcessed();

        File conf = FileUtil.getFile(configFile);
        initSchema(conf.getAbsolutePath());
        setEntity(e);
        setFields(f);
        setApiToken(apiKey);
        setDomain(domain);
        ctx.setConnector(this);
        setProjectId(ctx);
        l.info("Chargify Connector successfully loaded (entity: " + e + " fields: "+StringUtil.previewString(f, 256)+").");
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

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }
}
