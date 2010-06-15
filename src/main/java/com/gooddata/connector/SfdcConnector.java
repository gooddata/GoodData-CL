package com.gooddata.connector;

import au.com.bytecode.opencsv.CSVWriter;
import com.gooddata.exception.*;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.exception.SfdcException;
import com.gooddata.util.FileUtil;
import com.gooddata.util.StringUtil;
import com.sforce.soap.partner.*;
import com.sforce.soap.partner.fault.*;
import com.sforce.soap.partner.sobject.SObject;
import org.apache.axis.message.MessageElement;
import org.apache.log4j.Logger;
import org.gooddata.connector.AbstractConnector;
import org.gooddata.connector.Connector;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

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

    /**
     * Creates a new SFDC connector
     * @param projectId project id
     * @param configFileName configuration file
     * @param sfdcUsr SFDC user
     * @param sfdcPsw SFDC password
     * @param query SFDC query
     * @param connectorBackend connector backend
     * @param username database backend username
     * @param password database backend password
     * @throws com.gooddata.exception.InitializationException
     * @throws com.gooddata.exception.MetadataFormatException
     * @throws java.io.IOException
     */
    protected SfdcConnector(String projectId, String configFileName, String sfdcUsr, String sfdcPsw, String query,
                            int connectorBackend, String username, String password)
            throws InitializationException,
            MetadataFormatException, IOException, ModelException {
        super(projectId, configFileName, connectorBackend, username, password);
        setSfdcUsername(sfdcUsr);
        setSfdcPassword(sfdcPsw);
        setSfdcQuery(query);
    }

   /**
     * Creates a new SFDC connector
     * @param projectId project id
     * @param configFileName configuration file
     * @param sfdcUsr SFDC user
     * @param sfdcPsw SFDC password
     * @param query SFDC query
     * @param connectorBackend connector backend
     * @param username database backend username
     * @param password database backend password
     * @throws com.gooddata.exception.InitializationException
     * @throws com.gooddata.exception.MetadataFormatException
     * @throws java.io.IOException
     */
    public static SfdcConnector createConnector(String projectId, String configFileName, String sfdcUsr, String sfdcPsw, String query,
                            int connectorBackend, String username, String password)
                                throws InitializationException, MetadataFormatException,
             IOException, ModelException {
        return new SfdcConnector(projectId, configFileName, sfdcUsr, sfdcPsw, query, connectorBackend,
                username, password);
    }


    private static List<SObject> executeQuery(SoapBindingStub binding, String sfdcQuery) throws SfdcException {
        List<SObject> result = new ArrayList<SObject>();
        QueryOptions qo = new QueryOptions();
        qo.setBatchSize(500);
        binding.setHeader(new SforceServiceLocator().getServiceName().getNamespaceURI(),
             "QueryOptions", qo);
        try {
            QueryResult qr = binding.query(sfdcQuery);
            do {
                SObject[] sObjects = qr.getRecords();
                result.addAll(Arrays.asList(sObjects));
                if(!qr.isDone()) {
                    qr = binding.queryMore(qr.getQueryLocator());
                }
            } while(!qr.isDone());
        }
        catch (ApiQueryFault ex) {
         throw new SfdcException("Failed to execute SFDC query: " + ex.getExceptionMessage());
        }
        catch (UnexpectedErrorFault e) {
    	 throw new SfdcException("Failed to execute SFDC query: " + e.getExceptionMessage());
	    }
        catch (InvalidIdFault e) {
	        throw new SfdcException("Failed to execute SFDC query: " + e.getExceptionMessage());
	    }
        catch (InvalidQueryLocatorFault e) {
		    throw new SfdcException("Failed to execute SFDC query: " + e.getExceptionMessage());
	    }
        catch (RemoteException e) {
		    throw new SfdcException("Failed to execute SFDC query: " + e.getMessage());
	    }
        return result;
    }

    private static SObject executeQueryFirstRow(SoapBindingStub binding, String sfdcQuery) throws SfdcException {
        List<SObject> result = new ArrayList<SObject>();
        QueryOptions qo = new QueryOptions();
        qo.setBatchSize(1);
        binding.setHeader(new SforceServiceLocator().getServiceName().getNamespaceURI(),
             "QueryOptions", qo);
        try {
            QueryResult qr = binding.query(sfdcQuery);
            while (qr.getSize()>0) {
                SObject[] sObjects = qr.getRecords();
                result.addAll(Arrays.asList(sObjects));
            }
        }
        catch (ApiQueryFault ex) {
         throw new SfdcException("Failed to execute SFDC query: " + ex.getExceptionMessage());
        }
        catch (UnexpectedErrorFault e) {
    	 throw new SfdcException("Failed to execute SFDC query: " + e.getExceptionMessage());
	    }
        catch (InvalidIdFault e) {
	        throw new SfdcException("Failed to execute SFDC query: " + e.getExceptionMessage());
	    }
        catch (InvalidQueryLocatorFault e) {
		    throw new SfdcException("Failed to execute SFDC query: " + e.getExceptionMessage());
	    }
        catch (RemoteException e) {
		    throw new SfdcException("Failed to execute SFDC query: " + e.getMessage());
	    }
        return result.get(0);
    }

    private static Map<String, Field> describeObject(SoapBindingStub c, String name) throws RemoteException, UnexpectedErrorFault {
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
        return result;
    }


    /**
     * Saves a template of the config file
     * @throws com.gooddata.exception.InvalidArgumentException if there is a problem with arguments
     * @throws java.io.IOException if there is a problem with writing the config file
     * @throws java.sql.SQLException if there is a problem with the db
     * @throws com.gooddata.exception.InvalidArgumentException
     */
    public static void saveConfigTemplate(String name, String configFileName, String sfdcUsr, String sfdcPsw,
                                  String query)
            throws InvalidArgumentException, IOException, SfdcException {
        SourceSchema s = SourceSchema.createSchema(name);
        SoapBindingStub c = null;
        try {
            c = connect(sfdcUsr, sfdcPsw);
        } catch (ServiceException e) {
            throw new SfdcException("Connection to SFDC failed: "+e.getMessage());
        }
        SObject result = executeQueryFirstRow(c, query);
        if(result != null) {
            Map<String,Field> fields = describeObject(c, result.getType());
            for(MessageElement column : result.get_any()) {
                String nm = column.getName();
                String tp = getColumnType(fields, nm);
                if(tp.equals(SourceColumn.LDM_TYPE_DATE)) {
                    SourceColumn sc = new SourceColumn(StringUtil.formatShortName(nm), tp, nm, name);
                    sc.setFormat("yyyy-MM-dd");
                    s.addColumn(sc);
                }
                else {
                    SourceColumn sc = new SourceColumn(StringUtil.formatShortName(nm), tp, nm, name);
                    s.addColumn(sc);
                }
            }
        }
        else
            throw new InvalidArgumentException("The SFDC query hasn't returned any row.");
        s.writeConfig(new File(configFileName));
    }

    private static String getColumnType(Map<String,Field> fields, String fieldName) {
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
     * Extracts the source data CSV to the Derby database where it is going to be transformed
     * @throws com.gooddata.exception.ModelException in case of PDM schema issues
     */
    public void extract() throws ModelException, IOException {
        SoapBindingStub con = null;
        try {
            File dataFile = FileUtil.getTempFile();
            CSVWriter cw = new CSVWriter(new FileWriter(dataFile));
            SoapBindingStub c = connect(getSfdcUsername(), getSfdcPassword());
            List<SObject> result = null;
            try {
                result = executeQuery(c, getSfdcQuery());
            } catch (SfdcException e) {
                throw new IOException("SFDC query execution failed: "+ e.getMessage());
            }
            if(result != null && result.size() > 0) {
                l.debug("Started retrieving SFDC data.");
                SObject firstRow = result.get(0);
                Map<String,Field> fields = describeObject(c, firstRow.getType());
                MessageElement[] frCols = firstRow.get_any();
                String[] colTypes = new String[frCols.length];
                for(int i=0; i< frCols. length; i++) {
                    String nm = frCols[i].getName();
                    colTypes[i] = getColumnType(fields, nm);
                }
                for( SObject row : result) {
                    MessageElement[] cols = row.get_any();
                    String[] vals = new String[cols.length];
                    for(int i=0; i<vals.length; i++) {
                        if(colTypes[i].equals(SourceColumn.LDM_TYPE_DATE))
                            vals[i] = cols[i].getValue().substring(0,10);
                        else
                            vals[i] = cols[i].getValue();
                    }
                    cw.writeNext(vals);
                }
                l.debug("Retrieved " + result.size() + " rows of SFDC data.");
            }
            cw.flush();
            cw.close();
            getConnectorBackend().extract(dataFile);
            FileUtil.recursiveDelete(dataFile);
        }
        catch (ServiceException e) {
            l.error("Error retrieving data from the SFDC source.", e);
        }
    }

    /**
     * Connect the SFDC
     * @param usr SFDC username
     * @param psw SFDC pasword
     * @return SFDC stub
     * @throws java.sql.SQLException in case of connection issues
     */
    private static SoapBindingStub connect(String usr, String psw) throws ServiceException {
        SoapBindingStub binding = (SoapBindingStub) new SforceServiceLocator().getSoap();

        // Time out after a minute
        binding.setTimeout(60000);
        // Test operation
        LoginResult loginResult;
        try {
            loginResult = binding.login(usr, psw);
        }
        catch (LoginFault ex) {
            // The LoginFault derives from AxisFault
            ExceptionCode exCode = ex.getExceptionCode();
            if(exCode == ExceptionCode.FUNCTIONALITY_NOT_ENABLED)
                throw new ServiceException("Error logging into the SFDC. Functionality not enabled.", ex);
            if(exCode == ExceptionCode.INVALID_CLIENT)
                throw new ServiceException("Error logging into the SFDC. Invalid client.", ex);
            if(exCode == ExceptionCode.INVALID_LOGIN)
                throw new ServiceException("Error logging into the SFDC. Invalid login.", ex);
            if(exCode == ExceptionCode.LOGIN_DURING_RESTRICTED_DOMAIN)
                throw new ServiceException("Error logging into the SFDC. Restricred domain.", ex);
            if(exCode == ExceptionCode.LOGIN_DURING_RESTRICTED_TIME)
                throw new ServiceException("Error logging into the SFDC. Restricred during time.", ex);
            if(exCode == ExceptionCode.ORG_LOCKED)
                throw new ServiceException("Error logging into the SFDC. Organization locked.", ex);
            if(exCode == ExceptionCode.PASSWORD_LOCKOUT)
                throw new ServiceException("Error logging into the SFDC. Password lock-out.", ex);
            if(exCode == ExceptionCode.SERVER_UNAVAILABLE)
                throw new ServiceException("Error logging into the SFDC. Server not available.", ex);
            if(exCode == ExceptionCode.TRIAL_EXPIRED)
                throw new ServiceException("Error logging into the SFDC. Trial expired.", ex);
            if(exCode == ExceptionCode.UNSUPPORTED_CLIENT)
                throw new ServiceException("Error logging into the SFDC. Unsupported client.", ex);
            throw new ServiceException("Error logging into the SFDC.", ex);
        } catch (Exception ex) {
            throw new ServiceException("Error logging into the SFDC.", ex);
        }
        // Check if the password has expired
        if (loginResult.isPasswordExpired()) {
            l.error("An error has occurred. Your password has expired.");
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
        return binding;
    }

    /**
     * Connects to the Derby database
     * @return SFDC  stub
     * @throws ServiceException in case of connection issues
     */
    public SoapBindingStub connect() throws ServiceException {
        return connect(getSfdcUsername(), getSfdcPassword());
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

}