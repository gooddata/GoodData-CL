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

import com.gooddata.exception.ProcessingException;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.msdynamics.MsDynamicsWrapper;
import com.gooddata.processor.CliParams;
import com.gooddata.processor.Command;
import com.gooddata.processor.ProcessingContext;
import com.gooddata.util.FileUtil;
import com.gooddata.liveid.LogonManager;
import com.gooddata.liveid.SecurityToken;
import com.microsoft.schemas.crm._2006.query.ArrayOfString;
import com.microsoft.schemas.crm._2006.query.ColumnSet;
import com.microsoft.schemas.crm._2006.query.QueryExpression;
import com.microsoft.schemas.crm._2007.coretypes.CallerOriginToken;
import com.microsoft.schemas.crm._2007.coretypes.CrmAuthenticationToken;
import com.microsoft.schemas.crm._2007.crmdiscoveryservice.*;
import com.microsoft.schemas.crm._2007.crmdiscoveryservice.Execute;
import com.microsoft.schemas.crm._2007.crmdiscoveryservice.ExecuteResponse;
import com.microsoft.schemas.crm._2007.webservices.CallerOriginTokenE;
import com.microsoft.schemas.crm._2007.webservices.CrmAuthenticationTokenE;
import com.microsoft.schemas.crm._2007.webservices.CorrelationTokenE;
import com.microsoft.schemas.crm._2007.webservices.CrmServiceStub;
import com.microsoft.schemas.crm._2007.webservices.RetrieveMultiple;
import com.microsoft.wsdl.types.Guid;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * GoodData MS Dynamics connector
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class MsDynamicsConnector extends AbstractConnector implements Connector {


    private static Logger l = Logger.getLogger(MsDynamicsConnector.class);




    public static void main(String[] args) throws Exception {

        String username = "zsvoboda@gmail.com";
        String password = "zs5065%)^%";
        String organizationName = "crmNAorgfcbe3";  //Whats Org Name

        String server = "na40.crm.dynamics.com";

        String endpointURL = "http://"+server+"/MSCrmServices/2007/CrmService.asmx";



        CrmDiscoveryServiceStub dStub = new CrmDiscoveryServiceStub();

        RetrievePolicyRequest policyRequest = new RetrievePolicyRequest();

        Execute policyExecute = new Execute();
        policyExecute.setRequest(policyRequest);
        ExecuteResponse resp = dStub.execute(policyExecute);
        RetrievePolicyResponse policyResponse = (RetrievePolicyResponse)resp.getResponse();
        String policy = policyResponse.getPolicy();

        /*
        System.out.println("POLICY:"+policy);


        SecurityToken securityToken = new LogonManager().logon(server,username, password);

        System.out.println("Logon succeeded!");
        System.out.println("Passport Token: " + securityToken.
                getBinarySecurityToken());
        System.out.println("Issue Date: " + securityToken.getIssueDate());
        System.out.println("Expire Date: " + securityToken.getExpireDate());

        */

        MsDynamicsWrapper m = new MsDynamicsWrapper("na40.crm.dynamics.com", "crmNAorgfcbe3","zsvoboda@gmail.com", "zs5065%)^%");
        policy = m.retrievePolicy();
        m.setPolicy(policy);
        System.out.println("POLICY:"+policy);
        String liveid = m.login().trim();
        System.out.println("LIVE ID:"+liveid);
        m.setLiveId(liveid);


        RetrieveOrganizationRequest orgRequest = new RetrieveOrganizationRequest();
        orgRequest.setPassportTicket(liveid);
        orgRequest.setOrganizationName(organizationName);
        Execute orgExecute = new Execute();
        orgExecute.setRequest(orgRequest);
        resp = dStub.execute(orgExecute);
        RetrieveOrganizationResponse orgResponse = (RetrieveOrganizationResponse)resp.getResponse();
        String crmServiceUrl = orgResponse.getOrganizationDetail().getCrmServiceUrl();

        System.out.println("CRM URL:"+crmServiceUrl);

        RetrieveCrmTicketRequest crmTicketRequest = new RetrieveCrmTicketRequest();
        crmTicketRequest.setOrganizationName(organizationName);
        crmTicketRequest.setPassportTicket(liveid);
        Execute ticketExecute = new Execute();
        ticketExecute.setRequest(crmTicketRequest);
        resp = dStub.execute(ticketExecute);
        RetrieveCrmTicketResponse ticketResponse = (RetrieveCrmTicketResponse)resp.getResponse();
        String crmTicket = ticketResponse.getCrmTicket();

        System.out.println("CRM TICKET:"+crmTicket+" EXPIRES:"+ticketResponse.getExpirationDate());

        CrmServiceStub crmService = new CrmServiceStub();

        ColumnSet columnSet = new ColumnSet();
        ArrayOfString columns = new ArrayOfString();
        columns.addAttribute("fullname");
        columns.addAttribute("contactid");
        columnSet.setAttributes(columns);

        QueryExpression query = new QueryExpression();
        query.setColumnSet(columnSet);
        query.setEntityName("contact");

        RetrieveMultiple retrieveMultiple = new RetrieveMultiple();
        retrieveMultiple.setQuery(query);
        CrmAuthenticationTokenE crmAuthToken = new CrmAuthenticationTokenE();
        CrmAuthenticationToken cat = new CrmAuthenticationToken();
        cat.setAuthenticationType(1);
        cat.setCrmTicket(crmTicket);
        cat.setOrganizationName(organizationName);
        Guid guid = new Guid();
        guid.setGuid("00000000-0000-0000-0000-000000000000");
        cat.setCallerId(guid);
        crmAuthToken.setCrmAuthenticationToken(cat);

        CallerOriginTokenE callerOriginToken = new CallerOriginTokenE();
        CallerOriginToken clo = new CallerOriginToken();
        callerOriginToken.setCallerOriginToken(clo);


        crmService.retrieveMultiple(retrieveMultiple, crmAuthToken, callerOriginToken, new CorrelationTokenE());







        /*

        CrmServiceStub.CrmAuthenticationToken token = new CrmAuthenticationToken();
        token.setCallerId("00000000-0000-0000-0000-000000000000");
        token.setOrganizationName(OrganizationName);
        token.setCrmTicket(securityToken.getBinarySecurityToken());
        token.setAuthenticationType(1);

        CrmServiceSoapStub bindingStub = (CrmServiceSoapStub) new CrmServiceLocator().getCrmServiceSoap(new URL(endpointURL));
        bindingStub.setHeader("http://schemas.microsoft.com/crm/2007/WebServices",
                "CrmAuthenticationToken", token);

        bindingStub.setUsername(username);
        bindingStub.setPassword(password);

        bindingStub.retrieve("Opportunity", "", null);
        */

    }

    /**
     * Creates a new Facebook connector
     *
     * @return a new instance of the FacebookConnector
     */
    public static MsDynamicsConnector createConnector() {
        return new MsDynamicsConnector();
    }

    /**
     * Saves a template of the config file
     *
     * @param name           new schema name
     * @param configFileName config file name
     * @throws java.io.IOException   if there is a problem with writing the config file
     * @throws java.sql.SQLException if there is a problem with the db
     */
    public static void saveConfigTemplate(String name, String configFileName, String folder)
            throws IOException {
        l.debug("Saving Facebook config template.");
        final SourceSchema s = SourceSchema.createSchema(name);

        SourceColumn o = new SourceColumn("objectid", SourceColumn.LDM_TYPE_DATE, "FAcebook Object ID");
        if (folder != null && folder.length() > 0)
            o.setFolder(folder);
        s.addColumn(o);

        SourceColumn dt = new SourceColumn("date", SourceColumn.LDM_TYPE_DATE, "date");
        dt.setFormat(Constants.DEFAULT_DATE_FMT_STRING);
        if (folder != null && folder.length() > 0)
            dt.setFolder(folder);
        s.addColumn(dt);

        SourceColumn metric = new SourceColumn("metric", SourceColumn.LDM_TYPE_ATTRIBUTE, "metric");
        if (folder != null && folder.length() > 0)
            metric.setFolder(folder);
        s.addColumn(metric);

        SourceColumn value = new SourceColumn("value", SourceColumn.LDM_TYPE_FACT, "fact");
        if (folder != null && folder.length() > 0)
            value.setFolder(folder);
        s.addColumn(value);

        s.writeConfig(new File(configFileName));
        l.debug("Saved Facebook Insights config template.");
    }


    /**
     * {@inheritDoc}
     */
    public void extract(String dir) throws IOException {
        File dataFile = new File(dir + System.getProperty("file.separator") + "data.csv");
        extract(dataFile.getAbsolutePath(), true);
    }

    /**
     * {@inheritDoc}
     */
    public void dump(String file) throws IOException {
        extract(file, false);
    }

    /**
     * Extract rows
     *
     * @param file        name of the target file
     * @param extendDates add date/time facts
     * @throws java.io.IOException
     */
    public void extract(String file, final boolean extendDates) throws IOException {
        File dataFile = new File(file);

    }

    /**
     * {@inheritDoc}
     */
    public boolean processCommand(Command c, CliParams cli, ProcessingContext ctx) throws ProcessingException {
        try {
            if (c.match("GenerateFacebookInsightsConfig")) {
                generateConfig(c, cli, ctx);
            } else if (c.match("LoadFacebookInsights") || c.match("UseFacebookInsights")) {
                load(c, cli, ctx);
            } else
                return super.processCommand(c, cli, ctx);
        } catch (IOException e) {
            throw new ProcessingException(e);
        }
        return true;
    }

    /**
     * Loads new Facebook file command processor
     *
     * @param c   command
     * @param p   command line arguments
     * @param ctx current processing context
     * @throws java.io.IOException in case of IO issues
     */
    private void load(Command c, CliParams p, ProcessingContext ctx) throws IOException {

        String configFile = c.getParamMandatory("configFile");
        String auth = c.getParamMandatory("authToken");
        File conf = FileUtil.getFile(configFile);
        initSchema(conf.getAbsolutePath());
        // sets the current connector
        ctx.setConnector(this);
        setProjectId(ctx);
        l.info("Facebook Insights Connector successfully loaded.");
    }

    /**
     * Generate new config file from CSV command processor
     *
     * @param c   command
     * @param p   command line arguments
     * @param ctx current processing context
     * @throws java.io.IOException in case of IO issues
     */
    private void generateConfig(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        String name = c.getParamMandatory("name");
        String configFile = c.getParamMandatory("configFile");
        String folder = c.getParam("folder");
        MsDynamicsConnector.saveConfigTemplate(name, configFile, folder);
        l.info("Facebook Insights Connector configuration successfully generated. See config file: " + configFile);
    }


}