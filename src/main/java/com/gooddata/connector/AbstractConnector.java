package com.gooddata.connector;

import com.gooddata.exceptions.InternalErrorException;
import com.gooddata.exceptions.ModelException;
import com.gooddata.integration.model.Column;
import com.gooddata.integration.model.DLI;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.integration.rest.GdcRESTApiWrapper;
import com.gooddata.modeling.generator.MaqlGenerator;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.transformation.generator.model.PdmSchema;
import com.gooddata.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * GoodData abstract connector.
 * This connector creates a GoodData LDM schema from a source schema, extracts the data from the source,
 * normalizes the data, and create the GoodData data deployment package.
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public abstract class AbstractConnector {

    /**
     * The LDM schema of the data source
     */
    protected SourceSchema schema;

    /**
     * PDM schema
     */
    private PdmSchema pdm;

    /**
     * The project id
     */
    protected String projectId;

    /**
     * The data source name
     */
    protected String name;

    /**
     * The config file name
     */
    protected String configFileName;

    /**
     * The ZIP archive suffix
     */
    protected static final String DLI_ARCHIVE_SUFFIX = ".zip";


    /**
     * Constructor
     * @param projectId project id
     * @param name schema name
     */
    protected AbstractConnector(String projectId, String name) {
        this.projectId = projectId;
        this.name = name;
    }

    /**
     * Constructor
     * @param projectId project id 
     * @param name schema name
     * @param configFileName config file name
     * @throws IOException in case of an IO issue 
     */
    protected AbstractConnector(String projectId, String name, String configFileName) throws IOException {
        this(projectId, name);
        this.configFileName = configFileName;
        this.readSchema();
        getSchema().setName(name);
    }

    /**
     * Reads schema from the config file
     * @throws IOException in case of an IO issue
     */
    protected void readSchema() throws IOException {
        setSchema(SourceSchema.createSchema(new File(this.configFileName)));
        setPdm(PdmSchema.createSchema(getSchema()));
    }

    /**
     * Figures out if the connector is initialized
     * @return the initialization status
     */
    public abstract boolean isInitialized();

    /**
     * Initializes Connector
     * @throws ModelException imn case of PDM schema issues 
     */
    public abstract void initialize() throws ModelException;

    /**
     * Lists the current snapshots
     * @return list of snapshots as String
     * @throws InternalErrorException in case of internal issues (e.g. uninitialized schema)
     */
    public abstract String listSnapshots() throws InternalErrorException;

    /**
     * Drops all snapshots
     * @return  a msg
     */
    public abstract void dropSnapshots();
    
    /**
     * Extracts the data from the source system.
     * @throws ModelException in case of PDM schema issues 
     */
    public abstract void extract() throws ModelException;

    /**
     * Transforms (normalizes) the data.
     * @throws ModelException in case of PDM schema issues 
     */
    public abstract void transform() throws ModelException;

    /**
     * Load the all normalized data from the Derby SQL to the GoodData data package on the disk
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     * @throws ModelException in case of PDM schema issues 
     */
    public abstract void load(List<DLIPart> parts, String dir) throws ModelException;

    /**
     * Load the normalized data from the Derby SQL to the GoodData data package on the disk
     * incrementally (specific snapshot)
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     * @param snapshotIds snapshot ids that are going to be loaded (if NULL, all snapshots are going to be loaded)
     * @throws ModelException in case of PDM schema issues 
     */
    public abstract void loadSnapshot(List<DLIPart> parts, String dir, int[] snapshotIds) throws ModelException;

    /**
     * Get last snapshot number
     * @return last snapshot number
     * @throws InternalErrorException in case of internal issues (e.g. uninitialized schema)
     */
    public abstract int getLastSnapshotId() throws InternalErrorException;


    /**
     * Generates the MAQL for the data source
     * @return the MAQL in string format
     */
    public String generateMaql() {
        MaqlGenerator mg = new MaqlGenerator();
        return mg.generateMaql(schema);
    }

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
            throws IOException, ModelException {
        deploySnapshot(dli, parts, dir, archiveName, null);
    }

    /**
     * Adds CSV headers to all CSV files
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     * @throws IOException IO issues
     */
    protected void addHeaders(List<DLIPart> parts, String dir) throws IOException {
        for(DLIPart part : parts) {
            String fn = part.getFileName();
            List<Column> cols = part.getColumns();
            String header = "";
            for(Column col : cols) {
                if(header != null && header.length() > 0) {
                    header += ","+col.getName();
                }
                else {
                    header += col.getName();                    
                }
            }
            FileUtil.appendCsvHeader(header, dir + System.getProperty("file.separator") + fn);
        }
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
        loadSnapshot(parts, dir, snapshotIds);
        FileUtil.writeStringToFile(dli.getDLIManifest(parts), dir + System.getProperty("file.separator") +
                GdcRESTApiWrapper.DLI_MANIFEST_FILENAME);
        addHeaders(parts, dir);
        FileUtil.compressDir(dir, archiveName);
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
     * Data source name getter
     * @return data source name
     */
    public String getName() {
        return name;
    }

    /**
     * Data source name setter
     * @param name data source name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * PDM schema getter
     * @return pdm schema
     */
    public PdmSchema getPdm() {
        return pdm;
    }

    /**
     * PDM schema setter
     * @param pdm PDM schema
     */
    public void setPdm(PdmSchema pdm) {
        this.pdm = pdm;
    }
}
