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
package com.gooddata.msdynamics;

import com.gooddata.integration.soap.SoapExecutor;
import com.gooddata.util.FileUtil;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.w3c.dom.Document;

import javax.xml.soap.*;
import java.io.IOException;
import java.util.*;

/**
 * Interacts with the MS Dynamics Online
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class MsDynamicsWrapper {

    private final static String HTTPS = "https://";
    private final static String CRM_DISCOVERY_ENDPOINT = "/MSCRMServices/2007/Passport/CrmDiscoveryService.asmx";
    private final static String CRM_ENDPOINT = "/MSCrmServices/2007/CrmService.asmx";
    private final static String CRM_DISCOVERY_XMLNS = "http://schemas.microsoft.com/crm/2007/CrmDiscoveryService";

    private final static String LIVE_ID_HOST = "dev.login.live.com";
    private final static String LIVE_ID_ENDPOINT = "/wstlogin.srf";

    private final static String WSSE_XMLNS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

    private final static String ENTITY_XMLNS = "http://schemas.microsoft.com/crm/2006/WebServices";

    private final static String LIVE_ID_SERVER_PLACEHOLDER = "%SERVER%";
    private final static String LIVE_ID_USERNAME_PLACEHOLDER = "%USERNAME%";
    private final static String LIVE_ID_PASSWORD_PLACEHOLDER = "%PASSWORD%";
    private final static String LIVE_ID_POLICY_PLACEHOLDER = "%POLICY%";

    private final static String CRM_ORGANIZATION_PLACEHOLDER = "%ORGANIZATION%";
    private final static String LIVE_ID_TICKET_PLACEHOLDER = "%LIVEID%";
    private final static String CRM_TICKET_PLACEHOLDER = "%CRMTICKET%";
    private final static String CRM_ENTITY_PLACEHOLDER = "%ENTITY%";
    private final static String CRM_ATTRIBUTES_PLACEHOLDER = "%ATTRIBUTES%";

    private SoapExecutor soap;
    private String host;
    private String username;
    private String password;
    private String organization;
    private String policy;
    private String liveId;
    private String crmTicket;

    public static void main(String[] args) throws Exception {
        MsDynamicsWrapper m = new MsDynamicsWrapper("na40.crm.dynamics.com", "crmNAorgfcbe3","zsvoboda@gmail.com", "zs5065%)^%");
        String policy = m.retrievePolicy();
        m.setPolicy(policy);
        System.out.println("POLICY:"+policy);
        String liveid = m.login();
        System.out.println("LIVE ID:"+liveid);
        m.setLiveId(liveid);
        String crmTicket = m.retrieveCrmTicket();
        System.out.println("CRM TICKET:"+crmTicket);
        m.setCrmTicket(crmTicket);
        List<Map<String,String>> result = m.retrieveMultiple("opportunity", new String[] {"opportunityid", "name", "estimatedvalue"});
        System.err.println(result);
    }

    public MsDynamicsWrapper(String hostName, String organization, String user, String password) {
        soap = new SoapExecutor();
        setHost(hostName);
        setOrganization(organization);
        setUsername(user);
        setPassword(password);
    }

    public String retrievePolicy() throws IOException, SOAPException, JaxenException {
        String msg = FileUtil.readStringFromClasspath("/com/gooddata/msdynamics/RetrievePolicy.xml");
        SOAPMessage response = soap.execute(HTTPS + host + CRM_DISCOVERY_ENDPOINT, msg);
        XPath xp = soap.createXPath("//crm:Policy/text()", response);
        xp.addNamespace("crm", CRM_DISCOVERY_XMLNS);
        Text result = (Text)xp.selectSingleNode(response.getSOAPBody());
        return result.getValue();
    }

    public String retrieveCrmTicket() throws IOException, SOAPException, JaxenException {
        String msg = FileUtil.readStringFromClasspath("/com/gooddata/msdynamics/RetrieveCrmTicket.xml");
        msg = msg.replace(CRM_ORGANIZATION_PLACEHOLDER, getOrganization());
        msg = msg.replace(LIVE_ID_TICKET_PLACEHOLDER, getLiveId());
        SOAPMessage response = soap.execute(HTTPS + host + CRM_DISCOVERY_ENDPOINT, msg);
        XPath xp = soap.createXPath("//crm:CrmTicket/text()", response);
        xp.addNamespace("crm", CRM_DISCOVERY_XMLNS);
        Text result = (Text)xp.selectSingleNode(response.getSOAPBody());
        return result.getNodeValue();
    }

    public List<Map<String,String>> retrieveMultiple(String entity, String[] columns) throws IOException, SOAPException, JaxenException {
        List<Map<String,String>> ret = new ArrayList<Map<String,String>>();
        String msg = FileUtil.readStringFromClasspath("/com/gooddata/msdynamics/RetrieveMultiple.xml");
        msg = msg.replace(CRM_ORGANIZATION_PLACEHOLDER, getOrganization());
        msg = msg.replace(CRM_TICKET_PLACEHOLDER, getCrmTicket());
        msg = msg.replace(CRM_ENTITY_PLACEHOLDER, entity);
        String columnsElement = "";
        for(int i=0; i<columns.length; i++) {
            columnsElement += "<ns4:Attribute>"+columns[i]+"</ns4:Attribute>";
        }
        msg = msg.replace(CRM_ATTRIBUTES_PLACEHOLDER, columnsElement);
        SOAPMessage response = soap.execute(HTTPS + host + CRM_ENDPOINT, msg);
        XPath xp = soap.createXPath("//crm:BusinessEntity", response);
        xp.addNamespace("crm", ENTITY_XMLNS);
        List result = xp.selectNodes(response.getSOAPBody());
        for(Object o : result) {
            SOAPElement e = (SOAPElement)o;
            Map<String,String> instance =  new HashMap<String,String>();
            Iterator elements = e.getChildElements();
            while(elements.hasNext()) {
                SOAPElement name = (SOAPElement)elements.next();
                String value = name.getFirstChild().getNodeValue();
                instance.put(name.getElementName().getLocalName(),value);
                ret.add(instance);
            }
        }
        return ret;
    }

    public String login() throws IOException, SOAPException, JaxenException {
        String msg = FileUtil.readStringFromClasspath("/com/gooddata/msdynamics/LiveIdLogin.xml");
        msg = msg.replaceAll(LIVE_ID_SERVER_PLACEHOLDER, getHost());
        msg = msg.replaceAll(LIVE_ID_USERNAME_PLACEHOLDER, getUsername());
        msg = msg.replaceAll(LIVE_ID_PASSWORD_PLACEHOLDER, getPassword());
        msg = msg.replaceAll(LIVE_ID_POLICY_PLACEHOLDER, getPolicy());
        SOAPMessage response = soap.execute(HTTPS + LIVE_ID_HOST + LIVE_ID_ENDPOINT, msg);
        XPath xp = soap.createXPath("//wsse:BinarySecurityToken/text()", response);
        xp.addNamespace("wsse", WSSE_XMLNS);
        Node result = (Node)xp.selectSingleNode(response.getSOAPBody());
        return result.getValue();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    private void dumpXML(Document doc) throws IOException {
        OutputFormat format = new OutputFormat(doc);
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);
        XMLSerializer serializer = new XMLSerializer(System.out, format);
        serializer.serialize(doc);
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String l) {
        this.liveId = l;
    }

    public String getCrmTicket() {
        return crmTicket;
    }

    public void setCrmTicket(String crmTicket) {
        this.crmTicket = crmTicket;
    }
}
