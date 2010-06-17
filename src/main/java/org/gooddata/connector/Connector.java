package org.gooddata.connector;

import com.gooddata.integration.model.DLI;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import org.gooddata.connector.processor.Executor;

import java.io.IOException;
import java.util.List;

/**
 * GoodData Connector interface. Connector handles a specific external data source. It generates the GoodData project's
 * logical model from the data and loads the data to the project.
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public interface Connector extends Executor {

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
     * Initializes the database schema that is going to be used for the data normalization
     */
    public void initialize();

    /**
     * Drops all current snapshots. This is usually achieved by dropping the whole project database.
     */    
    public void dropSnapshots();

    /**
     * Lists the current snapshots
     * @return list of snapshots as String
     */
    public String listSnapshots();

    /**
     * Extracts the source data CSV to the database where it is going to be transformed
     * @throws IOException in case of IO issues
     */
    public void extract() throws IOException;

    /**
     * Perform the data normalization (generate lookups). The database must contain the required
     * schema
     */
    public void transform();

    /**
     * Create the GoodData data package with the ALL snapshots data
     * @param dli the Data Loading Interface that contains the required data structures
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     * @param archiveName the name of the target ZIP archive
     * @throws IOException IO issues
     */
    public void deploy(DLI dli, List<DLIPart> parts, String dir, String archiveName) throws IOException;

    /**
     * Create the GoodData data package with the data from specified snapshots
     * @param dli the Data Loading Interface that contains the required data structures
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     * @param archiveName the name of the target ZIP archive
     * @param snapshotIds snapshot ids that are going to be loaded (if NULL, all snapshots are going to be loaded)
     * @throws IOException IO issues
     */
    public void deploySnapshot(DLI dli, List<DLIPart> parts, String dir, String archiveName, int[] snapshotIds) throws IOException;

/**
     * Get last snapshot number. Snapshot is each individual lad of data. Snapshots are numbered (0...N).
     * Sometimes when you call this method at the beginning of a process that creates new snapshot, you might want to
     * add one to the snapshot number.
     * @return last snapshot number
     */
    public int getLastSnapshotId();

}
