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

package org.snaplogic.snap.gooddata;

import com.gooddata.integration.datatransfer.GdcDataTransferAPI;
import com.gooddata.integration.ftp.GdcFTPApiWrapper;
import com.gooddata.integration.rest.GdcRESTApiWrapper;
import com.gooddata.integration.rest.configuration.NamePasswordConfiguration;
import com.gooddata.exception.HttpMethodException;
import org.snaplogic.cc.*;
import org.snaplogic.cc.prop.SimpleProp;
import org.snaplogic.cc.prop.SimpleProp.SimplePropType;
import org.snaplogic.common.ComponentResourceErr;
import org.snaplogic.common.exceptions.SnapComponentException;
import org.snaplogic.snapi.PropertyConstraint;
import org.snaplogic.snapi.PropertyConstraint.Type;
import org.snaplogic.snapi.ResDef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Abstract class from which other DB connection components derive. It is used
 * to encapsulate common functionality.
 *
 * @author grisha
 */
public class GoodDataConnection extends ComponentAPI {

    @Override
    public String getDescription() {
        return getLabel() + ". Non-executable component that holds information about a GoodData connection.";
    }


    @Override
    public String getLabel() {
        return "GoodData Connection (Java)";
    }

    @Override
    public String getAPIVersion() {
        return "1.0";
    }

    @Override
    public String getComponentVersion() {
        return "1.0";
    }


    // public static final String PROP_HOST = "host";
    public static final String PROP_USERNAME = "username";
    public static final String PROP_PASSWORD = "password";
    public static final String PROP_PROTOCOL = "protocol";
    public static final String PROP_HOSTNAME = "hostname";
    public static final String PROP_HOSTNAME_FTP = "ftp-hostname";

    @Override
    public Capabilities getCapabilities() {
        // Using a TreeMap ensures keys are ordered, which is good for
        // readability.
        return new Capabilities() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 7753750032102369960L;

			{
                put(Capability.INPUT_VIEW_LOWER_LIMIT, 0);
                put(Capability.INPUT_VIEW_UPPER_LIMIT, 0);
                put(Capability.INPUT_VIEW_ALLOW_BINARY, false);
                put(Capability.OUTPUT_VIEW_LOWER_LIMIT, 0);
                put(Capability.OUTPUT_VIEW_UPPER_LIMIT, 0);
                put(Capability.OUTPUT_VIEW_ALLOW_BINARY, false);
                put(Capability.ALLOW_PASS_THROUGH, false);
            }
        };
    }
    
    public static final String GOODDATA_CONNECTION_CATEGORY = "connection.gooddata";

    private static List<String> _CONNECTION_CATEGORIES = new ArrayList<String>();

    static {
        _CONNECTION_CATEGORIES.add(GOODDATA_CONNECTION_CATEGORY);
    }

    public static List<String> CONNECTION_CATEGORIES = Collections.unmodifiableList(_CONNECTION_CATEGORIES);

    @Override
    public void createResourceTemplate() {
        createGDConnectionResourceTemplate(this);
    }

    /**
     * This is refactored out of
     * {@link GoodDataConnection#createResourceTemplate()} because this has its
     * usage in the {@link GoodDataWizard} as well as in
     * {@link GoodDataConnection}. Care must be taken when implementing <A href="https://www.snaplogic.org/trac/wiki/Documentation/2.2/UserGuide/CreateComponentJava#Upgrade"
     * >upgrades</a>, however.
     * 
     * The reason it is refactored this way is that currently {@link AbstractGoodDataComponent} 
     * is intended to be an abstract implementation of components that are not connections (that
     * is, they all have a reference to the connection instead). The clearer way of doing this
     * of course, is to define one more level of inheritance - AbstractGoodDataComponent having
     * this method, and underneath, AbstractGoodDataWorkerComponent (or some such name) having
     * the connection reference. But this is not something I have time for at this point -
     * Greg grisha@snaplogic.com
     * 
     * @param comp Instance of {@link ComponentAPI} to set properties for. 
     */
    static void createGDConnectionResourceTemplate(ComponentAPI comp) {
        comp.setPropertyDef(GoodDataConnection.PROP_USERNAME, new SimpleProp("Login", SimplePropType.SnapString,
                "Username", true));
        
        PropertyConstraint passwdConstraint = new PropertyConstraint(Type.OBFUSCATE, 0);
        SimpleProp passwdProp = new SimpleProp("Password", SimplePropType.SnapString, "Password", passwdConstraint,
                true);
        comp.setPropertyDef(GoodDataConnection.PROP_PASSWORD, passwdProp);

        PropertyConstraint protocolConstraint = new PropertyConstraint(Type.LOV, new String[] { "http", "https" });
        comp.setPropertyDef(GoodDataConnection.PROP_PROTOCOL, new SimpleProp("Protocol", SimplePropType.SnapString,
                "Connection Protocol", protocolConstraint, true));
        comp.setPropertyValue(GoodDataConnection.PROP_PROTOCOL, "https");

        comp.setPropertyDef(GoodDataConnection.PROP_HOSTNAME, new SimpleProp("Hostname", SimplePropType.SnapString,
                "Hostname of GoodData server", true));
        comp.setPropertyValue(GoodDataConnection.PROP_HOSTNAME, "secure.gooddata.com");

        comp.setPropertyDef(GoodDataConnection.PROP_HOSTNAME_FTP, new SimpleProp("FTP host", SimplePropType.SnapString,
                "FTP server where to upload the data", true));
        comp.setPropertyValue(GoodDataConnection.PROP_HOSTNAME_FTP, "secure-di.gooddata.com");

        comp.setCategories(GoodDataConnection.CONNECTION_CATEGORIES, false);
    
        // Remove for production
        comp.setPropertyValue(PROP_USERNAME, "username");
        comp.setPropertyValue(PROP_PASSWORD, "password");
    }

    /**
     * Attempt to connect to the database with current properties. If current
     * properties are not parameterized and connection cannot be made, then an
     * error is set.
     *
     *
     */
    @Override
    public void validate(ComponentResourceErr err) {
        ResDef resdef = this.getResdef();
        for (String propName : resdef.listPropertyNames()) {
            Object propVal = getPropertyValue(propName);
            if (hasParam(propVal)) {
                return;
            }
        }
        try {
            login(resdef);
        } catch (Exception e) {
            String msg = "Connection error: " + e.getMessage();
            err.setMessage(msg);
            elog(e);
        }
    }

    @Override
    public void execute(Map<String, InputView> inputViews, Map<String, OutputView> outputViews) {

    }

    public static GdcRESTApiWrapper login(ResDef goodDataCon) throws HttpMethodException {
        return login(goodDataCon, null);
    }

    public static GdcRESTApiWrapper login(ResDef goodDataCon, ComponentAPI comp) throws HttpMethodException {
        if (!goodDataCon.getComponentName().equals(GoodDataConnection.class.getName())) {
            throw new SnapComponentException("Incorrect ResDef for this operation: " + goodDataCon.getComponentName());
        }

        NamePasswordConfiguration config = getHttpConfiguration(goodDataCon);
        GdcRESTApiWrapper restApi = new GdcRESTApiWrapper(config);
        restApi.login();
        if (comp != null) {
            comp.debug("Logged in, token: %s", restApi.getToken());
        }

        return restApi;
    }
    
    public static GdcDataTransferAPI getFtpWrapper(ResDef goodDataCon, ComponentAPI comp) throws HttpMethodException {
        if (!goodDataCon.getComponentName().equals(GoodDataConnection.class.getName())) {
            throw new SnapComponentException("Incorrect ResDef for this operation: " + goodDataCon.getComponentName());
        }

        NamePasswordConfiguration config = getFtpConfiguration(goodDataCon);
        GdcDataTransferAPI restApi = new GdcFTPApiWrapper(config);
        return restApi;
    }
    
    public static NamePasswordConfiguration getFtpConfiguration(ResDef goodDataCon) {
        String username = (String) goodDataCon.getPropertyValue(PROP_USERNAME);
        String passwd = (String) goodDataCon.getPropertyValue(PROP_PASSWORD);
        String protocol = (String) goodDataCon.getPropertyValue(PROP_PROTOCOL);
        String hostname = (String) goodDataCon.getPropertyValue(PROP_HOSTNAME_FTP);
        NamePasswordConfiguration config = new NamePasswordConfiguration(protocol, hostname, username, passwd);
        return config;
    }
    
    public static NamePasswordConfiguration getHttpConfiguration(ResDef goodDataCon) {
        String username = (String) goodDataCon.getPropertyValue(PROP_USERNAME);
        String passwd = (String) goodDataCon.getPropertyValue(PROP_PASSWORD);
        String protocol = (String) goodDataCon.getPropertyValue(PROP_PROTOCOL);
        String hostname = (String) goodDataCon.getPropertyValue(PROP_HOSTNAME);
        NamePasswordConfiguration config = new NamePasswordConfiguration(protocol, hostname, username, passwd);
        return config;
    }
    
}
