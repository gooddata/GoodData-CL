package org.gooddata.connector.backend;

import com.gooddata.connector.model.PdmSchema;
import com.gooddata.exception.InternalErrorException;
import com.gooddata.exception.ModelException;
import com.gooddata.integration.model.DLI;
import com.gooddata.integration.model.DLIPart;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * GoodData
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public interface ConnectorBackend {

    /**
     * Connects to the Derby database
     * @return JDBC connection
     * @throws java.sql.SQLException
     */
    public Connection connect() throws SQLException;

    /**
     * Perform the data normalization (generate lookups) in the Derby database. The database must contain the required
     * tables
     * @throws ModelException in case of PDM schema issues
     */
    public void transform() throws ModelException;

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
     * Get last snapshot number
     * @return last snapshot number
     * @throws InternalErrorException in case of internal issues (e.g. uninitialized schema)
     */
    public int getLastSnapshotId() throws InternalErrorException;

    /**
     * Initializes the Derby database schema that is going to be used for the data normalization
     * @throws com.gooddata.exception.ModelException imn case of PDM schema issues
     */
    public void initialize() throws ModelException;
    
    /**
     * Figures out if the connector is initialized
     * @return the initialization status
     */
    public boolean isInitialized();

    /**
     * Load the all normalized data from the SQL to the GoodData data package on the disk
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     * @throws ModelException in case of PDM schema issues
     */
    public void load(List<DLIPart> parts, String dir) throws ModelException;

    /**
     * Load the normalized data from the SQL to the GoodData data package on the disk
     * incrementally (specific snapshot)
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     * @param snapshotIds snapshot ids that are going to be loaded (if NULL, all snapshots are going to be loaded)
     * @throws ModelException in case of PDM schema issues
     */
    public void loadSnapshot(List<DLIPart> parts, String dir, int[] snapshotIds) throws ModelException;


    /**
     * Create the GoodData data package with the ALL data
     * @param dli the Data Loading Interface that contains the required data structures
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     * @param archiveName the name of the target ZIP archive
     * @throws IOException IO issues
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
     * Extracts the source data CSV to the Derby database where it is going to be transformed
     * @param dataFile the data file to extract
     * @throws ModelException in case of PDM schema issues
     */
    public void extract(File dataFile) throws ModelException;

    /**
     * Returns true if the specified table exists in the DB
     * @param tbl table name
     * @return true if the table exists, false otherwise
     */
    public boolean exists(String tbl);

    /**
     * The project id
     */
    public String getProjectId();

    public void setProjectId(String projectId);

    /**
     * The PDM schema
     */
    public PdmSchema getPdm();

    public void setPdm(PdmSchema schema);

}
