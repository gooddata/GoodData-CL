/*
 * Copyright (c) 2009, GoodData Corporation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice, this list of conditions and
 *        the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *        and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *     * Neither the name of the GoodData Corporation nor the names of its contributors may be used to endorse
 *        or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.gooddata.processor;

import com.gooddata.exception.InvalidCommandException;
import com.gooddata.exception.InvalidParameterException;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
            this.parameters.load(new ByteArrayInputStream(params.replace(",","\n").getBytes()));
        }
        catch (IOException e) {
            l.debug("Error extracting command parameters.",e);
            throw new InvalidCommandException("Error extracting command parameters.",e);
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
        return this.getCommand().equalsIgnoreCase(cms);
    }

    /**
     * Returns the command's mandatory parameter value
     * @param p parameter name
     * @return the parameter value
     * @throws InvalidParameterException if the parameter doesn't exist
     */
    public String getParamMandatory( String p) throws InvalidParameterException {
        String v = (String)this.getParameters().remove(p);
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
        return (String)this.getParameters().remove(p);
    }

    /**
     * Checks if the parameter exists
     * @param p parameter name
     * @return true if the parameter exists, false othewrwise
     */
    public boolean checkParam(String p) {
        String v = (String)this.getParameters().get(p);
        return (v != null && v.length() > 0);
    }

    /**
     * Checks if there are no extra parameters present
     * Should be called once all valid parameters are processed
     * @throws InvalidParameterException if there are unprocessed parameters
     */
    public void paramsProcessed() {
        if (!this.getParameters().isEmpty()) {
            throw new InvalidParameterException(this.getCommand() + ": Extra parameters: "
                + this.getParameters().toString());
        }
    }

}
