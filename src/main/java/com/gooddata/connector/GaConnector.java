package com.gooddata.connector;

import au.com.bytecode.opencsv.CSVWriter;
import com.gooddata.exception.InitializationException;
import com.gooddata.exception.InvalidArgumentException;
import com.gooddata.exception.MetadataFormatException;
import com.gooddata.exception.ModelException;
import com.gooddata.google.analytics.FeedDumper;
import com.gooddata.google.analytics.GaQuery;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.util.FileUtil;
import com.google.gdata.client.analytics.AnalyticsService;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import org.apache.log4j.Logger;
import org.gooddata.connector.AbstractConnector;
import org.gooddata.connector.Connector;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * GoodData Google Analytics Connector
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class GaConnector extends AbstractConnector implements Connector {

    public static final String GA_DATE = "ga:date";
    
    private static Logger l = Logger.getLogger(GaConnector.class);

    private static final String APP_NAME = "gdc-ga-client";

    private String googleAnalyticsUsername;
    private String googleAnalyticsPassword;
    private GaQuery googleAnalyticsQuery;

    /**
     * Creates a new Google Analytics Connector
     * @param projectId project id
     * @param configFileName configuration file 
     * @param gUsr Google Analytics User
     * @param gPsw Google Analytics Password
     * @param gId Google Analytics profileId
     * @param gQuery Google Analytics query
     * @param connectorBackend connector backend
     * @param username database backend username
     * @param password database backend password
     * @throws InitializationException
     * @throws MetadataFormatException
     * @throws IOException
     */
    protected GaConnector(String projectId, String configFileName, String gUsr, String gPsw, String gId, GaQuery gQuery,
                          int connectorBackend, String username, String password)
            throws InitializationException,
            MetadataFormatException, IOException, ModelException {
        super(projectId, configFileName, connectorBackend, username, password);
        gQuery.setIds(gId);
        setGoogleAnalyticsUsername(gUsr);
        setGoogleAnalyticsPassword(gPsw);
        setGoogleAnalyticsQuery(gQuery);
    }

     /**
     * Creates a new Google Analytics Connector
     * @param projectId project id
     * @param configFileName configuration file
     * @param gUsr Google Analytics User
     * @param gPsw Google Analytics Password
     * @param gId Google Analytics profileId
     * @param gQuery Google Analytics query
     * @param connectorBackend connector backend
     * @param username database backend username
     * @param password database backend password
     * @return new Google Analytics Connector
     * @throws InitializationException
     * @throws MetadataFormatException
     * @throws IOException
     */
    public static GaConnector createConnector(String projectId, String configFileName, String gUsr, String gPsw,
                                String gId, GaQuery gQuery, int connectorBackend, String username, String password)
                                throws InitializationException, MetadataFormatException,
             IOException, ModelException {
        return new GaConnector(projectId, configFileName, gUsr, gPsw, gId, gQuery, connectorBackend, username, password);
    }

    /**
     * Saves a template of the config file
     * @param name the new config file name 
     * @param configFileName the new config file name
     * @param gQuery the Google Analytics query
     * @throws com.gooddata.exception.InvalidArgumentException if there is a problem with arguments
     * @throws IOException if there is a problem with writing the config file
     */
    public static void saveConfigTemplate(String name, String configFileName, GaQuery gQuery)
            throws InvalidArgumentException, IOException {
        String dims = gQuery.getDimensions();
        String mtrs = gQuery.getMetrics();
        SourceSchema s = SourceSchema.createSchema(name);
        if(dims != null && dims.length() > 0) {
            String[] dimensions = dims.split("\\|");
            for(String dim : dimensions) {
                // remove the "ga:"
                if(dim != null && dim.length() > 3) {
                    String d= dim.substring(3);
                    if(GA_DATE.equals(dim)) {
                        SourceColumn sc = new SourceColumn(d,SourceColumn.LDM_TYPE_DATE, d);
                        sc.setFormat("yyyy-MM-dd");
                        s.addColumn(sc);
                    }
                    else {
                        SourceColumn sc = new SourceColumn(d,SourceColumn.LDM_TYPE_ATTRIBUTE, d);
                        s.addColumn(sc);
                    }
                }
                else {
                    l.error("Invalid dimension name '" + dim + "'");
                    throw new InvalidArgumentException("Invalid dimension name '" + dim + "'");
                }
            }
        }
        else {
            l.error("Please specify Google Analytics dimensions separated by comma.");
            throw new InvalidArgumentException("Please specify Google Analytics dimensions separated by comma.");            
        }
        if(mtrs != null && mtrs.length() > 0) {
            String[] metrics = mtrs.split("\\|");
            for(String mtr : metrics) {
                // remove the "ga:"
                if(mtr != null && mtr.length() > 3) {
                    String m= mtr.substring(3);
                    SourceColumn sc = new SourceColumn(m,SourceColumn.LDM_TYPE_FACT, m);
                    s.addColumn(sc);
                }
                else {
                    l.error("Invalid dimension name '" + mtr + "'");
                    throw new InvalidArgumentException("Invalid metric name '" + mtr + "'");
                }
            }
        }
        else {
            l.error("Please specify Google Analytics metrics separated by comma.");
            throw new InvalidArgumentException("Please specify Google Analytics metrics separated by comma.");
        }
        s.writeConfig(new File(configFileName));
    }

    /**
     * Extracts the source data CSV to the Derby database where it is going to be transformed
     * @throws ModelException in case of PDM schema issues
     */
    public void extract() throws ModelException, IOException {
        Connection con = null;
        try {
            AnalyticsService as = new AnalyticsService(APP_NAME);
		    as.setUserCredentials(getGoogleAnalyticsUsername(), getGoogleAnalyticsPassword());
            File dataFile = FileUtil.getTempFile();
            GaQuery gaq = getGoogleAnalyticsQuery();
            gaq.setMaxResults(5000);
            int cnt = 1;
            CSVWriter cw = new CSVWriter(new FileWriter(dataFile));
            for(int startIndex = 1; cnt > 0; startIndex += cnt + 1) {
                gaq.setStartIndex(startIndex);
                DataFeed feed = as.getFeed(gaq.getUrl(), DataFeed.class);
                l.trace("Retrieving GA data from index="+startIndex);
                cnt = FeedDumper.dump(cw, feed);
                l.trace("Retrieved "+cnt+" entries.");
            }
            cw.flush();
            cw.close();
            getConnectorBackend().extract(dataFile);
            FileUtil.recursiveDelete(dataFile);
        }
        catch (IOException e) {
            throw new InternalError(e.getMessage());
        }
        catch (AuthenticationException e) {
            throw new InternalError(e.getMessage());
        } catch (ServiceException e) {
            throw new InternalError(e.getMessage());
        } finally {
            try {
                if (con != null && !con.isClosed())
                    con.close();
            }
            catch (SQLException e) {
                throw new InternalError(e.getMessage());
            }
        }
    }

    /**
     * Google Analytics username getter
     * @return Google Analytics username
     */
    public String getGoogleAnalyticsUsername() {
        return googleAnalyticsUsername;
    }

    /**
     * Google Analytics username setter
     * @param googleAnalyticsUsername Google Analytics username
     */
    public void setGoogleAnalyticsUsername(String googleAnalyticsUsername) {
        this.googleAnalyticsUsername = googleAnalyticsUsername;
    }

    /**
     * Google Analytics password getter
     * @return Google Analytics password
     */
    public String getGoogleAnalyticsPassword() {
        return googleAnalyticsPassword;
    }

    /**
     * Google Analytics password setter
     * @param googleAnalyticsPassword Google Analytics password
     */
    public void setGoogleAnalyticsPassword(String googleAnalyticsPassword) {
        this.googleAnalyticsPassword = googleAnalyticsPassword;
    }

    /**
     * Google Analytics query getter
     * @return Google Analytics query
     */
    public GaQuery getGoogleAnalyticsQuery() {
        return googleAnalyticsQuery;
    }

    /**
     * Google Analytics query setter
     * @param googleAnalyticsQuery Google Analytics query
     */
    public void setGoogleAnalyticsQuery(GaQuery googleAnalyticsQuery) {
        this.googleAnalyticsQuery = googleAnalyticsQuery;
    }
}
