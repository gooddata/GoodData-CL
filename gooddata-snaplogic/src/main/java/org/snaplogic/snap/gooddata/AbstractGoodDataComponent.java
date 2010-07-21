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

import com.gooddata.exception.GdcLoginException;
import com.gooddata.integration.ftp.GdcFTPApiWrapper;
import com.gooddata.integration.model.Project;
import com.gooddata.integration.rest.GdcRESTApiWrapper;
import org.snaplogic.cc.ComponentAPI;
import org.snaplogic.cc.InputView;
import org.snaplogic.cc.OutputView;
import org.snaplogic.cc.prop.SimpleProp;
import org.snaplogic.cc.prop.SimpleProp.SimplePropType;
import org.snaplogic.common.ComponentResourceErr;
import org.snaplogic.common.exceptions.SnapComponentException;
import org.snaplogic.snapi.PropertyConstraint;
import org.snaplogic.snapi.ResDef;
import org.snaplogic.snapi.PropertyConstraint.Type;

import java.util.Map;

public abstract class AbstractGoodDataComponent extends ComponentAPI {

    @Override
    public void execute(Map<String, InputView> inputViews, Map<String, OutputView> outputViews) {
        // TODO Auto-generated method stub

    }

    public static final String GOODDATA_CONNECTION_REF = "gooddata_connection_ref";



    @Override
    public void createResourceTemplate() {
        super.createResourceTemplate();
        addResourceRefDef(GOODDATA_CONNECTION_REF, "GoodData connection", GoodDataConnection.CONNECTION_CATEGORIES, true);
    }


    protected GdcRESTApiWrapper login() {
        return login(null);
    }
    
    protected GdcFTPApiWrapper ftpLogin() {
        return ftpLogin(null);
    }

    protected GdcRESTApiWrapper login(ComponentResourceErr err) {
        ResDef conResDef = getReferencedResDef(GOODDATA_CONNECTION_REF);
        if (conResDef == null) {
            if (err == null) {
                throw new SnapComponentException("Could not find connection reference");
            } else {
                err.getResourceRefErr(GOODDATA_CONNECTION_REF).setMessage("Connection reference not found");
                return null;
            }
        }
        try {

            GdcRESTApiWrapper restApi = GoodDataConnection.login(conResDef, this);
            return restApi;
        } catch (GdcLoginException gdcle) {
            elog(gdcle);
            if (err == null) {
                throw new SnapComponentException(gdcle);
            } else {
                err.getResourceRefErr(GOODDATA_CONNECTION_REF).setMessage("Could not validate connection reference: %s", gdcle);
            }
        }
        return null;
    }
    
    protected GdcFTPApiWrapper ftpLogin(ComponentResourceErr err) {
        ResDef conResDef = getReferencedResDef(GOODDATA_CONNECTION_REF);
        if (conResDef == null) {
            if (err == null) {
                throw new SnapComponentException("Could not find connection reference");
            } else {
                err.getResourceRefErr(GOODDATA_CONNECTION_REF).setMessage("Connection reference not found");
                return null;
            }
        }
        try {

            GdcFTPApiWrapper ftpApi = GoodDataConnection.getFtpWrapper(conResDef, this);
            return ftpApi;
        } catch (GdcLoginException gdcle) {
            elog(gdcle);
            if (err == null) {
                throw new SnapComponentException(gdcle);
            } else {
                err.getResourceRefErr(GOODDATA_CONNECTION_REF).setMessage("Could not validate connection reference: %s", gdcle);
            }
        }
        return null;
    }
}
