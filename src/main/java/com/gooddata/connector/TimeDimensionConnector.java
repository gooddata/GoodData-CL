package com.gooddata.connector;

import com.gooddata.exception.*;
import org.gooddata.connector.processor.CliParams;
import org.gooddata.connector.processor.Command;
import com.gooddata.util.StringUtil;
import org.apache.log4j.Logger;
import org.gooddata.connector.AbstractConnector;
import org.gooddata.connector.Connector;
import org.gooddata.connector.processor.ProcessingContext;

import java.io.IOException;

/**
 * GoodData Google Analytics Connector
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class TimeDimensionConnector extends AbstractConnector implements Connector {

    private static Logger l = Logger.getLogger(TimeDimensionConnector.class);

    //Time dimension context (e.g. created, closed etc.)
    private String ctx;

    /**
     * Creates a new Time Dimension Connector
     */
    protected TimeDimensionConnector() {
    }

    /**
     * Creates a new Time Dimension Connector
     * @return new Time Dimension Connector
     */
    public static TimeDimensionConnector createConnector() {
        return new TimeDimensionConnector();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void extract() throws ModelException, IOException {
        //EMPTY
    }

    /**
     * {@inheritDoc}
     */
    public String generateMaql() {
        l.debug("Generating time dimension MAQL with context "+ctx);
        if(ctx != null && ctx.trim().length()>0) {
            String idp = StringUtil.formatShortName(ctx);
            String ts = StringUtil.formatLongName(ctx);
            l.debug("Generated time dimension MAQL with context "+ctx);
            return "INCLUDE TEMPLATE \"URN:GOODDATA:DATE\" MODIFY (IDENTIFIER \""+idp+"\", TITLE \""+ts+"\");";
        }
        else {
            l.debug("Generated time dimension MAQL with no context ");
            return "INCLUDE TEMPLATE \"URN:GOODDATA:DATE\"";            
        }

    }

    /**
     * {@inheritDoc}
     */
    public boolean processCommand(Command c, CliParams cli, ProcessingContext ctx) throws ProcessingException {
        l.debug("Processing command "+c.getCommand());
        try {
            if(c.match("LoadTimeDimension")) {
                loadTD(c, cli, ctx);
            }
            else {
                l.debug("No match passing the command "+c.getCommand()+" further.");
                return super.processCommand(c, cli, ctx);
            }
        }
        catch (IOException e) {
            throw new ProcessingException(e);
        }
        l.debug("Processed command "+c.getCommand());
        return true;
    }

    /**
     * Loads TimeDimension data command processor
     * @param c command
     * @param p command line arguments
     * @param ctx current processing context
     * @throws IOException in case of IO issues
     */
    private void loadTD(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        String ct = "";
        if(c.checkParam("context"))
            ct = c.getParam( "context");
        this.ctx = ct;
        // sets the current connector
        ctx.setConnector(this);
    }
}