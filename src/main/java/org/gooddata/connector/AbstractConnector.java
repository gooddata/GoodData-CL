package org.gooddata.connector;

import com.gooddata.connector.backend.DerbyConnectorBackend;
import com.gooddata.connector.backend.MySqlConnectorBackend;
import com.gooddata.exceptions.InitializationException;
import com.gooddata.exceptions.InternalErrorException;
import com.gooddata.exceptions.MetadataFormatException;
import com.gooddata.exceptions.ModelException;
import com.gooddata.integration.model.DLI;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.modeling.generator.MaqlGenerator;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.connector.model.PdmSchema;
import org.gooddata.connector.backend.AbstractConnectorBackend;
import org.gooddata.connector.backend.ConnectorBackend;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * GoodData
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public abstract class AbstractConnector implements Connector {

    /**
     * The LDM schema of the data source
     */
    protected SourceSchema schema;

    // Connector backend
    private ConnectorBackend connectorBackend;

    protected AbstractConnector() {
    }

    /**
     * GoodData CSV connector. This constructor creates the connector from a config file
     * @param projectId project id
     * @param configFileName schema config file name
     * @param connectorBackend connector backend
     * @param username database backend username
     * @param password database backend password
     * @throws com.gooddata.exceptions.InitializationException issues with the initialization
     * @throws com.gooddata.exceptions.MetadataFormatException issues with the metadata definitions
     * @throws IOException in case of an IO issue
     */
    protected AbstractConnector(String projectId, String configFileName, int connectorBackend, String username,
                                String password) throws InitializationException,
            MetadataFormatException, IOException, ModelException {
        schema = SourceSchema.createSchema(new File(configFileName));
        PdmSchema pdm = PdmSchema.createSchema(schema);
        switch(connectorBackend) {
            case AbstractConnectorBackend.CONNECTOR_BACKEND_DERBY_SQL:
                setConnectorBackend(DerbyConnectorBackend.create(projectId, configFileName, pdm));
                break;
            case AbstractConnectorBackend.CONNECTOR_BACKEND_MYSQL:
                setConnectorBackend(MySqlConnectorBackend.create(projectId, configFileName, pdm, username, password));
                break;
            default:
                throw new InitializationException("Unsupported connector backend specified.");
        }
    }

    /**
     * Generates the MAQL for the data source
     * @return the MAQL in string format
     */
    public String generateMaql() {
        MaqlGenerator mg = new MaqlGenerator(schema);
        return mg.generateMaql();
    }

    /**
     * LDM schema getter
     * @return LDM schema
     */
    public SourceSchema getSchema() {
        return schema;
    }

    /**
     * LDM schema setter
     * @param schema LDM schema
     */
    public void setSchema(SourceSchema schema) {
        this.schema = schema;
    }

    /**
     * Connector backend getter
     * @return the connector backend
     */
    public ConnectorBackend getConnectorBackend() {
        return connectorBackend;
    }

    /**
     * Connector backend setter
     * @param connectorBackend the connector backend
     */
    public void setConnectorBackend(ConnectorBackend connectorBackend) {
        this.connectorBackend = connectorBackend;
    }

    /**
     * Drops all snapshots
     */
    public void dropSnapshots() {
        getConnectorBackend().dropSnapshots();
    }

    /**
     * Lists the current snapshots
     * @return list of snapshots as String
     * @throws com.gooddata.exceptions.InternalErrorException in case of internal issues (e.g. uninitialized schema)
     */
    public String listSnapshots() throws InternalErrorException {
        return getConnectorBackend().listSnapshots();
    }

    /**
     * Figures out if the connector is initialized
     * @return the initialization status
     */
    public boolean isInitialized() {
        return getConnectorBackend().isInitialized();
    }

    /**
     * Get last snapshot number
     * @return last snapshot number
     * @throws InternalErrorException in case of internal issues (e.g. uninitialized schema)
     */
    public int getLastSnapshotId() throws InternalErrorException {
        return getConnectorBackend().getLastSnapshotId();
    }


    /**
     * Create the GoodData data package with the ALL data
     * @param dli the Data Loading Interface that contains the required data structures
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     * @param archiveName the name of the target ZIP archive
     * @throws IOException IO issues
     * @throws com.gooddata.exceptions.ModelException in case of PDM schema issues
     */
    public void deploy(DLI dli, List<DLIPart> parts, String dir, String archiveName)
            throws IOException, ModelException {
        getConnectorBackend().deploy(dli, parts, dir, archiveName);
    }

    /**
     * Create the GoodData data package with the data from specified snapshots
     * @param dli the Data Loading Interface that contains the required data structures
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     * @param archiveName the name of the target ZIP archive
     * @param snapshotIds snapshot ids that are going to be loaded (if NULL, all snapshots are going to be loaded)
     * @throws IOException IO issues
     * @throws ModelException in case of PDM schema issues
     */
    public void deploySnapshot(DLI dli, List<DLIPart> parts, String dir, String archiveName, int[] snapshotIds)
            throws IOException, ModelException {
        getConnectorBackend().deploySnapshot(dli, parts, dir, archiveName, snapshotIds);
    }

    /**
     * Initializes the Derby database schema that is going to be used for the data normalization
     * @throws com.gooddata.exceptions.ModelException imn case of PDM schema issues
     */
    public void initialize() throws ModelException {
        getConnectorBackend().initialize();
    }

    /**
     * Perform the data normalization (generate lookups) in the Derby database. The database must contain the required
     * tables
     * @throws ModelException in case of PDM schema issues
     */
    public void transform() throws ModelException {        
        getConnectorBackend().transform();
    }

    /**
     * Extracts the source data CSV to the Derby database where it is going to be transformed
     * @throws ModelException in case of PDM schema issues
     */
    public abstract void extract() throws ModelException, IOException;

    
}
