package org.snaplogic.snap.gooddata;

import java.util.Map;

import org.snaplogic.cc.ComponentAPI;
import org.snaplogic.cc.InputView;
import org.snaplogic.cc.OutputView;
import org.snaplogic.common.ComponentResourceErr;
import org.snaplogic.common.exceptions.SnapComponentException;
import org.snaplogic.snapi.ResDef;

import com.gooddata.exception.GdcLoginException;
import com.gooddata.integration.ftp.GdcFTPApiWrapper;
import com.gooddata.integration.rest.GdcRESTApiWrapper;

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
