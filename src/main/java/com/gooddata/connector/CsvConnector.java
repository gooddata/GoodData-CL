package com.gooddata.connector;

import com.gooddata.exception.*;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import org.gooddata.connector.processor.CliParams;
import org.gooddata.connector.processor.Command;
import org.gooddata.connector.processor.ProcessingContext;
import com.gooddata.util.FileUtil;
import com.gooddata.util.StringUtil;
import org.apache.log4j.Logger;
import org.gooddata.connector.AbstractConnector;
import org.gooddata.connector.Connector;
import org.gooddata.connector.backend.ConnectorBackend;

import java.io.*;

import static org.apache.derby.tools.ij.runScript;

/**
 * GoodData CSV Connector
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class CsvConnector extends AbstractConnector implements Connector {

    private static Logger l = Logger.getLogger(CsvConnector.class);
    
    // data file
    private File dataFile;

    private boolean hasHeader;

    /**
     * Creates GoodData CSV connector
     * @param backend connector backend
     */
    protected CsvConnector(ConnectorBackend backend) {
        super(backend);
    }

    /**
     * Create a new GoodData CSV connector. This constructor creates the connector from a config file
     * @param backend connector backend
     * @return new CSV Connector
     */
    public static CsvConnector createConnector(ConnectorBackend backend) {
        return new CsvConnector(backend);    
    }

    /**
     * Saves a template of the config file
     * @param configFileName the new config file name
     * @param dataFileName the data file
     * @throws IOException in case of an IO error
     */
    public static void saveConfigTemplate(String configFileName, String dataFileName) throws IOException {
    	saveConfigTemplate(configFileName, dataFileName, null, null);
    }

    /**
     * Saves a template of the config file
     * @param configFileName the new config file name
     * @param dataFileName the data file
     * @param defaultLdmType default LDM type
     * @param folder default folder
     * @throws IOException in case of an IO issue
     */
    public static void saveConfigTemplate(String configFileName, String dataFileName, String defaultLdmType, String folder) throws IOException {
        l.debug("Saving CSV config template.");
        File dataFile = new File(dataFileName);
        String name = dataFile.getName().split("\\.")[0];
        String[] headers = FileUtil.getCsvHeader(dataFile);
        int i = 0;
        final SourceSchema s;
        File configFile = new File(configFileName);
        if (configFile.exists()) {
        	s = SourceSchema.createSchema(configFile);
        } else {
        	s = SourceSchema.createSchema(name);
        }
        final int knownColumns = s.getColumns().size();
        for(int j = knownColumns; j < headers.length; j++) {
        	final String header = headers[j];
            final SourceColumn sc;
            final String identifier = StringUtil.csvHeaderToIdentifier(header);
            final String title = StringUtil.csvHeaderToTitle(header);
            if (defaultLdmType != null) {
            	sc = new SourceColumn(identifier, defaultLdmType, title, folder);
            } else {
	            switch (i %3) {
	                case 0:
	                    sc = new SourceColumn(identifier, SourceColumn.LDM_TYPE_ATTRIBUTE, title, "folder");
	                    break;
	                case 1:
	                    sc = new SourceColumn(identifier, SourceColumn.LDM_TYPE_FACT, title, "folder");
	                    break;
	                case 2:
	                    sc = new SourceColumn(identifier, SourceColumn.LDM_TYPE_LABEL, title, "folder", "existing-attribute-name");
	                    break;
	                default:
	                	throw new AssertionError("i % 3 outside {0, 1, 2} - this cannot happen");
	            }
            }
            s.addColumn(sc);
            i++;
        }
        s.writeConfig(new File(configFileName));
        l.debug("Saved CSV config template.");
    }

    /**
     * Data CSV file getter
     * @return the data CSV file
     */
    public File getDataFile() {
        return dataFile;
    }

    /**
     * Data CSV file setter
     * @param dataFile the data CSV file
     */
    public void setDataFile(File dataFile) {
        this.dataFile = dataFile;
    }

    /**
     * {@inheritDoc}
     */
    public void extract() throws IOException {
        if(getHasHeader()) {
            File tmp = FileUtil.stripCsvHeader(getDataFile());
            getConnectorBackend().extract(tmp);
            tmp.delete();
        }
    }

    /**
     * hasHeader getter
     * @return hasHeader flag
     */
    public boolean getHasHeader() {
        return hasHeader;
    }

    /**
     * hasHeader setter
     * @param hasHeader flag value
     */
    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    /**
     * {@inheritDoc}
     */
    public boolean processCommand(Command c, CliParams cli, ProcessingContext ctx) throws ProcessingException {
        l.debug("Processing command "+c.getCommand());
        try {
            if(c.match("GenerateCsvConfig")) {
                generateCsvConfig(c, cli, ctx);
            }
            else if(c.match("LoadCsv")) {
                loadCsv(c, cli, ctx);
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
     * Loads new CSV file command processor
     * @param c command
     * @param p command line arguments
     * @param ctx current processing context
     * @throws IOException in case of IO issues
     */
    private void loadCsv(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        String configFile = c.getParamMandatory("configFile");
        String csvDataFile = c.getParamMandatory("csvDataFile");
        String hdr = c.getParamMandatory("header");
        File conf = FileUtil.getFile(configFile);
        File csvf = FileUtil.getFile(csvDataFile);
        boolean hasHeader = false;
        if(hdr.equalsIgnoreCase("true"))
            hasHeader = true;
        initSchema(conf.getAbsolutePath());
        setDataFile(new File(csvf.getAbsolutePath()));
        setHasHeader(hasHeader);
        // sets the current connector
        ctx.setConnector(this);
        setProjectId(ctx);
    }

    /**
     * Generate new config file from CSV command processor
     * @param c command
     * @param p command line arguments
     * @param ctx current processing context
     * @throws IOException in case of IO issues
     */
    private void generateCsvConfig(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        String configFile = c.getParamMandatory("configFile");
        String csvHeaderFile = c.getParamMandatory("csvHeaderFile");
        File cf = new File(configFile);
        File csvf = FileUtil.getFile(csvHeaderFile);
        CsvConnector.saveConfigTemplate(configFile, csvHeaderFile);
    }
}