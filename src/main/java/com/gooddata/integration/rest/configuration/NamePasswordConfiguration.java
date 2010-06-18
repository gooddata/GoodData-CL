package com.gooddata.integration.rest.configuration;

import java.net.MalformedURLException;
import java.net.URL;

/** Credentials configuration for the GoodData REST and FTP APIs
 * 
 * @author Jiri Zaloudek
 * @version 1.0
 */
public class NamePasswordConfiguration {

    /**
     * default GDC host
     */
    public static final String DEFAULT_GDC_HOST = "secure.gooddata.com";
    /**
     * default Gdc protocol
     */
    public static final String DEFAULT_GCD_PROTO = "https";

    // GDC protocol
    private String protocol;
    // GDC host
    private String gdcHost;
    // GDC username
    private String username;
    // GDC password
    private String password;

    /**
     * Constructor
     * @param username GoodData username
     * @param password GoodData password
     */
    public NamePasswordConfiguration(String username, String password) {
        this(DEFAULT_GCD_PROTO, DEFAULT_GDC_HOST, username, password);

    }

    /**
     * Constructor
     * @param protocol GoodData protocol (HTTP | FTP)
     * @param gdcHost GoodData host (e.g. secure.gooddata.com)
     * @param username GoodData username
     * @param password GoodData password
     */
    public NamePasswordConfiguration(String protocol, String gdcHost, String username, String password) {
        super();
        this.protocol = protocol;
        this.gdcHost = gdcHost;
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the GoodData server URL
     * @return GoodData server URL
     */
    public String getUrl() {
        try {
            URL url = new URL(protocol, gdcHost, "");
            return url.toString();
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    /**
     * GoodData host getter
     * @return GoodData host
     */
    public String getGdcHost() {
        return gdcHost;
    }

    /**
     * GoodData username getter
     * @return GoodData username
     */
    public String getUsername() {
        return username;
    }

    /**
     * GoodData password getter
     * @return GoodData password
     */
    public String getPassword() {
        return password;
    }

}
