package com.gooddata.connector;

import com.gooddata.exception.InitializationException;
import com.gooddata.exception.MetadataFormatException;
import com.gooddata.exception.ModelException;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.util.FileUtil;
import com.gooddata.util.StringUtil;
import org.gooddata.connector.AbstractConnector;
import org.gooddata.connector.Connector;

import java.io.*;

import static org.apache.derby.tools.ij.runScript;

/**
 * GoodData CSV Connector
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class CsvConnector extends AbstractConnector implements Connector {

    
    // data file
    private File dataFile;

    private boolean hasHeader;

    /**
     * GoodData CSV connector. This constructor creates the connector from a config file
     * @param projectId project id
     * @param configFileName schema config file name
     * @param dataFileName primary data file
     * @param header true if the loaded CSV file has header row
     * @param connectorBackend connector backend
     * @param username database backend username
     * @param password database backend password
     * @throws InitializationException issues with the initialization
     * @throws MetadataFormatException issues with the metadata definitions
     * @throws IOException in case of an IO issue 
     */
    protected CsvConnector(String projectId, String configFileName, String dataFileName, boolean header,
               int connectorBackend,String username, String password) throws InitializationException,
            MetadataFormatException, IOException, ModelException {
        super(projectId, configFileName, connectorBackend, username, password);
        setDataFile(new File(dataFileName));
        setHasHeader(header);
    }

    /**
     * Create a new GoodData CSV connector. This constructor creates the connector from a config file
     * @param projectId project id
     * @param configFileName schema config file name
     * @param dataFileName primary data file
     * @param header true if the loaded CSV file has header row
     * @param connectorBackend connector backend
     * @param username database backend username
     * @param password database backend password 
     * @return new CSV Connector 
     * @throws InitializationException issues with the initialization
     * @throws MetadataFormatException issues with the metadata definitions
     * @throws IOException in case of an IO issue
     */
    public static CsvConnector createConnector(String projectId, String configFileName, String dataFileName,
                   boolean header,int connectorBackend, String username, String password)
                   throws InitializationException,
            MetadataFormatException, IOException, ModelException {
        return new CsvConnector(projectId, configFileName, dataFileName, header, connectorBackend, username, password);    
    }

    /**
     * Saves a template of the config file
     * @param configFileName the new config file name
     * @param dataFileName the data file
     * @throws IOException
     * @throws AssertionError 
     * @throws ModelException 
     */
    public static void saveConfigTemplate(String configFileName, String dataFileName) throws IOException, ModelException, AssertionError {
    	saveConfigTemplate(configFileName, dataFileName, null, null);
    }
    
    public static void saveConfigTemplate(String configFileName, String dataFileName, String defaultLdmType, String folder) throws IOException, ModelException, AssertionError {
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
     * Extracts the source data CSV to the Derby database where it is going to be transformed
     * @throws ModelException in case of PDM schema issues
     */
    public void extract() throws ModelException, IOException {
        if(getHasHeader()) {
            File tmp = FileUtil.stripCsvHeader(getDataFile());
            getConnectorBackend().extract(tmp);
            tmp.delete();
        }
    }

    public boolean getHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }
}