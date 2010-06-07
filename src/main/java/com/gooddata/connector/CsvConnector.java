package com.gooddata.connector;

import com.gooddata.exceptions.InitializationException;
import com.gooddata.exceptions.MetadataFormatException;
import com.gooddata.exceptions.ModelException;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
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

    /**
     * GoodData CSV connector. This constructor creates the connector from a config file
     * @param projectId project id
     * @param configFileName schema config file name
     * @param dataFileName primary data file
     * @param connectorBackend connector backend 
     * @throws InitializationException issues with the initialization
     * @throws MetadataFormatException issues with the metadata definitions
     * @throws IOException in case of an IO issue 
     */
    protected CsvConnector(String projectId, String configFileName, String dataFileName, int connectorBackend)
            throws InitializationException,
            MetadataFormatException, IOException {
        super(projectId, configFileName, connectorBackend);
        this.setDataFile(new File(dataFileName));
    }

    /**
     * Create a new GoodData CSV connector. This constructor creates the connector from a config file
     * @param projectId project id
     * @param configFileName schema config file name
     * @param dataFileName primary data file
     * @param connectorBackend connector backend
     * @return new CSV Connector 
     * @throws InitializationException issues with the initialization
     * @throws MetadataFormatException issues with the metadata definitions
     * @throws IOException in case of an IO issue
     */
    public static CsvConnector createConnector(String projectId, String configFileName, String dataFileName,
                                                int connectorBackend) throws InitializationException,
            MetadataFormatException, IOException {
        return new CsvConnector(projectId, configFileName, dataFileName, connectorBackend);    
    }

    /**
     * Saves a template of the config file
     * @param configFileName the new config file name
     * @param dataFileName the data file
     * @throws IOException
     */
    public static void saveConfigTemplate(String configFileName, String dataFileName) throws IOException {
        File dataFile = new File(dataFileName);
        String name = dataFile.getName().split("\\.")[0];
        BufferedReader r = new BufferedReader(new FileReader(dataFile));
        String headerLine = r.readLine();
        r.close();
        String[] headers = headerLine.split(",");
        int i = 0;
        SourceSchema s = SourceSchema.createSchema(name);
        for(String header : headers) {
            SourceColumn sc = null;
            switch (i %3) {
                case 0:
                    sc = new SourceColumn(StringUtil.csvHeaderToIdentifier(header),SourceColumn.LDM_TYPE_ATTRIBUTE,
                            StringUtil.csvHeaderToTitle(header), "folder");
                    break;
                case 1:
                    sc = new SourceColumn(StringUtil.csvHeaderToIdentifier(header),SourceColumn.LDM_TYPE_FACT,
                            StringUtil.csvHeaderToTitle(header), "folder");
                    break;
                case 2:
                    sc = new SourceColumn(StringUtil.csvHeaderToIdentifier(header),SourceColumn.LDM_TYPE_LABEL,
                            StringUtil.csvHeaderToTitle(header), "folder", "existing-attribute-name");
                    break;
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
    public void extract() throws ModelException {
        getConnectorBackend().extract(getDataFile());
    }
    
}