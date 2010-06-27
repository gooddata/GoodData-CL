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

package com.gooddata.connector.driver;

import com.gooddata.connector.model.PdmSchema;
import com.gooddata.integration.model.DLIPart;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * GoodData SQL Executor
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public interface SqlDriver {

    /**
     * Executes the system DDL initialization. Creates functions, system tables etc.
     * @param c JDBC connection
     * @throws SQLException in case of db problems
     */
    public void executeSystemDdlSql(Connection c) throws SQLException;

    /**
     * Executes the DDL initialization. Creates the database schema that is required for the data normalization.
     * The schema is specific for the incoming data.
     * @param c JDBC connection
     * @param schema the PDM schema
     * @throws java.sql.SQLException in case of db problems
     */
    public void executeDdlSql(Connection c, PdmSchema schema) throws SQLException;

    /**
     * Executes the data normalization script
     * @param c JDBC connection
     * @param schema the PDM schema
     * @throws SQLException in case of db problems
     */
    public void executeNormalizeSql(Connection c, PdmSchema schema) throws SQLException;

    /**
     * Extracts the data from a CSV file to the database for normalization
     * @param c JDBC connection
     * @param schema the PDM schema
     * @param file extracted file
     * @throws SQLException in case of db problems
     */
    public void executeExtractSql(Connection c, PdmSchema schema, String file) throws SQLException;

    /**
     * Executes the Derby SQL that unloads the normalized data from the database to a CSV
     * @param c JDBC connection
     * @param schema PDM schema
     * @param part DLI part
     * @param dir target directory
     * @param snapshotIds specific snapshots IDs that will be integrated
     * @throws SQLException in case of db problems
     */
    public void executeLoadSql(Connection c, PdmSchema schema, DLIPart part, String dir, int[] snapshotIds)
            throws SQLException;


    /**
     * Executes the copying of the referenced lookup tables. This is used for REFERENCE lookups that are copies of the
     * associated CONNECTION POINT lookups.
     * @param c JDBC connection
     * @param schema the PDM schema
     * @throws java.sql.SQLException in case of db problems
     */
    public void executeLookupReplicationSql(Connection c, PdmSchema schema) throws SQLException;

    /**
     * {@inheritDoc}
     */
    public boolean exists(Connection c, String tbl) throws SQLException;

    /**
     * Returns true if the specified column of the specified table exists in the DB
     * @param c JDBC connection 
     * @param tbl table name
     * @param col column name
     * @return true if the table exists, false otherwise
     * @throws SQLException in case of a DB issue 
     */
    public boolean exists(Connection c, String tbl, String col) throws SQLException;

}