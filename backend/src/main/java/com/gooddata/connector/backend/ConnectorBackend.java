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

package com.gooddata.connector.backend;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import com.gooddata.connector.model.PdmSchema;
import com.gooddata.integration.model.DLI;
import com.gooddata.integration.model.DLIPart;

/**
 * GoodData connector backend interface.
 * Connector backend handles communication with the specific SQL database. Specifically it handles the DB connection
 * and other communication specifics of the DBMS. It uses the SQL driver that generates appropriate SQL dialect.
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
*/
public interface ConnectorBackend {

    /**
     * Perform the data normalization (generate lookups). The database must contain the required schema
     */
    public void transform();

    /**
     * Drops the whole project database.
     */
    public void dropIntegrationDatabase();


    /**
     * Drops all current snapshots.
     */
    public void dropSnapshots();

    /**
     * Lists the current snapshots
     * @return list of snapshots as String
     */
    public String listSnapshots();

    /**
     * Get last snapshot number. Snapshot is each individual lad of data. Snapshots are numbered (0...N).
     * Sometimes when you call this method at the beginning of a process that creates new snapshot, you might want to
     * add one to the snapshot number.
     * @return last snapshot number
     */
    public int getLastSnapshotId();

    /**
     * Initializes the database schema that is going to be used for the data normalization
     */
    public void initialize();
    
    /**
     * Figures out if the connector is initialized
     * @return the initialization status
     */
    public boolean isInitialized();

    /**
     * Load the all normalized data from the SQL to the GoodData data package on the disk
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     */
    public void load(List<DLIPart> parts, String dir);

    /**
     * Load the normalized data of selected snapshots from the SQL to the GoodData data package on the disk.
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     * @param snapshotIds snapshot ids that are going to be loaded (if NULL, all snapshots are going to be loaded)
     */
    public void loadSnapshot(List<DLIPart> parts, String dir, int[] snapshotIds);


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
     * Extracts the source data CSV to the database where it is going to be transformed
     * @param dataFile the data file to extract
     * @param hasHeader true if the CSV file has header
     */
    public void extract(File dataFile, boolean hasHeader);

    /**
     * Project id getter
     * @return project id
     */
    public String getProjectId();

    /**
     * Project id setter
     * @param projectId project id
     */
    public void setProjectId(String projectId);

    /**
     * PDM schema getter
     * @return pdm schema
     */
    public PdmSchema getPdm();

    /**
     * PDM schema setter
     * @param schema PDM schema
     */
    public void setPdm(PdmSchema schema);

    /**
     * Frees all resources allocated by this connector
     */
    public void close();

}
