package com.gooddata.processor;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/**
 * Command.
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

}
