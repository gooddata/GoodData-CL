/*
 * Copyright (c) 2009 GoodData Corporation.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Redistributions in any form must be accompanied by information on
 *    how to obtain complete source code for this software and any
 *    accompanying software that uses this software.  The source code
 *    must either be included in the distribution or be available for no
 *    more than the cost of distribution plus a nominal fee, and must be
 *    freely redistributable under reasonable conditions.  For an
 *    executable file, complete source code means the source code for all
 *    modules it contains.  It does not include source code for modules or
 *    files that typically accompany the major components of the operating
 *    system on which the executable file runs.
 *
 * THIS SOFTWARE IS PROVIDED BY GOODDATA ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT, ARE DISCLAIMED.  IN NO EVENT SHALL ORACLE BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.gooddata.processor;

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
