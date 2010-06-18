package org.gooddata.processor;

import com.gooddata.exception.GdcLoginException;
import com.gooddata.exception.InvalidParameterException;
import com.gooddata.integration.ftp.GdcFTPApiWrapper;
import com.gooddata.integration.rest.GdcRESTApiWrapper;
import com.gooddata.integration.rest.configuration.NamePasswordConfiguration;
import org.apache.log4j.Logger;
import org.gooddata.connector.Connector;

/**
 * GoodData
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class ProcessingContext {

    private static Logger l = Logger.getLogger(ProcessingContext.class);

    private String projectId;
    private Connector connector;
    private GdcRESTApiWrapper _restApi = null;
    private GdcFTPApiWrapper _ftpApi = null;


    public String getProjectId() throws InvalidParameterException {
        if(projectId == null || projectId.length() <= 0)
            throw new InvalidParameterException("No project is active. Please activate project via CreateProject or " +
                    "OpenProject command. ");
        else
            return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public GdcRESTApiWrapper getRestApi(CliParams cliParams) throws GdcLoginException {
    	if (_restApi == null) {
            NamePasswordConfiguration httpConfig = cliParams.getHttpConfig();
            l.debug("Using the GoodData HTTP host '" + httpConfig.getGdcHost() + "'.");
            _restApi = new GdcRESTApiWrapper(httpConfig);
            _restApi.login();
    	}
    	return _restApi;
    }

    public GdcFTPApiWrapper getFtpApi(CliParams cliParams) {
    	if (_ftpApi == null) {
            NamePasswordConfiguration ftpConfig = cliParams.getFtpConfig();
	        l.debug("Using the GoodData FTP host '" + ftpConfig.getGdcHost() + "'.");
	        _ftpApi = new GdcFTPApiWrapper(ftpConfig);
    	}
    	return _ftpApi;
    }

}
