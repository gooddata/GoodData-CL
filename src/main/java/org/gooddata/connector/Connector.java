package org.gooddata.connector;

import com.gooddata.exception.InternalErrorException;
import com.gooddata.exception.ModelException;
import com.gooddata.integration.model.DLI;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;

import java.io.IOException;
import java.util.List;

/**
 * GoodData
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public interface Connector {

    /**
     * Generates the MAQL for the data source
     * @return the MAQL in string format
     */
    public String generateMaql();
    
    /**
     * Generates the MAQL for the specified columns
     * of the datasource
     * @return the MAQL in string format
     */
    public String generateMaql(List<SourceColumn> columns);

    /**
     * LDM schema getter
     * @return LDM schema
     */
    public SourceSchema getSchema();

    /**
     * LDM schema setter
     * @param schema LDM schema
     */
    public void setSchema(SourceSchema schema);

    /**
     * Figures out if the connector is initialized
     * @return the initialization status
     */
    public boolean isInitialized();

    /**
     * Initializes the Derby database schema that is going to be used for the data normalization
     * @throws com.gooddata.exception.ModelException imn case of PDM schema issues
     */
    public void initialize() throws ModelException;

    /**
     * Drops all snapshots
     */
    public void dropSnapshots();

    /**
     * Lists the current snapshots
     * @return list of snapshots as String
     * @throws com.gooddata.exception.InternalErrorException in case of internal issues (e.g. uninitialized schema)
     */
    public String listSnapshots() throws InternalErrorException;

    /**
     * Extracts the source data CSV to the Derby database where it is going to be transformed
     * @throws ModelException in case of PDM schema issues
     */
    public void extract() throws ModelException, IOException;

    /**
     * Perform the data normalization (generate lookups) in the Derby database. The database must contain the required
     * tables
     * @throws ModelException in case of PDM schema issues
     */
    public void transform() throws ModelException;

    /**
     * Create the GoodData data package with the ALL data
     * @param dli the Data Loading Interface that contains the required data structures
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     * @param archiveName the name of the target ZIP archive
     * @throws java.io.IOException IO issues
     * @throws ModelException in case of PDM schema issues
     */
    public void deploy(DLI dli, List<DLIPart> parts, String dir, String archiveName)
            throws IOException, ModelException;

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
            throws IOException, ModelException;

    /**
     * Get last snapshot number
     * @return last snapshot number
     * @throws InternalErrorException in case of internal issues (e.g. uninitialized schema)
     */
    public int getLastSnapshotId() throws InternalErrorException;

    

    
}
