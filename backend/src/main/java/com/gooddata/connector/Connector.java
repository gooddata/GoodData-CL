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

import com.gooddata.integration.model.Column;
import com.gooddata.integration.model.SLI;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.processor.CliParams;
import com.gooddata.processor.Command;
import com.gooddata.processor.Executor;
import com.gooddata.processor.ProcessingContext;

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
    public String generateMaqlCreate();

    /**
     * LDM schema getter
     * @return LDM schema
     */
    public SourceSchema getSchema();

    /**
     * Extracts the source data CSV to the database where it is going to be transformed
     * @param dir target directory where the data package will be stored

     * @throws IOException in case of IO issues
     */
    public void extract(String dir) throws IOException;

    /**
     * Dumps the source data CSV to a file
     * @param file target file

     * @throws IOException in case of IO issues
     */
    public void dump(String file) throws IOException;

    /**
     * Extract data from the internal database and transfer them to a GoodData project
     * @param c command
     * @param pid project id
     * @param cc connector
     * @param p cli parameters
     * @param ctx current context
     * @param waitForFinish synchronous execution flag
     * @throws IOException IO issues
     * @throws InterruptedException internal problem with making file writable
     */
    public void extractAndTransfer(Command c, String pid, Connector cc,  boolean waitForFinish, CliParams p, ProcessingContext ctx)
    	throws IOException, InterruptedException;

    /**
     * LDM schema setter
     * @param schema LDM schema
     */
    public void setSchema(SourceSchema schema);

    /**
     * Create the GoodData data package with the ALL snapshots data
     * @param sli the SLI interface
     * @param columns the SLI columns
     * @param dir target directory where the data package will be stored
     * @param archiveName the name of the target ZIP archive
     * @throws IOException IO issues
     */
    public void deploy(SLI sli, List<Column> columns, String dir, String archiveName) throws IOException;

}
