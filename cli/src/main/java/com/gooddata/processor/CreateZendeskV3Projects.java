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

import com.gooddata.Constants;
import com.gooddata.exception.*;
import com.gooddata.integration.model.Project;
import com.gooddata.integration.rest.GdcRESTApiWrapper;
import com.gooddata.integration.rest.configuration.NamePasswordConfiguration;
import com.gooddata.util.CSVReader;
import com.gooddata.util.CSVWriter;
import com.gooddata.util.FileUtil;
import com.gooddata.util.StringUtil;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Utility that creates a new Zendesk V3 project for every V1 Zendesk project.
 * The utility copies all users from the old V1 project to the new V3 project.
 * It needs to be executed under the bear@gooddata.com, as adding users to projects is only allowed to the admin of the
 * domain where the user has been created. We assume that all Zendesk users are in the default GoodData domain.
 *
 * @author Zdenek Svoboda <zd@gooddata.org>
 * @version 1.0
 */
public class CreateZendeskV3Projects {

    private static Logger l = Logger.getLogger(CreateZendeskV3Projects.class);

    private boolean finishedSucessfuly = false;

    //Options data
    public static String[] CLI_PARAM_HELP = {"help", "h"};
    public static String[] CLI_PARAM_USERNAME = {"username", "u"};
    public static String[] CLI_PARAM_PASSWORD = {"password", "p"};
    public static String[] CLI_PARAM_HOST = {"host", "h"};
    public static String[] CLI_PARAM_TEMPLATE = {"template", "t"};
    public static String[] CLI_PARAM_VERSION = {"version", "v"};
    public static String[] CLI_PARAM_LIST = {"input", "i"};
    public static String[] CLI_PARAM_OUTPUT = {"output", "o"};
    public static String[] CLI_PARAM_TOKEN = {"token", "a"};

    // Command line options
    private static Options ops = new Options();
    public static Option[] mandatoryOptions = {
            new Option(CLI_PARAM_USERNAME[1], CLI_PARAM_USERNAME[0], true, "GoodData username"),
            new Option(CLI_PARAM_PASSWORD[1], CLI_PARAM_PASSWORD[0], true, "GoodData password"),
            new Option(CLI_PARAM_LIST[1], CLI_PARAM_LIST[0], true, "List of the V1 projects that needs to be converted (single column CSV with the V1 project hash)"),
            new Option(CLI_PARAM_OUTPUT[1], CLI_PARAM_OUTPUT[0], true, "List that associates the old V1 project and the new V3 project (two columns: [old hash,new hash])."),
            new Option(CLI_PARAM_TEMPLATE[1], CLI_PARAM_TEMPLATE[0], true, "The template to create the new V3 projects from")
    };

    public static Option[] optionalOptions = {
            new Option(CLI_PARAM_HOST[1], CLI_PARAM_HOST[0], true, "GoodData host (default secure.gooddata.com)"),
            new Option(CLI_PARAM_TOKEN[1], CLI_PARAM_TOKEN[0], true, "Create project access token.")
    };

    public static Option[] helpOptions = {
            new Option(CLI_PARAM_HELP[1], CLI_PARAM_HELP[0], false, "Print command reference"),
            new Option(CLI_PARAM_VERSION[1], CLI_PARAM_VERSION[0], false, "Prints the tool version.")
    };

    private CliParams cliParams = null;
    private ProcessingContext ctx = new ProcessingContext();

    public CreateZendeskV3Projects(CommandLine ln) {
        try {
            cliParams = parse(ln);

            String username = cliParams.get(CLI_PARAM_USERNAME[0]);
            String password = cliParams.get(CLI_PARAM_PASSWORD[0]);
            String input = cliParams.get(CLI_PARAM_LIST[0]);
            String output = cliParams.get(CLI_PARAM_OUTPUT[0]);
            String template = cliParams.get(CLI_PARAM_TEMPLATE[0]);
            String host = cliParams.get(CLI_PARAM_HOST[0]);
            String token = cliParams.get(CLI_PARAM_TOKEN[0]);

            NamePasswordConfiguration config = new NamePasswordConfiguration("https", host, username, password);
            cliParams.setHttpConfig(config);

            CSVReader reader = FileUtil.createUtf8CsvReader(new File(input));
            CSVWriter writer = FileUtil.createUtf8CsvWriter(new File(output));
            CSVWriter tool = FileUtil.createUtf8CsvWriter(new File(output+".tool"));

            tool.writeNext(new String[] {"login","project","role","project-state","user-state"});
            int rowCnt = 1;
            String[] row = reader.readNext();
            while(row != null && row.length > 0) {
                if(row.length > 1) {
                    throw new InvalidArgumentException("The row "+rowCnt+" of the '"+input+"' contains more than one column!");
                }
                String oldProjectHash = row[0];
                String newProjectHash = "ERROR: Failed to access the corresponding V1 project.";
                try {
                    newProjectHash = processProject(oldProjectHash, template, token, tool);
                }
                catch (GdcProjectAccessException e) {
                    l.info("The project "+oldProjectHash+" either doesn't exist, is disabled, or can't be accessed by the user that invoked this tool.");
                }
                writer.writeNext(new String[] {oldProjectHash,newProjectHash});
                writer.flush();
                tool.flush();
                row = reader.readNext();
                rowCnt++;
            }
            writer.close();
            tool.close();
            reader.close();
            finishedSucessfuly = true;
        } catch (InterruptedException e) {
            l.error("Interrupted during project creation." + e.getMessage());
            l.debug(e);
            Throwable c = e.getCause();
            while (c != null) {
                l.debug("Caused by: ", c);
                c = c.getCause();
            }
            finishedSucessfuly = false;
        } catch (IOException e) {
        l.error("Encountered an IO problem. Please check that all files that you use in your command line arguments and commands exist." + e.getMessage());
        l.debug(e);
        Throwable c = e.getCause();
        while (c != null) {
            l.debug("Caused by: ", c);
            c = c.getCause();
        }
        finishedSucessfuly = false;
    }

        catch (HttpMethodException e) {
            l.debug("Error executing GoodData REST API: " + e);
            Throwable c = e.getCause();
            while (c != null) {
                l.debug("Caused by: ", c);
                c = c.getCause();
            }

            String msg = e.getMessage();
            String requestId = e.getRequestId();
            if (requestId != null) {
                msg += "\n\n" +
                        "If you believe this is not your fault, good people from support\n" +
                        "portal (http://support.gooddata.com) may help you.\n\n" +
                        "Show them this error ID: " + requestId;
            }
            l.error(msg);
            finishedSucessfuly = false;
        } catch (GdcRestApiException e) {
            l.error("REST API invocation error: " + e.getMessage());
            l.debug(e, e);
            Throwable c = e.getCause();
            while (c != null) {
                if (c instanceof HttpMethodException) {
                    HttpMethodException ex = (HttpMethodException) c;
                    String msg = ex.getMessage();
                    if (msg != null && msg.length() > 0 && msg.indexOf("/ldm/manage") > 0) {
                        l.error("Error creating/updating logical data model (executing MAQL DDL).");
                        if (msg.indexOf(".date") > 0) {
                            l.error("Bad time dimension schemaReference.");
                        } else {
                            l.error("You are either trying to create a data object that already exists " +
                                    "(executing the same MAQL multiple times) or providing a wrong reference " +
                                    "or schemaReference in your XML configuration.");
                        }
                    }
                }
                l.debug("Caused by: ", c);
                c = c.getCause();
            }
            finishedSucessfuly = false;
        } catch (GdcException e) {
            l.error("Unrecognized error: " + e.getMessage());
            l.debug(e);
            Throwable c = e.getCause();
            while (c != null) {
                l.debug("Caused by: ", c);
                c = c.getCause();
            }
            finishedSucessfuly = false;
        } finally {
            /*
            if (cliParams != null)
                context.getRestApi(cliParams).logout();
                */
        }
    }

    /**
     * Creates a new V3 projects from the template identified by the templateUri for the V1 project that is passed in
     * the oldProjectHash parameter
     * Copies all users from the V1 to the V3 project with appropriate roles
     * @param oldProjectHash the old V1 project hash
     * @param templateUri the new V3 project template URI
     * @param token project creation token (redirects the new projects to the correct DWH server)
     * @return the new V3 project hash
     */
    private String processProject(String oldProjectHash, String templateUri, String token, CSVWriter tool) throws InterruptedException {
        Project project = ctx.getRestApi(cliParams).getProjectById(oldProjectHash);
        Map<String,GdcRESTApiWrapper.GdcUser> activeUsers = new HashMap<String,GdcRESTApiWrapper.GdcUser>();

        l.info("Getting users from project " + oldProjectHash);
        List<GdcRESTApiWrapper.GdcUser> users = ctx.getRestApi(cliParams).getProjectUsers(oldProjectHash, true);
        for(GdcRESTApiWrapper.GdcUser user : users) {
            activeUsers.put(user.getUri(), user);
        }
        l.info(users.size() + " users retrieved from project " + oldProjectHash);
        l.info("Getting roles from project " + oldProjectHash);
        List<GdcRESTApiWrapper.GdcRole> roles  = ctx.getRestApi(cliParams).getProjectRoles(oldProjectHash);
        l.info(roles.size() + " roles retrieved from project " + oldProjectHash);

        String newName = project.getName()+" (new)";
        String newProjectHash = ctx.getRestApi(cliParams).createProject(StringUtil.toTitle(newName), StringUtil.toTitle(newName), templateUri, "Pg", token);
        checkProjectCreationStatus(newProjectHash, cliParams, ctx);
        l.info("New V3 project created: " + newProjectHash);

        for(GdcRESTApiWrapper.GdcRole role : roles) {
            l.info("Getting users from role " + role.getIdentifier());
            List<String> userUris = ctx.getRestApi(cliParams).getRoleUsers(role, true);
            l.info(userUris.size() + " users retrieved from role " + role.getIdentifier());
            for(String userUri : userUris) {
                GdcRESTApiWrapper.GdcUser user = activeUsers.get(userUri);
                if(user != null) {
                    l.info("Adding user "+user.getLogin()+" to the new V3 project " + newProjectHash+ " with role "+role.getIdentifier());
                    tool.writeNext(new String[] {user.getLogin(),newProjectHash,role.getIdentifier(),"ENABLED","ENABLED"});
                }
                else {
                    l.info("Detected suspended user " + userUri);
                }
            }
        }
        return newProjectHash;
    }

    /**
     * Parse and validate the cli arguments
     *
     * @param ln parsed command line
     * @return parsed cli parameters wrapped in the CliParams
     * @throws com.gooddata.exception.InvalidArgumentException in case of nonexistent or incorrect cli args
     */
    protected CliParams parse(CommandLine ln) throws InvalidArgumentException {
        l.debug("Parsing cli " + ln);
        CliParams cp = new CliParams();

        if (cp.containsKey(CLI_PARAM_VERSION[0])) {
            l.info("GoodData CL version 1.3.0");
            System.exit(0);
        }

        if (ln.hasOption(CLI_PARAM_HELP[1])) {
            printHelp();
        }

        for (Option o : mandatoryOptions) {
            String name = o.getLongOpt();
            if (ln.hasOption(name))
                cp.put(name, ln.getOptionValue(name));
            else {
                l.info("Please specify the mandatory option "+name+"("+o.getOpt()+")");
                printHelp();
                System.exit(0);
            }
        }

        for (Option o : optionalOptions) {
            String name = o.getLongOpt();
            if (ln.hasOption(name))
                cp.put(name, ln.getOptionValue(name));
        }

        // use default host if there is no host in the CLI params
        if (!cp.containsKey(CLI_PARAM_HOST[0])) {
            cp.put(CLI_PARAM_HOST[0], Defaults.DEFAULT_HOST);
        }

        l.debug("Using host " + cp.get(CLI_PARAM_HOST[0]));

        return cp;
    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("createZendeskProjects [<options> ...]", ops);
    }

    private static boolean checkJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.8") || version.startsWith("1.7") || version.startsWith("1.6") || version.startsWith("1.5"))
            return true;
        l.error("You're running Java " + version + ". Please use Java 1.5 or higher for running this tool. " +
                "Please refer to http://java.sun.com/javase/downloads/index.jsp for a more recent Java version.");
        throw new InternalErrorException("You're running Java " + version + ". Please use use Java 1.5 or higher for running this tool. " +
                "Please refer to http://java.sun.com/javase/downloads/index.jsp for a more recent Java version.");
    }

    /**
     * The main CLI processor
     *
     * @param args command line argument
     */
    public static void main(String[] args) {

        checkJavaVersion();

        for (Option o : mandatoryOptions)
            ops.addOption(o);
        for (Option o : optionalOptions)
            ops.addOption(o);
        for (Option o : helpOptions)
            ops.addOption(o);

        try {
            CommandLineParser parser = new GnuParser();
            CommandLine cmdline = parser.parse(ops, args);
            CreateZendeskV3Projects gdi = new CreateZendeskV3Projects(cmdline);
            if (!gdi.finishedSucessfuly) {
                System.exit(1);
            }
        } catch (org.apache.commons.cli.ParseException e) {
            l.error("Error parsing command line parameters: ", e);
            l.debug("Error parsing command line parameters", e);
        }
    }

    /**
     * Checks the project status. Waits till the status is ENABLED or DELETED
     *
     * @param projectId project ID
     * @param p         cli parameters
     * @param ctx       current context
     * @throws InterruptedException internal problem with making file writable
     */
    private void checkProjectCreationStatus(String projectId, CliParams p, ProcessingContext ctx) throws InterruptedException {
        l.debug("Checking project " + projectId + " loading status.");
        String status = null;
        do {
            status = ctx.getRestApi(p).getProjectStatus(projectId);
            l.debug("Project " + projectId + " loading  status = " + status);
            Thread.sleep(Constants.POLL_INTERVAL);
        } while (!("DELETED".equalsIgnoreCase(status) || "ENABLED".equalsIgnoreCase(status)));
    }

}
