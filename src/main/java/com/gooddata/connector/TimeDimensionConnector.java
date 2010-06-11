package com.gooddata.connector;

import com.gooddata.exception.ModelException;
import com.gooddata.util.StringUtil;
import org.apache.log4j.Logger;
import org.gooddata.connector.AbstractConnector;
import org.gooddata.connector.Connector;

import java.io.IOException;

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