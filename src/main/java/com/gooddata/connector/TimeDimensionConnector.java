package com.gooddata.connector;

import au.com.bytecode.opencsv.CSVWriter;
import com.gooddata.exceptions.InitializationException;
import com.gooddata.exceptions.InvalidArgumentException;
import com.gooddata.exceptions.MetadataFormatException;
import com.gooddata.exceptions.ModelException;
import com.gooddata.google.analytics.FeedDumper;
import com.gooddata.google.analytics.GaQuery;
import com.gooddata.modeling.generator.MaqlGenerator;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.util.FileUtil;
import com.gooddata.util.StringUtil;
import com.google.gdata.client.analytics.AnalyticsService;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import org.apache.log4j.Logger;
import org.gooddata.connector.AbstractConnector;
import org.gooddata.connector.Connector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * GoodData Google Analytics Connector
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class TimeDimensionConnector extends AbstractConnector implements Connector {

    private static Logger l = Logger.getLogger(TimeDimensionConnector.class);

    private String ctx;

    /**
     * Creates a new Time Dimension Connector
     * @param context time dimension context
     */
    protected TimeDimensionConnector(String context) {
        ctx = context;
    }

    /**
     * Creates a new Time Dimension Connector
     * @param context time dimension context
     * @return new Time Dimension Connector
     */
    public static TimeDimensionConnector createConnector(String context) {
        return new TimeDimensionConnector(context);
    }

    @Override
    public void extract() throws ModelException, IOException {
        //EMPTY
    }

    /**
     * Generates the MAQL for the data source
     * @return the MAQL in string format
     */
    public String generateMaql() {
        if(ctx != null && ctx.trim().length()>0) {
            String idp = StringUtil.formatShortName(ctx);
            String ts = StringUtil.formatLongName(ctx);
            return "INCLUDE TEMPLATE \"URN:GOODDATA:DATE\" MODIFY (IDENTIFIER \""+idp+"\", TITLE \""+ts+"\");";
        }
        else {
            return "INCLUDE TEMPLATE \"URN:GOODDATA:DATE\"";            
        }
    }
}