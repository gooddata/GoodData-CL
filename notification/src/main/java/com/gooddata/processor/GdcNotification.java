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

import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.gooddata.config.NotificationConfig;
import com.gooddata.config.NotificationMessage;
import com.sforce.ws.ConnectionException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.gooddata.exception.GdcException;
import com.gooddata.exception.GdcLoginException;
import com.gooddata.exception.GdcRestApiException;
import com.gooddata.exception.HttpMethodException;
import com.gooddata.exception.InternalErrorException;
import com.gooddata.exception.InvalidArgumentException;
import com.gooddata.exception.SfdcException;
import com.gooddata.integration.rest.configuration.NamePasswordConfiguration;
import com.gooddata.util.FileUtil;

/**
 * The GoodData Data Integration CLI processor.
 *
 * @author jiri.zaloudek
 * @author Zdenek Svoboda <zd@gooddata.org>
 * @version 1.0
 */
public class GdcNotification {

    private static Logger l = Logger.getLogger(GdcNotification.class);

    //Options data
    public static String[] CLI_PARAM_GDC_USERNAME = {"username","u"};
    public static String[] CLI_PARAM_GDC_PASSWORD = {"password","p"};

    public static String[] CLI_PARAM_SFDC_USERNAME = {"sfdcusername","d"};
    public static String[] CLI_PARAM_SFDC_PASSWORD = {"sfdcpassword","c"};

    public static String[] CLI_PARAM_GDC_HOST = {"host","h"};
    public static String[] CLI_PARAM_PROJECT = {"project","i"};
    public static String[] CLI_PARAM_PROTO = {"proto","t"};
    public static String[] CLI_PARAM_VERSION = {"version","V"};
    public static String CLI_PARAM_CONFIG = "config";

    private static String DEFAULT_PROPERTIES = "gdi.properties";

    // mandatory options
    public static Option[] mandatoryOptions = { };

    // optional options
    public static Option[] optionalOptions = {
        new Option(CLI_PARAM_GDC_USERNAME[1], CLI_PARAM_GDC_USERNAME[0], true, "GoodData username"),
        new Option(CLI_PARAM_GDC_PASSWORD[1], CLI_PARAM_GDC_PASSWORD[0], true, "GoodData password"),
        new Option(CLI_PARAM_SFDC_USERNAME[1], CLI_PARAM_SFDC_USERNAME[0], true, "Salesforce username"),
        new Option(CLI_PARAM_SFDC_PASSWORD[1], CLI_PARAM_SFDC_PASSWORD[0], true, "Salesforce password"),
        new Option(CLI_PARAM_GDC_HOST[1], CLI_PARAM_GDC_HOST[0], true, "GoodData host"),
        new Option(CLI_PARAM_PROJECT[1], CLI_PARAM_PROJECT[0], true, "GoodData project identifier (a string like nszfbgkr75otujmc4smtl6rf5pnmz9yl)"),
        new Option(CLI_PARAM_PROTO[1], CLI_PARAM_PROTO[0], true, "HTTP or HTTPS (deprecated)"),
        new Option(CLI_PARAM_VERSION[1], CLI_PARAM_VERSION[0], false, "Prints the tool version."),
    };

    private CliParams cliParams = null;
    private boolean finishedSucessfuly = false;

    private final static String BUILD_NUMBER = "";

    private GdcNotification(CommandLine ln, Properties defaults) {
        try {
            cliParams = parse(ln, defaults);
            cliParams.setHttpConfig(new NamePasswordConfiguration("https",
                    cliParams.get(CLI_PARAM_GDC_HOST[0]),
                    cliParams.get(CLI_PARAM_GDC_USERNAME[0]), cliParams.get(CLI_PARAM_GDC_PASSWORD[0])));
            String config = cliParams.get(CLI_PARAM_CONFIG);
            if(config != null && config.length()>0) {
                execute(config);
            }
            else {
                l.error("No config file given.");
                commandsHelp();
                System.exit(1);
            }
            finishedSucessfuly = true;
        }
        catch (ConnectionException e) {
            l.error("Can't connect to SFDC: "+e.getMessage());
            Throwable c = e.getCause();
            while(c!=null) {
                l.error("Caused by: "+c.getMessage());
                c = c.getCause();
            }
            l.debug("Can't connect to SFDC:",e);
        }
        catch (InvalidArgumentException e) {
            l.error("Invalid command line argument: "+e.getMessage());
            Throwable c = e.getCause();
            while(c!=null) {
                l.error("Caused by: "+c.getMessage());
                c = c.getCause();
            }
            l.debug("Invalid command line argument:",e);
            l.info(commandsHelp());
        }
        catch (SfdcException e) {
            l.error("Error communicating with SalesForce: "+e.getMessage());
            Throwable c = e.getCause();
            while(c!=null) {
                l.error("Caused by: "+c.getMessage());
                c = c.getCause();
            }
            l.debug("Error communicating with SalesForce.",e);
        }
        catch (GdcLoginException e) {
            l.error("Error logging to GoodData. Please check your GoodData username and password: "+e.getMessage());
            Throwable c = e.getCause();
            while(c!=null) {
                l.error("Caused by: "+c.getMessage());
                c = c.getCause();
            }
            l.debug("Error logging to GoodData. Please check your GoodData username and password.",e);
        }
        /*
        catch (IOException e) {
            l.error("Encountered an IO problem. Please check that all files that you use in your command line arguments and commands exist. More info: '"+e.getMessage()+"'");
            Throwable c = e.getCause();
            while(c!=null) {
                l.error("Caused by: "+c.getMessage());
                c = c.getCause();
            }
            l.debug("Encountered an IO problem. Please check that all files that you use in your command line arguments and commands exist. More info: '"+e.getMessage()+"'",e);
        }
        */
        catch (InternalErrorException e) {
            Throwable c = e.getCause();
            if( c != null && c instanceof SQLException) {
                l.error("Error extracting data. Can't process the incoming data. Please check the CSV file " +
                        "separator and consistency (same number of columns in each row). Also, please make sure " +
                        "that the number of columns in your XML config file matches the number of rows in your " +
                        "data source. Make sure that your file is readable by other users (particularly the mysql user). " +
                        "More info: '"+c.getMessage()+"'");
                l.debug("Error extracting data. Can't process the incoming data. Please check the CSV file " +
                        "separator and consistency (same number of columns in each row). Also, please make sure " +
                        "that the number of columns in your XML config file matches the number of rows in your " +
                        "data source. Make sure that your file is readable by other users (particularly the mysql user). " +
                        "More info: '"+c.getMessage()+"'",c);
            }
            else {
                l.error("Internal error: "+e.getMessage());
                c = e.getCause();
                while(c!=null) {
                    l.error("Caused by: "+c.getMessage());
                    c = c.getCause();
                }
                l.debug("REST API invocation error: ",e);
            }
        }
        catch (HttpMethodException e) {
            l.error("Error executing GoodData REST API: "+e.getMessage());
            Throwable c = e.getCause();
            while(c!=null) {
                l.error("Caused by: "+c.getMessage());
                c = c.getCause();
            }
            l.debug("Error executing GoodData REST API.",e);
        }
        catch (GdcRestApiException e) {
            l.error("REST API invocation error: "+e.getMessage());
            Throwable c = e.getCause();
            while(c!=null) {
                if(c instanceof HttpMethodException) {
                    HttpMethodException ex = (HttpMethodException)c;
                    String msg = ex.getMessage();
                    if(msg != null && msg.length()>0 && msg.indexOf("/ldm/manage")>0) {
                        l.error("Error creating/updating logical data model (executing MAQL DDL).");
                        if(msg.indexOf(".date")>0) {
                            l.error("Bad time dimension schemaReference.");
                        }
                        else {
                           l.error("You are either trying to create a data object that already exists " +
                                   "(executing the same MAQL multiple times) or providing a wrong reference " +
                                   "or schemaReference in your XML configuration.");
                        }
                    }
                }
                l.error("Caused by: "+c.getMessage());
                c = c.getCause();
            }
            l.debug("REST API invocation error: ", e);
        }
        catch (GdcException e) {
            l.error("Unrecognized error: "+e.getMessage());
            Throwable c = e.getCause();
            while(c!=null) {
                l.error("Caused by: "+c.getMessage());
                c = c.getCause();
            }
            l.debug("Unrecognized error: ",e);
        }
    }

    private boolean processMessage(NotificationMessage m) {
        

    }

    private void execute(String config) throws ConnectionException, IOException {

        NotificationConfig c = NotificationConfig.fromXml(new File(config));

        for(NotificationMessage m : c.getMessages()) {
            
        }

        /*
        GdcRESTApiWrapper rest = new GdcRESTApiWrapper(cliParams.getHttpConfig());
        rest.login();
        String projectId = cliParams.get(CLI_PARAM_PROJECT);
        String metricUri = "/gdc/md/uikbr0t694tnh3uje22yedukbyzyt30o/obj/1177";
        double val = rest.computeMetric(metricUri);
        System.out.println("VALUE is "+val);

        
        */

        String exp = "(M1 + 2*M2) == 0";

        Expression e = null;
        try {
            e = ExpressionFactory.createExpression(exp);
            JexlContext jc = JexlHelper.createContext();
            Map<String, Number> vars = new HashMap<String, Number>();
            vars.put("M1", new Float(16));
            vars.put("M2", new Float(10));
            jc.setVars(vars);

            Object result = (Object)e.evaluate(jc);
            System.err.println("RESULT is "+result.toString());
            
        } catch (Exception e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    /**
     * Returns all cli options
     * @return all cli options
     */
    public static Options getOptions() {
        Options ops = new Options();
        for( Option o : mandatoryOptions)
            ops.addOption(o);
        for( Option o : optionalOptions)
            ops.addOption(o);
        return ops;
    }

    /**
     * Parse and validate the cli arguments
     * @param ln parsed command line
     * @return parsed cli parameters wrapped in the CliParams
     * @throws InvalidArgumentException in case of nonexistent or incorrect cli args
     */
    protected CliParams parse(CommandLine ln, Properties defaults) throws InvalidArgumentException {
        l.debug("Parsing cli "+ln);
        CliParams cp = new CliParams();

        for( Option o : mandatoryOptions) {
            String name = o.getLongOpt();
            if (ln.hasOption(name))
                cp.put(name,ln.getOptionValue(name));
            else if (defaults.getProperty(name) != null) {
            	cp.put(name, defaults.getProperty(name));
            } else {
                throw new InvalidArgumentException("Missing the '"+name+"' commandline parameter.");
            }

        }

        for( Option o : optionalOptions) {
            String name = o.getLongOpt();
            if (ln.hasOption(name)) {
                cp.put(name,ln.getOptionValue(name));
            } else if (defaults.getProperty(name) != null) {
            	cp.put(name, defaults.getProperty(name));
            }
        }

        if(cp.containsKey(CLI_PARAM_VERSION[0])) {
            l.info("GoodData CL version 1.2.2-SNAPSHOT" +
                    ((BUILD_NUMBER.length()>0) ? ", build "+BUILD_NUMBER : "."));
            System.exit(0);

        }


        // use default host if there is no host in the CLI params
        if(!cp.containsKey(CLI_PARAM_GDC_HOST[0])) {
            cp.put(CLI_PARAM_GDC_HOST[0], Defaults.DEFAULT_HOST);
        }

        l.debug("Using host "+cp.get(CLI_PARAM_GDC_HOST[0]));

        if(cp.containsKey(CLI_PARAM_PROTO[0])) {
            String proto = ln.getOptionValue(CLI_PARAM_PROTO[0]).toLowerCase();
            if(!"http".equalsIgnoreCase(proto) && !"https".equalsIgnoreCase(proto)) {
                throw new InvalidArgumentException("Invalid '"+CLI_PARAM_PROTO[0]+"' parameter. Use HTTP or HTTPS.");
            }
        }


        return cp;
    }


    /**
     * Returns the help for commands
     * @return help text
     */
    public static String commandsHelp() {
        try {
        	final InputStream is = CliParams.class.getResourceAsStream("/com/gooddata/processor/COMMANDS.txt");
        	if (is == null)
        		throw new IOException();
            return FileUtil.readStringFromStream(is);
        } catch (IOException e) {
            l.error("Could not read com/gooddata/processor/COMMANDS.txt");
        }
        return "";
    }
    

    private static boolean checkJavaVersion() {
        String version = System.getProperty("java.version");
        if(version.startsWith("1.6") || version.startsWith("1.5"))
            return true;
        l.error("You're running Java "+version+". Please use Java 1.5 or higher for running this tool. " +
                "Please refer to http://java.sun.com/javase/downloads/index.jsp for the Java 6 installation.");
        throw new InternalErrorException("You're running Java "+version+". Please use use Java 1.5 or higher for running this tool. " +
                "Please refer to http://java.sun.com/javase/downloads/index.jsp for the Java 6 installation.");
    }

    /**
     * The main CLI processor
     * @param args command line argument
     */
    public static void main(String[] args) {

        checkJavaVersion();
        String logConfig = System.getProperty("log4j.configuration");
        if(logConfig != null && logConfig.length()>0) {
            File lc = new File(logConfig);
            if(lc.exists()) {
                PropertyConfigurator.configure(logConfig);
                Properties defaults = loadDefaults();
                try {
                    Options o = getOptions();
                    CommandLineParser parser = new GnuParser();
                    CommandLine cmdline = parser.parse(o, args);
                    GdcNotification gdi = new GdcNotification(cmdline, defaults);
                    if (!gdi.finishedSucessfuly) {
                        System.exit(1);
                    }
                } catch (org.apache.commons.cli.ParseException e) {
                    l.error("Error parsing command line parameters: "+e.getMessage());
                    l.debug("Error parsing command line parameters",e);
                }
            }
            else {
                l.error("Can't find the logging config. Please configure the logging via the log4j.configuration.");
            }
        }
        else {
            l.error("Can't find the logging config. Please configure the logging via the log4j.configuration.");
        }
    }


    /**
     * Loads default values of common parameters from a properties file searching
     * the working directory and user's home.
     * @return default configuration 
     */
    private static Properties loadDefaults() {
		final String[] dirs = new String[]{ "user.dir", "user.home" };
		final Properties props = new Properties();
		for (final String d : dirs) {
			String path = System.getProperty(d) + File.separator + DEFAULT_PROPERTIES;
			File f = new File(path);
			if (f.exists() && f.canRead()) {
				try {
					FileInputStream is = new FileInputStream(f);
					props.load(is);
                    l.debug("Succesfully red the gdi configuration from '" + f.getAbsolutePath() + "'.");                    
					return props;
				} catch (IOException e) {
					l.warn("Readable gdi configuration '" + f.getAbsolutePath() + "' found be error occurred reading it.");
					l.debug("Error reading gdi configuration '" + f.getAbsolutePath() + "': ", e);
				}
			}
		}
		return props;
	}

}
