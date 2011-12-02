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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gooddata.transform.Transformer;
import com.sforce.soap.partner.*;
import org.apache.axis.message.MessageElement;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.gooddata.util.CSVWriter;

import com.gooddata.exception.InternalErrorException;
import com.gooddata.exception.ProcessingException;
import com.gooddata.exception.SfdcException;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.processor.CliParams;
import com.gooddata.processor.Command;
import com.gooddata.processor.ProcessingContext;
import com.gooddata.util.FileUtil;
import com.gooddata.util.StringUtil;
import com.sforce.soap.partner.fault.ApiQueryFault;
import com.sforce.soap.partner.fault.ExceptionCode;
import com.sforce.soap.partner.fault.InvalidIdFault;
import com.sforce.soap.partner.fault.InvalidQueryLocatorFault;
import com.sforce.soap.partner.fault.LoginFault;
import com.sforce.soap.partner.fault.UnexpectedErrorFault;
import com.sforce.soap.partner.sobject.SObject;

/**
 * GoodData SFDC Connector
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class SfdcConnector extends AbstractConnector implements Connector {

    private static Logger l = Logger.getLogger(SfdcConnector.class);

    private String sfdcUsername;
    private String sfdcPassword;
    private String sfdcQuery;
    private String sfdcToken;
    private String sfdcHostname = "www.salesforce.com";
    private String clientID;


    /**
     * Creates a new SFDC connector
     */
    protected SfdcConnector() {
        super();
    }

   /**
     * Creates a new SFDC connector
     * @return a new instance of the SFDC connector
     */
    public static SfdcConnector createConnector() {
        return new SfdcConnector();
    }

    /**
     * Executes the SFDC query, returns one row only. This is useful for metadata inspection purposes
     * @param binding SFDC stub
     * @param sfdcQuery SFDC SOOL query
     * @param clientID SFDC partner client ID
     * @return results as List of SObjects
     * @throws SfdcException in case of SFDC communication errors
     */
    protected static SObject executeQueryFirstRow(SoapBindingStub binding, String sfdcQuery, String clientID) throws SfdcException {
        l.debug("Executing SFDC query "+sfdcQuery);
        List<SObject> result = new ArrayList<SObject>();
        QueryOptions qo = new QueryOptions();
        qo.setBatchSize(1);
        binding.setHeader(new SforceServiceLocator().getServiceName().getNamespaceURI(),
             "QueryOptions", qo);
        try {
            QueryResult qr = binding.query(sfdcQuery);
            if(qr.getSize()>0) {
                SObject[] sObjects = qr.getRecords();
                result.addAll(Arrays.asList(sObjects));
            }
        }
        catch (ApiQueryFault ex) {
            l.debug("Executing SFDC query failed",ex);
            throw new SfdcException("Failed to execute SFDC query.",ex);
        }
        catch (UnexpectedErrorFault e) {
            l.debug("Executing SFDC query failed",e);
            throw new SfdcException("Failed to execute SFDC query.",e);
        }
        catch (InvalidIdFault e) {
            l.debug("Executing SFDC query failed",e);
            throw new SfdcException("Failed to execute SFDC query.",e);
        }
        catch (InvalidQueryLocatorFault e) {
            l.debug("Executing SFDC query failed",e);
            throw new SfdcException("Failed to execute SFDC query.",e);
        }
        catch (RemoteException e) {
            l.debug("Executing SFDC query failed",e);
            throw new SfdcException("Failed to execute SFDC query.",e);
        }
        l.debug("Finihed SFDC query execution.");
        if(result.size()>0)
            return result.get(0);
        else
            return null;
    }

    /**
     * Retrieves the object's metadata
     * @param c SFDC stub
     * @param name SFDC object name
     * @return Map of fields
     * @throws RemoteException communication error
     */
    protected static Map<String, Field> describeObject(SoapBindingStub c, String name) throws RemoteException {
        l.debug("Retrieving SFDC object "+name+" metadata.");
        Map<String,Field> result = new HashMap<String,Field>();
        DescribeSObjectResult describeSObjectResult = c.describeSObject(name);
        if (! (describeSObjectResult == null)) {
            Field[] fields = describeSObjectResult.getFields();
            if (fields != null) {
                for(Field field: fields) {
                    result.put(field.getName(), field);
                }
            }
        }
        l.debug("SFDC object "+name+" metadata retrieved.");
        return result;
    }


    /**
     * Saves a template of the config file
     * @param name new schema name
     * @param configFileName config file name
     * @param sfdcUsr SFDC username
     * @param sfdcPsw SFDC password
     * @param sfdcToken SFDC security token
     * @param query SFDC query
     * @param partnerId SFDC partner ID
     * @throws IOException if there is a problem with writing the config file
     */
    public static void saveConfigTemplate(String name, String configFileName, String sfdcHostname, String sfdcUsr, String sfdcPsw, String sfdcToken, String partnerId,
                                  String query)
            throws IOException {
        l.debug("Saving SFDC config template.");
        SourceSchema s = SourceSchema.createSchema(name);
        SoapBindingStub c = connect(sfdcHostname, sfdcUsr, sfdcPsw, sfdcToken, partnerId);
        SObject result = executeQueryFirstRow(c, query, partnerId);
        if(result != null) {
            Map<String,Field> fields = describeObject(c, result.getType());
            for(MessageElement column : result.get_any()) {
                String nm = column.getName();
                String tp = getColumnType(fields, nm);
                if(tp.equals(SourceColumn.LDM_TYPE_DATE)) {
                    SourceColumn sc = new SourceColumn(nm, tp, nm, name);
                    sc.setFormat("yyyy-MM-dd");
                    s.addColumn(sc);
                }
                else {
                    SourceColumn sc = new SourceColumn(nm, tp, nm, name);
                    s.addColumn(sc);
                }
            }
        }
        else {
            l.debug("The SFDC query hasn't returned any row.");
            throw new SfdcException("The SFDC query hasn't returned any row.");
        }
        s.writeConfig(new File(configFileName));
        l.debug("Saved SFDC config template.");
    }

    /**
     * Derives the LDM type from the SFDC type
     * @param fields SFDC object metadata
     * @param fieldName the field name
     * @return LDM type
     */
    protected static String getColumnType(Map<String,Field> fields, String fieldName) {
        String type = SourceColumn.LDM_TYPE_ATTRIBUTE;
        Field f = fields.get(fieldName);
        if(f != null) {
            FieldType t = f.getType();
            if(t.getValue().equalsIgnoreCase("id"))
                type = SourceColumn.LDM_TYPE_CONNECTION_POINT;
            else if(t.getValue().equalsIgnoreCase("string"))
                type = SourceColumn.LDM_TYPE_ATTRIBUTE;
            else if(t.getValue().equalsIgnoreCase("currency"))
                type = SourceColumn.LDM_TYPE_FACT;
            else if(t.getValue().equalsIgnoreCase("boolean"))
                type = SourceColumn.LDM_TYPE_ATTRIBUTE;
            else if(t.getValue().equalsIgnoreCase("reference"))
                type = SourceColumn.LDM_TYPE_REFERENCE;
            else if(t.getValue().equalsIgnoreCase("date"))
                type = SourceColumn.LDM_TYPE_DATE;
            else if(t.getValue().equalsIgnoreCase("datetime"))
                type = SourceColumn.LDM_TYPE_DATE;
        }
        return type;
    }

    /**
     * {@inheritDoc}
     */
    public void extract(String file, boolean transform) throws IOException {
        File dataFile = new File(file);
        l.debug("Extracting SFDC data to file="+dataFile.getAbsolutePath());
        CSVWriter cw = FileUtil.createUtf8CsvEscapingWriter(dataFile);
        Transformer t = Transformer.create(schema);
        String[] header = t.getHeader(transform);
        cw.writeNext(header);
        SoapBindingStub c = connect(getSfdcHostname(), getSfdcUsername(), getSfdcPassword(), getSfdcToken(), getClientID());
        l.debug("Executing SFDC query "+sfdcQuery);
        QueryOptions qo = new QueryOptions();
        qo.setBatchSize(500);
        c.setHeader(new SforceServiceLocator().getServiceName().getNamespaceURI(),"QueryOptions", qo);
        String[] colTypes = null;
        boolean firstBatch = true;
        int rowCnt = 0;
        try {
            QueryResult qr = c.query(sfdcQuery);
            boolean isdone = false;
            do {
                SObject[] sObjects = qr.getRecords();
                if(sObjects != null && sObjects.length >0) {
                    l.debug("Started retrieving SFDC data.");
                    boolean firstRow = true;
                    for( SObject srow : sObjects) {
                        if(firstBatch && firstRow) {
                            SObject hdr = sObjects[0];
                            Map<String,Field> fields = describeObject(c, hdr.getType());
                            MessageElement[] frCols = hdr.get_any();
                            colTypes = new String[frCols.length];
                            for(int i=0; i< frCols. length; i++) {
                                String nm = frCols[i].getName();
                                colTypes[i] = getColumnType(fields, nm);
                            }
                            firstRow = false;
                        }
                        MessageElement[] cols = srow.get_any();
                        Object[] row = new Object[cols.length];
                        for(int i=0; i<row.length; i++) {
                            if (colTypes[i].equalsIgnoreCase(SourceColumn.LDM_TYPE_DATE)) {
                                row[i] = new DateTime(cols[i].getValue());
                            } else {
                                row[i] = cols[i].getValue();
                            }
                        }
                        String[] nrow = null;
                        if(transform) {
                            nrow = t.transformRow(row, DATE_LENGTH_UNRESTRICTED);
                        }
                        else {
                            nrow = new String[row.length];
                            for(int i = 0; i<row.length; i++) {
                                nrow[i] = row[i].toString();
                            }
                        }
                        cw.writeNext(nrow);
                        cw.flush();
                        rowCnt++;
                    }
                    isdone = qr.isDone();
                    if(!isdone) {
                        qr = c.queryMore(qr.getQueryLocator());
                        firstBatch = false;
                    }
                }
            } while(!isdone);
            l.debug("Retrieved " + rowCnt + " rows of SFDC data.");
            cw.close();

        }
        catch (ApiQueryFault ex) {
            l.debug("Executing SFDC query failed",ex);
            throw new SfdcException("Failed to execute SFDC query.",ex);
        }
        catch (UnexpectedErrorFault e) {
            l.debug("Executing SFDC query failed",e);
            throw new SfdcException("Failed to execute SFDC query.",e);
        }
        catch (InvalidIdFault e) {
            l.debug("Executing SFDC query failed",e);
            throw new SfdcException("Failed to execute SFDC query.",e);
        }
        catch (InvalidQueryLocatorFault e) {
            l.debug("Executing SFDC query failed",e);
            throw new SfdcException("Failed to execute SFDC query.",e);
        }
        catch (RemoteException e) {
            l.debug("Executing SFDC query failed",e);
            throw new SfdcException("Failed to execute SFDC query.",e);
        }
        l.debug("Finihed SFDC query execution.");


    }



    /**
     * Connect the SFDC
     * @param usr SFDC username
     * @param psw SFDC pasword
     * @param token SFDC security token
     * @return SFDC stub
     * @throws SfdcException in case of connection issues
     */
    protected static SoapBindingStub connect(String host, String usr, String psw, String token, String clientID) throws SfdcException {
        SoapBindingStub binding;
        LoginResult loginResult;
        if (token != null) {
            psw += token;
        }
        try {
            SforceServiceLocator loc = new SforceServiceLocator();
            loc.setSoapEndpointAddress(loc.getSoapAddress().replaceAll("www.salesforce.com", host).replaceAll("login.salesforce.com", host));
            binding = (SoapBindingStub) loc.getSoap();
            l.debug("Connecting to SFDC.");
            // Time out after a minute
            binding.setTimeout(60000);
            // Test operation
            if(clientID != null && clientID.length()>0) {
                CallOptions co = new CallOptions();
                co.setClient(clientID);
                binding.setHeader(new SforceServiceLocator().getServiceName().getNamespaceURI(), "CallOptions", co);
            }
            loginResult = binding.login(usr, psw);
        }
        catch (LoginFault ex) {
            // The LoginFault derives from AxisFault
            ExceptionCode exCode = ex.getExceptionCode();
            if(exCode == ExceptionCode.FUNCTIONALITY_NOT_ENABLED) {
                l.debug("Error logging into the SFDC. Functionality not enabled.", ex);
                throw new SfdcException("Error logging into the SFDC. Functionality not enabled.", ex);
            }
            else if(exCode == ExceptionCode.INVALID_CLIENT) {
                l.debug("Error logging into the SFDC. Invalid client.", ex);
                throw new SfdcException("Error logging into the SFDC. Invalid client.", ex);
            }
            else if(exCode == ExceptionCode.INVALID_LOGIN) {
                l.debug("Error logging into the SFDC. Invalid login.", ex);
                throw new SfdcException("Error logging into the SFDC. Invalid login.", ex);
            }
            else if(exCode == ExceptionCode.LOGIN_DURING_RESTRICTED_DOMAIN) {
                l.debug("Error logging into the SFDC. Restricred domain.", ex);
                throw new SfdcException("Error logging into the SFDC. Restricred domain.", ex);
            }
            else if(exCode == ExceptionCode.LOGIN_DURING_RESTRICTED_TIME) {
                l.debug("Error logging into the SFDC. Restricred during time.", ex);
                throw new SfdcException("Error logging into the SFDC. Restricred during time.", ex);
            }
            else if(exCode == ExceptionCode.ORG_LOCKED) {
                l.debug("Error logging into the SFDC. Organization locked.", ex);
                throw new SfdcException("Error logging into the SFDC. Organization locked.", ex);
            }
            else if(exCode == ExceptionCode.PASSWORD_LOCKOUT) {
                l.debug("Error logging into the SFDC. Password lock-out.", ex);
                throw new SfdcException("Error logging into the SFDC. Password lock-out.", ex);
            }
            else if(exCode == ExceptionCode.SERVER_UNAVAILABLE) {
                l.debug("Error logging into the SFDC. Server not available.", ex);
                throw new SfdcException("Error logging into the SFDC. Server not available.", ex);
            }
            else if(exCode == ExceptionCode.TRIAL_EXPIRED) {
                l.debug("Error logging into the SFDC. Trial expired.", ex);
                throw new SfdcException("Error logging into the SFDC. Trial expired.", ex);
            }
            else if(exCode == ExceptionCode.UNSUPPORTED_CLIENT) {
                l.debug("Error logging into the SFDC. Unsupported client.", ex);
                throw new SfdcException("Error logging into the SFDC. Unsupported client.", ex);
            }
            else {
                l.debug("Error logging into the SFDC.", ex);
                throw new SfdcException("Error logging into the SFDC.", ex);
            }
        } catch (Exception ex) {
            l.debug("Error logging into the SFDC.", ex);
            throw new SfdcException("Error logging into the SFDC.", ex);
        }
        // Check if the password has expired
        if (loginResult.isPasswordExpired()) {
            l.debug("An error has occurred. Your password has expired.");
            throw new SfdcException("An error has occurred. Your password has expired.");
        }
        /** Once the client application has logged in successfully, it will use
         *  the results of the login call to reset the endpoint of the service
         *  to the virtual server instance that is servicing your organization.
         *  To do this, the client application sets the ENDPOINT_ADDRESS_PROPERTY
         *  of the binding object using the URL returned from the LoginResult.
         */
        binding._setProperty(SoapBindingStub.ENDPOINT_ADDRESS_PROPERTY,
            loginResult.getServerUrl());
        /** The sample client application now has an instance of the SoapBindingStub
         *  that is pointing to the correct endpoint. Next, the sample client application
         *  sets a persistent SOAP header (to be included on all subsequent calls that
         *  are made with the SoapBindingStub) that contains the valid sessionId
         *  for our login credentials. To do this, the sample client application
         *  creates a new SessionHeader object and set its sessionId property to the
         *  sessionId property from the LoginResult object.
         */
        // Create a new session header object and add the session id
        // from the login return object
        SessionHeader sh = new SessionHeader();
        sh.setSessionId(loginResult.getSessionId());
        /** Next, the sample client application calls the setHeader method of the
         *  SoapBindingStub to add the header to all subsequent method calls. This
         *  header will persist until the SoapBindingStub is destroyed until the header
         *  is explicitly removed. The "SessionHeader" parameter is the name of the
         *  header to be added.
         */
        // set the session header for subsequent call authentication
        binding.setHeader(new SforceServiceLocator().getServiceName().getNamespaceURI(),
                          "SessionHeader", sh);
        l.debug("Connected to SFDC.");
        return binding;
    }

    /**
     * SFDC username getter
     * @return SFDC username
     */
    public String getSfdcUsername() {
        return sfdcUsername;
    }

    /**
     * SFDC username setter
     * @param sfdcUsername SFDC username
     */
    public void setSfdcUsername(String sfdcUsername) {
        this.sfdcUsername = sfdcUsername;
    }

    /**
     * SFDC password getter
     * @return SFDC password
     */
    public String getSfdcPassword() {
        return sfdcPassword;
    }

    /**
     * SFDC password setter
     * @param sfdcPassword SFDC password
     */
    public void setSfdcPassword(String sfdcPassword) {
        this.sfdcPassword = sfdcPassword;
    }

    /**
     * SFDC query getter
     * @return SFDC query
     */
    public String getSfdcQuery() {
        return sfdcQuery;
    }

    /**
     * SFDC query setter
     * @param sfdcQuery SFDC query
     */
    public void setSfdcQuery(String sfdcQuery) {
        this.sfdcQuery = sfdcQuery;
    }
    
    /**
     * SFDC security token getter
     * @return SFDC security token
     */
    public String getSfdcToken() {
        return sfdcToken;
    }

    /**
     * SFDC security token setter
     * @param sfdcToken SFDC security token
     */
    public void setSfdcToken(String sfdcToken) {
        this.sfdcToken = sfdcToken;
    }

    /**
     * @return the sfdcHostname
     */
    public String getSfdcHostname() {
        return sfdcHostname;
    }

    /**
     * @param sfdcHostname the sfdcHostname to set
     */
    public void setSfdcHostname(String sfdcHostname) {
        this.sfdcHostname = sfdcHostname;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    /**
     * {@inheritDoc}
     */
    public boolean processCommand(Command c, CliParams cli, ProcessingContext ctx) throws ProcessingException {
        l.debug("Processing command "+c.getCommand());
        try {
            if(c.match("GenerateSfdcConfig")) {
                generateSfdcConfig(c, cli, ctx);
            }
            else if(c.match("LoadSfdc") || c.match("UseSfdc")) {
                loadSfdc(c, cli, ctx);
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
     * Loads SFDC data command processor
     * @param c command
     * @param p command line arguments
     * @param ctx current processing context
     * @throws IOException in case of IO issues
     */
    private void loadSfdc(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        String configFile = c.getParamMandatory("configFile");
        String usr = c.getParamMandatory( "username");
        String psw = c.getParamMandatory( "password");
        String q = c.getParamMandatory("query");
        String t = c.getParam("token");
        String host = c.getParam("host");
        String partnerId = c.getParam("partnerId");
        c.paramsProcessed();

        File conf = FileUtil.getFile(configFile);
        initSchema(conf.getAbsolutePath());
        setSfdcUsername(usr);
        setSfdcPassword(psw);
        setSfdcToken(t);
        setSfdcQuery(q);
        setClientID(partnerId);
        if (host != null && !"".equals(host)) {
            setSfdcHostname(host);
        }
        // sets the current connector
        ctx.setConnector(this);
        setProjectId(ctx);
        l.info("SFDC Connector successfully loaded (query: " + StringUtil.previewString(q, 256) + ").");
    }

    /**
     * Generates the SFDC config
     * @param c command
     * @param p command line arguments
     * @param ctx current processing context
     * @throws IOException in case of IO issues
     */
    private void generateSfdcConfig(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        String configFile = c.getParamMandatory("configFile");
        String name = c.getParamMandatory("name");
        String usr = c.getParamMandatory( "username");
        String psw = c.getParamMandatory( "password");
        String token = c.getParam("token");
        String query = c.getParamMandatory("query");
        String host = c.getParam("host");
        if (host == null || "".equals(host)) {
            host = sfdcHostname;
        }
        String partnerId = c.getParam("partnerId");
        c.paramsProcessed();

        SfdcConnector.saveConfigTemplate(name, configFile, host, usr, psw, token, partnerId, query);
        l.info("SFDC Connector configuration successfully generated. See config file: "+configFile);
    }
}
