/*
 * .
 */

/**
 * Soap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public interface Soap extends java.rmi.Remote {

    /**
     * Login to the Salesforce.com SOAP Api
     */
    public LoginResult login(String username, String password) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.LoginFault;

    /**
     * Describe an sObject
     */
    public DescribeSObjectResult describeSObject(String sObjectType) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault;

    /**
     * Describe a number sObjects
     */
    public DescribeSObjectResult[] describeSObjects(String[] sObjectType) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault;

    /**
     * Describe the Global state
     */
    public DescribeGlobalResult describeGlobal() throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Describe the layout of an sObject
     */
    public DescribeLayoutResult describeLayout(String sObjectType, String[] recordTypeIds) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidIdFault;

    /**
     * Describe the layout of the SoftPhone
     */
    public DescribeSoftphoneLayoutResult describeSoftphoneLayout() throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Describe the tabs that appear on a users page
     */
    public DescribeTabSetResult[] describeTabs() throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Create a set of new sObjects
     */
    public SaveResult[] create(com.sforce.soap.partner.sobject.SObject[] sObjects) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.InvalidFieldFault;

    /**
     * Update a set of sObjects
     */
    public SaveResult[] update(com.sforce.soap.partner.sobject.SObject[] sObjects) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.InvalidFieldFault;

    /**
     * Update or insert a set of sObjects based on object id
     */
    public com.sforce.soap.partner.UpsertResult[] upsert(String externalIDFieldName, com.sforce.soap.partner.sobject.SObject[] sObjects) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.InvalidFieldFault;

    /**
     * Merge and update a set of sObjects based on object id
     */
    public MergeResult[] merge(MergeRequest[] request) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.InvalidFieldFault;

    /**
     * Delete a set of sObjects
     */
    public DeleteResult[] delete(String[] ids) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Undelete a set of sObjects
     */
    public com.sforce.soap.partner.UndeleteResult[] undelete(String[] ids) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Empty a set of sObjects from the recycle bin
     */
    public EmptyRecycleBinResult[] emptyRecycleBin(String[] ids) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Get a set of sObjects
     */
    public com.sforce.soap.partner.sobject.SObject[] retrieve(String fieldList, String sObjectType, String[] ids) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.MalformedQueryFault, com.sforce.soap.partner.fault.InvalidFieldFault;

    /**
     * Submit an entity to a workflow process or process a workitem
     */
    public ProcessResult[] process(ProcessRequest[] actions) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidIdFault;

    /**
     * convert a set of leads
     */
    public LeadConvertResult[] convertLead(LeadConvert[] leadConverts) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Logout the current user, invalidating the current session.
     */
    public void logout() throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Logs out and invalidates session ids
     */
    public InvalidateSessionsResult[] invalidateSessions(String[] sessionIds) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Get the IDs for deleted sObjects
     */
    public GetDeletedResult getDeleted(String sObjectType, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault;

    /**
     * Get the IDs for updated sObjects
     */
    public GetUpdatedResult getUpdated(String sObjectType, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault;

    /**
     * Create a Query Cursor
     */
    public QueryResult query(String queryString) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.InvalidQueryLocatorFault, com.sforce.soap.partner.fault.MalformedQueryFault, com.sforce.soap.partner.fault.InvalidFieldFault;

    /**
     * Create a Query Cursor, including deleted sObjects
     */
    public QueryResult queryAll(String queryString) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.InvalidQueryLocatorFault, com.sforce.soap.partner.fault.MalformedQueryFault, com.sforce.soap.partner.fault.InvalidFieldFault;

    /**
     * Gets the next batch of sObjects from a query
     */
    public QueryResult queryMore(String queryLocator) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidQueryLocatorFault, com.sforce.soap.partner.fault.InvalidFieldFault;

    /**
     * Search for sObjects
     */
    public SearchResult search(String searchString) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.MalformedSearchFault, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidFieldFault;

    /**
     * Gets server timestamp
     */
    public GetServerTimestampResult getServerTimestamp() throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Set a user's password
     */
    public SetPasswordResult setPassword(String userId, String password) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.InvalidNewPasswordFault;

    /**
     * Reset a user's password
     */
    public ResetPasswordResult resetPassword(String userId) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidIdFault;

    /**
     * Returns standard information relevant to the current user
     */
    public GetUserInfoResult getUserInfo() throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Send outbound email
     */
    public SendEmailResult[] sendEmail(Email[] messages) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault;
}
