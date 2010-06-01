package com.gooddata.processor;

import com.gooddata.connector.AbstractConnector;
import com.gooddata.connector.CsvConnector;
import com.gooddata.connector.exceptions.InternalErrorException;
import com.gooddata.integration.ftp.GdcFTPApiWrapper;
import com.gooddata.integration.model.DLI;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.integration.rest.GdcRESTApiWrapper;
import com.gooddata.integration.rest.configuration.NamePasswordConfiguration;
import com.gooddata.processor.exceptions.InvalidArgumentException;
import com.gooddata.util.FileUtil;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The GoodData Data Integration CLI processor.
 *
 * @author jiri.zaloudek
 * @author Zdenek Svoboda <zd@gooddata.org>
 * @version 1.0
 */
public class GdcDI {

    private static Command[] cmdHelp = new Command[] {
        new Command("CreateProject", "name=new project name,desc=new project description"),
        new Command("OpenProject", "id=existing project id"),
        new Command("GenerateCsvConfigTemplate", "csvHeaderFile=CSV header file (1 header row only),"+
            "configFile=configuration file (will be overwritten)"),
        new Command("LoadCsv", "name=dataset name,"+
            "configFile=configuration file (will be overwritten)," +
            "csvDataFile=CSV datafile (data only no headers)"),
        new Command("GenerateMaql", "maqlFile=MAQL file (will be overwritten)"),
        new Command("ExecuteMaql", "maqlFile=MAQL file"),
        new Command("ListSnapshots", ""),
        new Command("DropSnapshots", ""),
        new Command("TransferAllData", ""),
        new Command("TransferSnapshots", "firstSnapshot=the first transferred snapshot id," +
                "lastSnapshot=the last transferred snapshot id"),
        new Command("TransferLastSnapshot", "")
    };


    /**
     * The main CLI processor
     * @param args command line argument
     * @throws Exception any issue
     */
    public static void main(String[] args) throws Exception {

        String userName = null;
        String password = null;
        String host = "secure.gooddata.com";
        String ftpHost = null;

        Options o = new Options();

        o.addOption("u", "username", true, "GoodData username");
        o.addOption("p", "password", true, "GoodData password");
        o.addOption("h", "host", true, "GoodData host");

        CommandLineParser parser = new GnuParser();
        CommandLine line = parser.parse(o, args);

        if(line.hasOption("username")) {
            userName = line.getOptionValue("username");
        }
        else
            printErrorHelpandExit("Please specify a GoodData username (-u or --username command-line options).\n", o);

        if(line.hasOption("password")) {
            password = line.getOptionValue("password");
        }
        else
            printErrorHelpandExit("Please specify a GoodData password (-p or --password command-line options).\n", o);

        if(line.hasOption("host")) {
            host = line.getOptionValue("host");
        }
        else {
            System.out.println("Using the default GoodData REST API host '" + host + "'.");
        }

        // Create the FTP host automatically
        String[] hcs = host.split("\\.");
        if(hcs != null && hcs.length > 0) {
            for(String hc : hcs) {
                if(ftpHost != null && ftpHost.length()>0)
                    ftpHost += "." + hc;
                else
                    ftpHost = hc + "-upload";
            }
        }
        else {
            printErrorHelpandExit("Invalid format of the GoodData REST API host: " + host + ".\n", o);
        }

        System.out.println("Using the GoodData FTP host '" + ftpHost + "'.");

        NamePasswordConfiguration httpConfiguration = new NamePasswordConfiguration("https", host,
                userName, password);
        NamePasswordConfiguration ftpConfiguration = new NamePasswordConfiguration("ftp",
                ftpHost, userName, password);
        
        GdcRESTApiWrapper restApi = new GdcRESTApiWrapper(httpConfiguration);
        GdcFTPApiWrapper ftpApi = new GdcFTPApiWrapper(ftpConfiguration);

        String projectId = null;
        AbstractConnector connector = null;

        restApi.login();

        String commands = FileUtil.readStringFromFile(args[args.length - 1]);

        Command[] cmds = parseCmd(commands);

        for(Command command : cmds) {

            if(command.getCommand().equalsIgnoreCase("CreateProject")) {
                String name = (String)command.getParameters().get("name");
                String desc = (String)command.getParameters().get("desc");
                if(name != null && name.length() > 0) {
                    if(desc != null && desc.length() > 0)
                        projectId = restApi.createProject(name, desc);
                    else
                        projectId = restApi.createProject(name, name);
                }
                else
                    printErrorHelpandExit("CreateProject: Command requires the 'name' parameter.", o);

            }

            if(command.getCommand().equalsIgnoreCase("OpenProject")) {
                String id = (String)command.getParameters().get("id");
                if(id != null && id.length() > 0) {
                    projectId = id;
                }
                else {
                    printErrorHelpandExit("OpenProject: Command requires the 'id' parameter.", o);
                }
            }

            if(command.getCommand().equalsIgnoreCase("GenerateCsvConfigTemplate")) {
                String configFile = (String)command.getParameters().get("configFile");
                String csvHeaderFile = (String)command.getParameters().get("csvHeaderFile");
                if(configFile != null && configFile.length() > 0) {
                    File cf = new File(configFile);
                    if(csvHeaderFile != null && csvHeaderFile.length() > 0) {
                        File csvf = new File(csvHeaderFile);
                        if(csvf.exists())  {
                            CsvConnector.saveConfigTemplate(configFile, csvHeaderFile);
                        }
                        else
                            printErrorHelpandExit(
                                "GenerateCsvConfigTemplate: File '" + csvHeaderFile +
                                "' doesn't exists.", o);
                    }
                    else
                        printErrorHelpandExit(
                                "GenerateCsvConfigTemplate: Command requires the 'csvHeaderFile' parameter.", o);
                }
                else
                    printErrorHelpandExit("GenerateCsvConfigTemplate: Command requires the 'configFile' parameter.", o);
            }

            if(command.getCommand().equalsIgnoreCase("LoadCsv")) {
                String name = (String)command.getParameters().get("name");
                String configFile = (String)command.getParameters().get("configFile");
                String csvDataFile = (String)command.getParameters().get("csvDataFile");
                if(name != null && name.length() > 0) {
                    if(configFile != null && configFile.length() > 0) {
                        File conf = new File(configFile);
                        if(conf.exists()) {
                            if(csvDataFile != null && csvDataFile.length() > 0) {
                                File csvf = new File(csvDataFile);
                                if(csvf.exists())  {
                                    if(projectId != null) {
                                        connector = CsvConnector.createConnector(projectId, name, configFile, csvDataFile);
                                    }
                                    else
                                        printErrorHelpandExit(
                                        "LoadCsv: No active project found. Use command 'CreateProject'" +
                                        " or 'OpenProject' first.", o);
                                }
                                else
                                    printErrorHelpandExit(
                                        "LoadCsv: File '" + csvDataFile +
                                        "' doesn't exists.", o);
                            }
                            else
                                printErrorHelpandExit(
                                        "LoadCsv: Command requires the 'csvHeaderFile' parameter.", o);
                        }
                        else
                            printErrorHelpandExit(
                                        "LoadCsv: File '" + configFile +
                                        "' doesn't exists.", o);
                    }
                    else
                        printErrorHelpandExit(
                            "LoadCsv: Command requires the 'configFile' parameter.", o);
                }
                else
                    printErrorHelpandExit("LoadCsv: Command requires the 'name' parameter.", o);
            }

            if(command.getCommand().equalsIgnoreCase("GenerateMaql")) {
                String maqlFile = (String)command.getParameters().get("maqlFile");
                if(maqlFile != null && maqlFile.length() > 0) {
                    if(connector != null) {
                        String maql = connector.generateMaql();
                        FileUtil.writeStringToFile(maql, maqlFile);
                    }
                    else
                        printErrorHelpandExit("GenerateMaql: No data source loaded. Use a 'LoadXXX' to load a data source.",
                                o);


                }
                else
                    printErrorHelpandExit("GenerateMaql: Command requires the 'maqlFile' parameter.", o);

            }

            if(command.getCommand().equalsIgnoreCase("ExecuteMaql")) {
                String maqlFile = (String)command.getParameters().get("maqlFile");
                if(maqlFile != null && maqlFile.length() > 0) {
                    File mf = new File(maqlFile);
                    if(mf.exists()) {
                        if(projectId != null) {
                            String maql = FileUtil.readStringFromFile(maqlFile);
                            restApi.executeMAQL(projectId, maql);
                        }
                        else
                            printErrorHelpandExit(
                                        "ExecuteMaql: No active project found. Use command 'CreateProject'" +
                                        " or 'OpenProject' first.", o);
                    }
                    else
                        printErrorHelpandExit(
                                        "ExecuteMaql: File '" + maqlFile +
                                        "' doesn't exists.", o);
                }
                else
                    printErrorHelpandExit("ExecuteMaql: Command requires the 'maqlFile' parameter.", o);

            }

            if(command.getCommand().equalsIgnoreCase("ListSnapshots")) {
                if(connector != null) {
                    System.out.println(connector.listSnapshots());
                }
                else
                    printErrorHelpandExit("ListSnapshots: No data source loaded. Use a 'LoadXXX' to load a data source.",
                            o);

            }

            if(command.getCommand().equalsIgnoreCase("DropSnapshots")) {
                if(connector != null) {
                    connector.dropSnapshots();
                }
                else
                    printErrorHelpandExit("DropSnapshots: No data source loaded. Use a 'LoadXXX' to load a data source.",
                            o);

            }

            if(command.getCommand().equalsIgnoreCase("TransferAllData")) {
                if(connector != null) {
                    if(!connector.isInitialized())
                        connector.initialize();

                    // retrieve the DLI
                    DLI dli = restApi.getDLIById("dataset." + connector.getName().toLowerCase(), projectId);
                    List<DLIPart> parts= restApi.getDLIParts("dataset." + connector.getName().toLowerCase(), projectId);
                    // target directories and ZIP names
                    File tmpDir = FileUtil.createTempDir();
                    File tmpZipDir = FileUtil.createTempDir();
                    String archiveName = tmpDir.getName();
                    String archivePath = tmpZipDir.getAbsolutePath() + System.getProperty("file.separator") +
                            archiveName + ".zip";
                    // loads the CSV data to the embedded Derby SQL
                    connector.extract();
                    // normalize the data in the Derby
                    connector.transform();
                    // load data from the Derby to the local GoodData data integration package
                    connector.deploy(dli, parts, tmpDir.getAbsolutePath(), archivePath);
                    // transfer the data package to the GoodData server
                    ftpApi.transferDir(archivePath);
                    // kick the GooDData server to load the data package to the project
                    restApi.startLoading(projectId, archiveName);
                }
                else
                    printErrorHelpandExit("TransferAllData: No data source loaded. Use a 'LoadXXX' to load a data source.",
                            o);

            }

            if(command.getCommand().equalsIgnoreCase("TransferSnapshots")) {
                String firstSnapshot = (String)command.getParameters().get("firstSnapshot");
                String lastSnapshot = (String)command.getParameters().get("lastSnapshot");
                if(firstSnapshot != null && firstSnapshot.length() > 0) {
                    if(lastSnapshot != null && lastSnapshot.length() > 0) {
                        int fs = 0,ls = 0;
                        try  {
                            fs = Integer.parseInt(firstSnapshot);
                        }
                        catch (NumberFormatException e) {
                            printErrorHelpandExit("TransferSnapshots: The 'firstSnapshot' (" + firstSnapshot +
                                    ") parameter is not a number.", o);
                        }
                        try {
                            ls = Integer.parseInt(lastSnapshot);
                        }
                        catch (NumberFormatException e) {
                            printErrorHelpandExit("TransferSnapshots: The 'lastSnapshot' (" + lastSnapshot +
                                    ") parameter is not a number.", o);
                        }
                        int cnt = ls - fs;
                        if(cnt >= 0) {
                            int[] snapshots = new int[cnt];
                            for(int i = 0; i < cnt; i++) {
                                snapshots[i] = fs + i;
                            }
                            if(connector != null) {
                                if(!connector.isInitialized())
                                    connector.initialize();
                                // retrieve the DLI
                                DLI dli = restApi.getDLIById("dataset." + connector.getName().toLowerCase(),
                                        projectId);
                                List<DLIPart> parts= restApi.getDLIParts("dataset." +
                                        connector.getName().toLowerCase(), projectId);
                                for(DLIPart part : parts) {
                                    if(part.getFileName().startsWith("f_")) {
                                        part.setLoadMode(DLIPart.LM_INCREMENTAL);
                                    }
                                }
                                // target directories and ZIP names
                                File tmpDir = FileUtil.createTempDir();
                                File tmpZipDir = FileUtil.createTempDir();
                                String archiveName = tmpDir.getName();
                                String archivePath = tmpZipDir.getAbsolutePath() +
                                        System.getProperty("file.separator") + archiveName + ".zip";
                                // loads the CSV data to the embedded Derby SQL
                                connector.extract();
                                // normalize the data in the Derby
                                connector.transform();
                                // load data from the Derby to the local GoodData data integration package
                                connector.deploySnapshot(dli, parts, tmpDir.getAbsolutePath(), archivePath,
                                        snapshots);
                                // transfer the data package to the GoodData server
                                ftpApi.transferDir(archivePath);
                                // kick the GooDData server to load the data package to the project
                                restApi.startLoading(projectId, archiveName);
                            }
                            else
                                printErrorHelpandExit("TransferSnapshots: No data source loaded." +
                                            "Use a 'LoadXXX' to load a data source.", o);
                        }
                        else
                            printErrorHelpandExit("TransferSnapshots: The 'lastSnapshot' (" + lastSnapshot +
                                    ") parameter must be higher than the 'firstSnapshot' (" + firstSnapshot +
                                    ") parameter.", o);
                    }
                    else
                        printErrorHelpandExit("TransferSnapshots: Command requires the 'lastSnapshot' parameter.", o);
                }
                else
                    printErrorHelpandExit("TransferSnapshots: Command requires the 'firstSnapshot' parameter.", o);
            }

            if(command.getCommand().equalsIgnoreCase("TransferLastSnapshot")) {
                if(connector != null) {
                    if(!connector.isInitialized())
                        connector.initialize();
                    // retrieve the DLI
                    DLI dli = restApi.getDLIById("dataset." + connector.getName().toLowerCase(),
                            projectId);
                    List<DLIPart> parts= restApi.getDLIParts("dataset." +
                            connector.getName().toLowerCase(), projectId);
                    for(DLIPart part : parts) {
                        if(part.getFileName().startsWith("f_")) {
                            part.setLoadMode(DLIPart.LM_INCREMENTAL);
                        }
                    }
                    // target directories and ZIP names
                    File tmpDir = FileUtil.createTempDir();
                    File tmpZipDir = FileUtil.createTempDir();
                    String archiveName = tmpDir.getName();
                    String archivePath = tmpZipDir.getAbsolutePath() +
                            System.getProperty("file.separator") + archiveName + ".zip";
                    // loads the CSV data to the embedded Derby SQL
                    connector.extract();
                    // normalize the data in the Derby
                    connector.transform();
                    // load data from the Derby to the local GoodData data integration package
                    connector.deploySnapshot(dli, parts, tmpDir.getAbsolutePath(), archivePath,
                            new int[] {connector.getLastSnapshotId()});
                    // transfer the data package to the GoodData server
                    ftpApi.transferDir(archivePath);
                    // kick the GooDData server to load the data package to the project
                    restApi.startLoading(projectId, archiveName);
                }
                else
                    printErrorHelpandExit("TransferLastSnapshot: No data source loaded." +
                            "Use a 'LoadXXX' to load a data source.", o);
            }
        }
    }

    /**
     * Parses the commands
     * @param cmd commands string
     * @return array of commands
     * @throws InvalidArgumentException in case there is an invalid command
     */
    protected static Command[] parseCmd(String cmd) throws InvalidArgumentException {
        if(cmd != null && cmd.length()>0) {
            List<Command> cmds = new ArrayList<Command>();
            String[] commands = cmd.split(";");
            for( String component : commands) {
                component = component.trim();
                if(component != null && component.length() > 0 && !component.startsWith("#")) {                    
                    Pattern p = Pattern.compile(".*?\\(.*?\\)");
                    Matcher m = p.matcher(component);
                    if(!m.matches())
                        throw new InvalidArgumentException("Invalid command: "+component);
                    p = Pattern.compile(".*?\\(");
                    m = p.matcher(component);
                    String command = "";
                    if(m.find()) {
                        command = m.group();
                        command = command.substring(0, command.length() - 1);
                    }
                    else {
                        throw new InvalidArgumentException("Can't extract command from: "+component);
                    }
                    p = Pattern.compile("\\(.*?\\)");
                    m = p.matcher(component);
                    Properties args = new Properties();
                    if(m.find()) {
                        String as = m.group();
                        as = as.substring(1,as.length()-1);
                        try {
                            args.load(new StringReader(as.replace(",","\n")));
                        }
                        catch (IOException e) {
                            throw new InvalidArgumentException(e.getMessage());
                        }
                    }
                    else {
                        throw new InvalidArgumentException("Can't extract command from: "+component);
                    }
                    cmds.add(new Command(command, args));
                }
            }
            return cmds.toArray(new Command[]{});
        }
        throw new InvalidArgumentException("Can't parse command.");
    }

    /**
     * Returns the help for commands
     * @param commands commands array
     * @return help text
     */
    protected static String commandsHelp(Command[] commands) {
        String helpText = "Commands:\n\n";
        for(Command cmd : commands) {
            helpText += " " + cmd.getCommand() + "\n";
            for(Object pName : cmd.getParameters().keySet()) {
                String paramName = (String)pName;
                String paramValue = cmd.getParameters().getProperty(paramName);
                helpText += "  " + paramName + " - " + paramValue + "\n";
            }
            helpText += "\n";
        }
        return helpText;
    }

    /**
     * Prints an err message, help and exits with status code 1
     * @param err the err message
     * @param o options
     */
    protected static void printErrorHelpandExit(String err, Options o) {
        HelpFormatter formatter = new HelpFormatter();
        System.out.println("ERROR: " + err);
        formatter.printHelp( "GdcDI", o );
        System.out.println("\n");
        System.out.println(commandsHelp(cmdHelp));
        System.exit(1);    
    }

}
