package com.gooddata.processor;

import com.gooddata.exception.InvalidArgumentException;
import com.gooddata.exception.InvalidParameterException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/**
 * Script command wrapper
 *
 * @author Zdenek Svoboda <zd@gooddata.org>
 * @version 1.0
 */
public class Command {

    private static Logger l = Logger.getLogger(Command.class);

    // Command
    private String command;
    // Command parameters
    private Properties parameters;


    /**
     * Constructor
     *
     * @param command command name
     */
    public Command(String command) {
        this.command = command;
    }

    /**
     * Constructor
     * 
     * @param command command name
     * @param params parameters
     */
    public Command(String command, Properties params) {
        this(command);
        this.parameters = params;
    }

    /**
     * Constructor
     *
     * @param command command name
     * @param params parameters
     */
    public Command(String command, String params) {
        this(command);
        this.parameters = new Properties();
        try {
            this.parameters.load(new StringReader(params.replace(",","\n")));
        }
        catch (IOException e) {
            l.error("Error extracting command parameters.",e);
        }
    }

    /**
     * Command getter
     * @return command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Command setter
     * @param command command
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * Paremeters getter
     * @return parameters
     */
    public Properties getParameters() {
        return parameters;
    }

    /**
     * Parameters setter
     * @param parameters command parameters
     */
    public void setParameters(Properties parameters) {
        this.parameters = parameters;
    }
    
    @Override
    public String toString() {
    	return new StringBuffer(command).append("(").append(parameters).append(")").toString();
    }

    /**
     * Returns true if the command matches the command name
     * @param cms command name
     * @return returns true if the command matches the command name, false otherwise
     */
    public boolean match(String cms) {
        if(this.getCommand().equalsIgnoreCase(cms))
            return true;
        else
            return false;
    }

    /**
     * Returns the command's mandatory parameter value
     * @param p parameter name
     * @return the parameter value
     * @throws InvalidParameterException if the parameter doesn't exist
     */
    public String getParamMandatory( String p) throws InvalidParameterException {
        String v = (String)this.getParameters().get(p);
        if(v == null || v.length() == 0) {
            throw new InvalidParameterException(this.getCommand() + ": Command parameter '"+p+"' is required.");
        }
        return v;
    }

    /**
     * Returns the parameter value
     * @param p parameter name
     * @return parameter value
     */
    public String getParam(String p) {
        return (String)this.getParameters().get(p);
    }

    /**
     * Checks if the parameter exists
     * @param p parameter name
     * @return true if the parameter exists, false othewrwise
     */
    public boolean checkParam(String p) {
        String v = (String)this.getParameters().get(p);
        if(v == null || v.length() == 0) {
            return false;
        }
        return true;
    }

}
