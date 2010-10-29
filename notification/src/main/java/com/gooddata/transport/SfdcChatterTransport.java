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
package com.gooddata.transport;

import com.gooddata.exception.SfdcException;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import org.apache.log4j.Logger;


/**
 * SalesForce Chatter com.gooddata.transport
 * @author zd@gooddata.com
 * @version 1.0
 */
public class SfdcChatterTransport implements NotificationTransport {

    private static Logger l = Logger.getLogger(SfdcChatterTransport.class);

    private static final String PROTOCOL = "sfdc";

    private String username;
    private String password;
    private String token;

    private PartnerConnection connection;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * {@inheritDoc}
     */
    public SfdcChatterTransport(String usr, String psw, String tkn) {
        this.setUsername(usr);
        this.setPassword(psw);
        this.setToken(tkn);

        ConnectorConfig config = new ConnectorConfig();
        config.setUsername(getUsername());
        config.setPassword(getPassword());
        try {
            connection = Connector.newConnection(config);
        } catch (ConnectionException e) {
            l.debug("Error connecting to SFDC.", e);
            throw new SfdcException("Error connecting to SFDC.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void send(String message) {

        SObject user = new SObject();
        user.setType("User");
        try {
            user.setId(connection.getUserInfo().getUserId());
            user.setField("CurrentStatus", message);
            SaveResult[] results = connection.update(new SObject[] { user });
            if (!results[0].isSuccess()) {
                l.error("Error updating user status: "+ results[0].getErrors()[0].getMessage());
                throw new SfdcException("Error updating user status: "+ results[0].getErrors()[0].getMessage());
            }
        } catch (ConnectionException e) {
            l.debug("Error sending the SFDC chatter message.", e);
            throw new SfdcException("Error sending the SFDC chatter message.", e);
        }
    }

    public String getProtocol() {
        return PROTOCOL;
    }
}
