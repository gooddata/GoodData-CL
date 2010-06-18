package org.gooddata.processor;

import com.gooddata.integration.rest.configuration.NamePasswordConfiguration;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * GoodData CLI parameters wrapper
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class CliParams extends HashMap<String,String> {


    private NamePasswordConfiguration httpConfig = null;
    private NamePasswordConfiguration ftpConfig = null;

    private static Logger l = Logger.getLogger(CliParams.class);

    /**
     * HTTP config getter
     * @return HTTP config
     */
    public NamePasswordConfiguration getHttpConfig() {
        return httpConfig;
    }

    /**
     * HTTP config setter
     * @param httpConfig HTTP config
     */
    public void setHttpConfig(NamePasswordConfiguration httpConfig) {
        this.httpConfig = httpConfig;
    }

    /**
     * FTP config getter
     * @return FTP config
     */
    public NamePasswordConfiguration getFtpConfig() {
        return ftpConfig;
    }
    /**
     * FTP config setter
     * @param ftpConfig FTP config
     */
    public void setFtpConfig(NamePasswordConfiguration ftpConfig) {
        this.ftpConfig = ftpConfig;
    }
}
