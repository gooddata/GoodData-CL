package com.gooddata.sfdc;

import java.io.InvalidObjectException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.QueryOptions;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SessionHeader;
import com.sforce.soap.partner.SforceServiceLocator;
import com.sforce.soap.partner.SoapBindingStub;
import com.sforce.soap.partner.fault.ExceptionCode;
import com.sforce.soap.partner.fault.InvalidFieldFault;
import com.sforce.soap.partner.fault.InvalidIdFault;
import com.sforce.soap.partner.fault.InvalidQueryLocatorFault;
import com.sforce.soap.partner.fault.InvalidSObjectFault;
import com.sforce.soap.partner.fault.LoginFault;
import com.sforce.soap.partner.fault.MalformedQueryFault;
import com.sforce.soap.partner.fault.UnexpectedErrorFault;

public class SfdcHelper {

	private SoapBindingStub binding;
	private boolean authenticated = false;

	private SfdcHelper() {
	}

	public SfdcHelper(String userName, String password) throws SfdcException {
		login(userName, password);
		authenticated = true;
	}

	/**
	 * The login call is used to obtain a token from Salesforce. This token must be passed to all other calls to provide
	 * authentication.
	 */
	private void login(String userName, String password) throws SfdcException {
		/**
		 * Next, the sample client application initializes the binding stub. This is our main interface to the API through which
		 * all calls are made. The getSoap method takes an optional parameter, (a java.net.URL) which is the endpoint. For the
		 * login call, the parameter always starts with http(s)://www.salesforce.com. After logging in, the sample client
		 * application changes the endpoint to the one specified in the returned loginResult object.
		 */
		try {
			binding = (SoapBindingStub) new SforceServiceLocator().getSoap();
		} catch (ServiceException e) {
			throw new SfdcException("Unable to instantiate SFDC WS API, reason: " + e.getMessage());
		}

		// Time out after a minute
		binding.setTimeout(60000);
		// Test operation
		LoginResult loginResult;
		try {
			System.out.println("LOGGING IN NOW....");
			loginResult = binding.login(userName, password);
		} catch (LoginFault ex) {
			// The LoginFault derives from AxisFault
			ExceptionCode exCode = ex.getExceptionCode();
			if (exCode == ExceptionCode.FUNCTIONALITY_NOT_ENABLED || exCode == ExceptionCode.INVALID_CLIENT
					|| exCode == ExceptionCode.INVALID_LOGIN || exCode == ExceptionCode.LOGIN_DURING_RESTRICTED_DOMAIN
					|| exCode == ExceptionCode.LOGIN_DURING_RESTRICTED_TIME || exCode == ExceptionCode.ORG_LOCKED
					|| exCode == ExceptionCode.PASSWORD_LOCKOUT || exCode == ExceptionCode.SERVER_UNAVAILABLE
					|| exCode == ExceptionCode.TRIAL_EXPIRED || exCode == ExceptionCode.UNSUPPORTED_CLIENT) {
				throw new SfdcException("Unable to log in SDFC, reason is: " + ex.getExceptionCode() + ", SFDC message: "
						+ ex.getExceptionMessage());
			} else {
				throw new SfdcException("An unexpected error has occurred, message: " + ex.getExceptionMessage() + ", code: "
						+ ex.getExceptionCode());
			}
		} catch (Exception ex) {
			throw new SfdcException("An unexpected error has occurred, message: " + ex.getMessage());
		}
		// Check if the password has expired
		if (loginResult.isPasswordExpired()) {
			throw new SfdcException("An error has occurred. Your password has expired.");
		}
		/**
		 * Once the client application has logged in successfully, it will use the results of the login call to reset the endpoint
		 * of the service to the virtual server instance that is servicing your organization. To do this, the client application
		 * sets the ENDPOINT_ADDRESS_PROPERTY of the binding object using the URL returned from the LoginResult.
		 */
		binding._setProperty(SoapBindingStub.ENDPOINT_ADDRESS_PROPERTY, loginResult.getServerUrl());
		/**
		 * The sample client application now has an instance of the SoapBindingStub that is pointing to the correct endpoint.
		 * Next, the sample client application sets a persistent SOAP header (to be included on all subsequent calls that are made
		 * with the SoapBindingStub) that contains the valid sessionId for our login credentials. To do this, the sample client
		 * application creates a new SessionHeader object and set its sessionId property to the sessionId property from the
		 * LoginResult object.
		 */
		// Create a new session header object and add the session id
		// from the login return object
		SessionHeader sh = new SessionHeader();
		sh.setSessionId(loginResult.getSessionId());
		/**
		 * Next, the sample client application calls the setHeader method of the SoapBindingStub to add the header to all
		 * subsequent method calls. This header will persist until the SoapBindingStub is destroyed until the header is explicitly
		 * removed. The "SessionHeader" parameter is the name of the header to be added.
		 */
		// set the session header for subsequent call authentication
		binding.setHeader(new SforceServiceLocator().getServiceName().getNamespaceURI(), "SessionHeader", sh);
		// return true to indicate that we are logged in, pointed
		// at the right url and have our security token in place.
	}

	public QueryResult executeQuery(String soql, int batchSize) throws SfdcException {
		if (!authenticated)
			throw new SfdcException("Queries cannot be executed on unauthenticated SFDC WS API Stub!");
		QueryOptions qo = new QueryOptions();
		qo.setBatchSize(batchSize);
		binding.setHeader(new SforceServiceLocator().getServiceName().getNamespaceURI(), "QueryOptions", qo);
		QueryResult qr;
		try {
			qr = binding.query(soql);
		} catch (InvalidSObjectFault e) {
			throw new SfdcException("Unable to execute search query: " + e.getExceptionMessage());
		} catch (MalformedQueryFault e) {
			throw new SfdcException("Unable to execute search query: " + e.getExceptionMessage());
		} catch (InvalidFieldFault e) {
			throw new SfdcException("Unable to execute search query: " + e.getExceptionMessage());
		} catch (UnexpectedErrorFault e) {
			throw new SfdcException("Unable to execute search query: " + e.getExceptionMessage());
		} catch (InvalidIdFault e) {
			throw new SfdcException("Unable to execute search query: " + e.getExceptionMessage());
		} catch (InvalidQueryLocatorFault e) {
			throw new SfdcException("Unable to execute search query: " + e.getExceptionMessage());
		} catch (RemoteException e) {
			throw new SfdcException("Unable to execute search query: " + e.getMessage());
		}
		return qr;
	}

	public QueryResult getMoreResults(String queryLocator) throws SfdcException {
		try {
			QueryResult queryMore = binding.queryMore(queryLocator);
			return queryMore;
		} catch (InvalidFieldFault e) {
			throw new SfdcException("Failed to execute query succesfully, InvalidFieldFault, error message was:" + e.getMessage());
		} catch (UnexpectedErrorFault e) {
			throw new SfdcException("Failed to execute query succesfully, UnexpectedErrorFault, error message was:"
					+ e.getMessage());
		} catch (InvalidQueryLocatorFault e) {
			throw new SfdcException("Failed to execute query succesfully, InvalidQueryLocatorFault, error message was:"
					+ e.getMessage());
		} catch (RemoteException e) {
			throw new SfdcException("Failed to execute query succesfully, error message was:" + e.getMessage());
		}
	}

	public boolean moduleExists(String module) throws SfdcException {
		DescribeSObjectResult descSObjectRslt;

		try {
			descSObjectRslt = binding.describeSObject(module);
		} catch (InvalidSObjectFault e) {
			System.out.print("Module " + module + " not found.");
			return false;
		} catch (UnexpectedErrorFault e) {
			throw new SfdcException("Failed to execute describe succesfully, UnexpectedErrorFault, error message was: " + e.getMessage());
		} catch (RemoteException e) {
			throw new SfdcException("Failed to execute describe succesfully, RemoteException, error message was: " + e.getMessage());
		}

		if (descSObjectRslt != null)
			return true;
		else
			return false;
	}

}
