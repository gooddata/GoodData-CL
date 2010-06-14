package com.gooddata.connector;

import au.com.bytecode.opencsv.CSVWriter;
import com.gooddata.exception.InitializationException;
import com.gooddata.exception.InvalidArgumentException;
import com.gooddata.exception.MetadataFormatException;
import com.gooddata.exception.ModelException;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.sfdc.SfdcException;
import com.gooddata.util.FileUtil;
import com.gooddata.util.JdbcUtil;
import com.gooddata.util.StringUtil;
import com.sforce.soap.partner.*;
import com.sforce.soap.partner.fault.*;
import com.sforce.soap.partner.sobject.SObject;
import org.apache.axis.message.MessageElement;
import org.apache.log4j.Logger;
import org.gooddata.connector.AbstractConnector;
import org.gooddata.connector.Connector;
import org.w3c.dom.NodeList;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * @param sfdcUsr JDBC user
     * @param sfdcPsw JDBC password
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
     * @param sfdcUsr JDBC user
     * @param sfdcPsw JDBC password
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


    private static List<SObject> executeQuery(SoapBindingStub binding, String sfdcQuery, int records) throws SfdcException {
        List<SObject> result = new ArrayList<SObject>();
        QueryOptions qo = new QueryOptions();
        if(records < 500)
            qo.setBatchSize(records);
        binding.setHeader(new SforceServiceLocator().getServiceName().getNamespaceURI(),
             "QueryOptions", qo);
        try {
            int cnt = 0;
            QueryResult qr = binding.query(sfdcQuery);
            while (qr.getSize()>0 && cnt < records) {
                SObject[] sObjects = qr.getRecords();
                result.addAll(Arrays.asList(sObjects));
                cnt += sObjects.length;
                qr = binding.queryMore(qr.getQueryLocator());
            }
        }
        catch (ApiQueryFault ex) {
         throw new SfdcException("Failed to execute SFDC query: " + ex.getMessage());
        }
        catch (UnexpectedErrorFault e) {
    	 throw new SfdcException("Failed to execute SFDC query: " + e.getMessage());
	    }
        catch (InvalidIdFault e) {
	        throw new SfdcException("Failed to execute SFDC query: " + e.getMessage());
	    }
        catch (InvalidQueryLocatorFault e) {
		    throw new SfdcException("Failed to execute SFDC query: " + e.getMessage());
	    }
        catch (RemoteException e) {
		    throw new SfdcException("Failed to execute SFDC query: " + e.getMessage());
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
            throws InvalidArgumentException, IOException, ServiceException, SfdcException {
        SourceSchema s = SourceSchema.createSchema(name);
        SoapBindingStub c = connect(sfdcUsr, sfdcPsw);
        List<SObject> result = executeQuery(c, query, 1);
        for( SObject row : result) {
            //            
        }
        s.writeConfig(new File(configFileName));
    }

    private static String getColumnType(int jct) {
        String type;
        switch (jct) {
            case Types.CHAR:
                type = SourceColumn.LDM_TYPE_ATTRIBUTE;
                break;
            case Types.VARCHAR:
                type = SourceColumn.LDM_TYPE_ATTRIBUTE;
                break;
            case Types.NCHAR:
                type = SourceColumn.LDM_TYPE_ATTRIBUTE;
                break;
            case Types.NVARCHAR:
                type = SourceColumn.LDM_TYPE_ATTRIBUTE;
                break;
            case Types.INTEGER:
                type = SourceColumn.LDM_TYPE_ATTRIBUTE;
                break;
            case Types.BIGINT:
                type = SourceColumn.LDM_TYPE_ATTRIBUTE;
                break;
            case Types.FLOAT:
                type = SourceColumn.LDM_TYPE_FACT;
                break;
            case Types.DOUBLE:
                type = SourceColumn.LDM_TYPE_FACT;
                break;
            case Types.DECIMAL:
                type = SourceColumn.LDM_TYPE_FACT;
                break;
            case Types.NUMERIC:
                type = SourceColumn.LDM_TYPE_FACT;
                break;
            case Types.DATE:
                type = SourceColumn.LDM_TYPE_DATE;
                break;
            case Types.TIMESTAMP:
                type = SourceColumn.LDM_TYPE_DATE;
                break;
            default:
                type = SourceColumn.LDM_TYPE_ATTRIBUTE;
                break;
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
            con = connect(sfdcUsername, sfdcPassword);
            File dataFile = FileUtil.getTempFile();
            CSVWriter cw = new CSVWriter(new FileWriter(dataFile));
            l.debug("Started retrieving JDBC data.");
            l.debug("Finished retrieving JDBC data.");
            cw.flush();
            cw.close();
            getConnectorBackend().extract(dataFile);
            FileUtil.recursiveDelete(dataFile);
        }
        catch (ServiceException e) {
            l.error("Error retrieving data from the SFDC source.", e);
        } finally {
            
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
     * JDBC username getter
     * @return JDBC username
     */
    public String getSfdcUsername() {
        return sfdcUsername;
    }

    /**
     * JDBC username setter
     * @param sfdcUsername JDBC username
     */
    public void setSfdcUsername(String sfdcUsername) {
        this.sfdcUsername = sfdcUsername;
    }

    /**
     * JDBC password getter
     * @return JDBC password
     */
    public String getSfdcPassword() {
        return sfdcPassword;
    }

    /**
     * JDBC password setter
     * @param sfdcPassword JDBC password
     */
    public void setSfdcPassword(String sfdcPassword) {
        this.sfdcPassword = sfdcPassword;
    }

    /**
     * JDBC query getter
     * @return JDBC query
     */
    public String getSfdcQuery() {
        return sfdcQuery;
    }

    /**
     * JDBC query setter
     * @param sfdcQuery JDBC query
     */
    public void setSfdcQuery(String sfdcQuery) {
        this.sfdcQuery = sfdcQuery;
    }

}