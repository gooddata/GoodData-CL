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
package com.gooddata.sugar;

import com.gooddata.integration.soap.SoapExecutor;
import com.gooddata.util.CSVWriter;
import com.gooddata.util.FileUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import javax.xml.soap.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Java wrapper of selected MS Sugar CRM Online web services
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class SugarCrmWrapper {

    private final static String PROTOCOL = "http://";
    private final static String SUGAR_ENDPOINT = "/service/v2/soap.php";

    private final static int MAX_ROWS = 1000;

    private final static String USERNAME_PLACEHOLDER = "%USERNAME%";
    private final static String PASSWORD_PLACEHOLDER = "%PASSWORD%";

    private final static String SESSION_PLACEHOLDER = "%SESSION%";

    private final static String MODULE_PLACEHOLDER = "%MODULE%";
    private final static String FIELDS_PLACEHOLDER = "%FIELDS%";
    private final static String LINKED_FIELDS_PLACEHOLDER = "%LINKED_FIELDS%";
    private final static String QUERY_PLACEHOLDER = "%QUERY%";
    private final static String FIELDS_COUNT_PLACEHOLDER = "%FIELDS_COUNT%";
    private final static String MAX_ROWS_PLACEHOLDER = "%MAX_ROWS%";
    private final static String OFFSET_PLACEHOLDER = "%OFFSET%";

    // SAAJ SOAP executor
    private SoapExecutor soap;
    // Sugar CRM host
    private String host;
    // Sugar CRM username
    private String username;
    // Sugar CRM password
    private String password;
    // Sugar CRM session token
    private String sessionToken;


    public static void main(String[] arg) throws Exception {
        SugarCrmWrapper s = new SugarCrmWrapper("trial.sugarondemand.com/fanodg1159", "jim", "jim");
        s.connect();
        s.getAllEntries("Users",
                new String[] {},
                new String[] {},
                "",
                "/Users/zdenek/temp/sugar_opps.csv");
    }

    public int getAllEntries(String module, String[] fields, String[] linked_fields,
                             String query, String csvFile)
            throws IOException, SOAPException, JaxenException {
        int cnt = 0;
        CSVWriter cw = FileUtil.createUtf8CsvEscapingWriter(new File(csvFile));
        try {
            int nextIndex = 0;
            while (nextIndex >=0) {
                List<Map<String,String>> ret = new ArrayList<Map<String,String>>();
                nextIndex = getEntries(module, fields, linked_fields, query, nextIndex, ret);
                for(Map<String,String>  m : ret) {
                    String[] row = null;
                    if(linked_fields != null && linked_fields.length>0) {
                        row = new String[fields.length + linked_fields.length];
                    }
                    else {
                        row = new String[fields.length];
                    }
                    for(int i=0; i<fields.length; i++) {
                        row[i] = m.get(fields[i]);
                    }
                    if(linked_fields != null && linked_fields.length>0) {
                        for(int i=0; i<linked_fields.length; i++) {
                            row[fields.length+i] = m.get(linked_fields[i]);
                        }
                    }
                    cw.writeNext(row);
                }
                cnt += ret.size();
                cw.flush();
            }
        }
        finally {
            cw.close();
        }
        return cnt;

    }

    /**
     * Constructor
     * @param hostName Sugar CRM Online host
     * @param user Sugar CRM Online username
     * @param password Sugar CRM Online password
     */
    public SugarCrmWrapper(String hostName, String user, String password) {
        soap = new SoapExecutor();
        setHost(hostName);
        setUsername(user);
        setPassword(password);
    }

    /**
     * Connects the Sugar CRM Online
     * @throws org.jaxen.JaxenException issue with the response format
     * @throws java.io.IOException generic IO issue
     * @throws javax.xml.soap.SOAPException issue with SOAP invocation
     */
    public void connect() throws JaxenException, IOException, SOAPException {
        String token = login();
        setSessionToken(token);
    }


    /**
     * Logs into the MS Sugar CRM Online
     * @return the session token
     * @throws org.jaxen.JaxenException issue with the response format
     * @throws java.io.IOException generic IO issue
     * @throws javax.xml.soap.SOAPException issue with SOAP invocation
     */
    public String login() throws IOException, SOAPException, JaxenException {
        String msg = FileUtil.readStringFromClasspath("/com/gooddata/sugar/Login.xml", SugarCrmWrapper.class);
        msg = msg.replaceAll(USERNAME_PLACEHOLDER, getUsername());
        msg = msg.replaceAll(PASSWORD_PLACEHOLDER, getPasswordMD5());
        String endpoint = PROTOCOL + getHost() + SUGAR_ENDPOINT;
        SOAPMessage response = soap.execute(endpoint, msg);
        XPath xp = soap.createXPath("//id/text()", response);
        Node result = (Node)xp.selectSingleNode(response.getSOAPBody());
        return result.getNodeValue();
    }

    public int getEntries(String module, String[] fields,  String[] linked_fields,
                          String query, int offset, List<Map<String,String>> ret)
            throws IOException, SOAPException, JaxenException {
        if(module != null && module.length() > 0) {
            String msg = FileUtil.readStringFromClasspath("/com/gooddata/sugar/GetEntryList.xml", SugarCrmWrapper.class);
            msg = msg.replaceAll(SESSION_PLACEHOLDER, getSessionToken());
            msg = msg.replaceAll(MODULE_PLACEHOLDER, module);
            msg = msg.replaceAll(FIELDS_COUNT_PLACEHOLDER, Integer.toString(fields.length));
            String fieldsXml = "";
            if(linked_fields != null && linked_fields.length > 0) {
                fieldsXml += "<link_name_to_fields_array xsi:type='SOAP-ENC:Array' SOAP-ENC:arrayType='tns:link_name_to_fields_array["+
                        linked_fields.length+"]'>";
                for(int i=0; i<linked_fields.length; i++) {
                    String[] components = linked_fields[i].split("\\.");
                    if(components != null && components.length == 2) {
                        fieldsXml += "<item><name>"+components[0].toLowerCase()+"</name><value><item>"+components[1]+"</item></value></item>";
                    }
                    else {
                        throw new SOAPException("getEntries: the linked fields must have format module.field .");
                    }
                }
                fieldsXml += "</link_name_to_fields_array>";
            }
            msg = msg.replaceAll(LINKED_FIELDS_PLACEHOLDER, fieldsXml);
            msg = msg.replaceAll(QUERY_PLACEHOLDER, query);
            msg = msg.replaceAll(MAX_ROWS_PLACEHOLDER, Integer.toString(MAX_ROWS));
            msg = msg.replaceAll(OFFSET_PLACEHOLDER, Integer.toString(offset));
            fieldsXml = "";
            if(fields != null && fields.length > 0) {
                for(int i=0; i < fields.length; i++) {
                    fieldsXml += "<item xsi:type='xsd:string'>"+fields[i]+"</item>";
                }
            }
            msg = msg.replaceAll(FIELDS_PLACEHOLDER, fieldsXml);
            //System.err.println(msg);
            String endpoint = PROTOCOL + getHost() + SUGAR_ENDPOINT;
            SOAPMessage response = soap.execute(endpoint, msg);
            //System.err.println(soap.dumpSoapMessage(response));
            XPath xp = soap.createXPath("//entry_list/item", response);
            List result = xp.selectNodes(response.getSOAPBody());
            if(result != null && result.size()>0) {
                for(int j=0; j<result.size(); j++) {
                    SOAPElement e = (SOAPElement)result.get(j);
                    NodeList ids = e.getElementsByTagName("id");
                    if(ids != null && ids.getLength() > 0) {
                        Node id = ids.item(0);
                        Node text = id.getFirstChild();
                        if(text != null) {
                            Map<String,String> record = new HashMap<String,String>();
                            record.put("id", text.getNodeValue());
                            XPath xpd = soap.createXPath("//entry_list/item["+(j+1)+"]/name_value_list/item", response);
                            List dataResult = xpd.selectNodes(response.getSOAPBody());
                            if(dataResult != null && dataResult.size()>0) {
                                for(int i = 0; i<dataResult.size(); i++) {
                                    SOAPElement dataNode = (SOAPElement)dataResult.get(i);
                                    NodeList names = dataNode.getElementsByTagName("name");
                                    NodeList values = dataNode.getElementsByTagName("value");
                                    if(names != null && names.getLength()>0 && values != null && values.getLength()>0) {
                                        Node name = names.item(0).getFirstChild();
                                        Node value = values.item(0).getFirstChild();
                                        if(name != null) {
                                            if(value != null) {
                                                record.put(name.getNodeValue(), value.getNodeValue());
                                            }
                                            else {
                                                record.put(name.getNodeValue(), "");
                                            }
                                        }
                                        else {
                                            throw new SOAPException("getEntries: No name texts in the result row.");
                                        }
                                    }
                                    else {
                                        throw new SOAPException("getEntries: No name/value pair in the result row.");
                                    }
                                }
                            }
                            else {
                                throw new SOAPException("getEntries: No record items in the result row.");
                            }
                            if(linked_fields != null && linked_fields.length > 0) {
                                for(int k=0; k<linked_fields.length; k++) {
                                    // take only the first item
                                    xpd = soap.createXPath("//relationship_list/item["+(j+1)+"]/item["+(k+1)+"]/records/item/item[1]", response);
                                    Object res = xpd.selectSingleNode(response.getSOAPBody());
                                    if(res != null) {
                                        SOAPElement dataNode = (SOAPElement)res;
                                        NodeList names = dataNode.getElementsByTagName("name");
                                        NodeList values = dataNode.getElementsByTagName("value");
                                        if(names != null && names.getLength()>0 && values != null && values.getLength()>0) {
                                            Node name = names.item(0).getFirstChild();
                                            Node value = values.item(0).getFirstChild();
                                            if(name != null) {
                                                if(value != null) {
                                                    record.put(linked_fields[k], value.getNodeValue());
                                                }
                                                else {
                                                    record.put(linked_fields[k], "");
                                                }
                                            }
                                            else {
                                                throw new SOAPException("getEntries: No linked module name texts in the result row.");
                                            }
                                        }
                                        else {
                                            throw new SOAPException("getEntries: No name/value pair in the result row.");
                                        }
                                    }
                                }
                            }
                            ret.add(record);
                        }
                        else {
                            throw new SOAPException("getEntries: No text in the id element.");
                        }
                    }
                    else {
                        throw new SOAPException("getEntries: No row id in the result.");
                    }
                }
                XPath xpn = soap.createXPath("//next_offset/text()", response);
                Node nr = (Node)xpn.selectSingleNode(response.getSOAPBody());
                if(nr != null) {
                    String v = nr.getNodeValue();
                    if(v != null) {
                        return Integer.parseInt(nr.getNodeValue());
                    }
                    else {
                        return -1;
                    }
                }
                else {
                    return -1;
                }
            }
            else {
                return -1;
            }
        }
        else {
            throw new SOAPException("The getEntries module parameter can't be empty.");
        }
    }

// Getters and Setters

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


    public String getPasswordMD5() {
        return DigestUtils.md5Hex(getPassword());
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
