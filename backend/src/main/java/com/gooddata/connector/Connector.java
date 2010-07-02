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

package com.gooddata.connector;

import com.gooddata.integration.model.DLI;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.processor.Executor;

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
     * @param columns columns that the MAQL is generated for
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
