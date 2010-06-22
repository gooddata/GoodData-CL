/*
 * Copyright (c) 2009 GoodData Corporation.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Redistributions in any form must be accompanied by information on
 *    how to obtain complete source code for this software and any
 *    accompanying software that uses this software.  The source code
 *    must either be included in the distribution or be available for no
 *    more than the cost of distribution plus a nominal fee, and must be
 *    freely redistributable under reasonable conditions.  For an
 *    executable file, complete source code means the source code for all
 *    modules it contains.  It does not include source code for modules or
 *    files that typically accompany the major components of the operating
 *    system on which the executable file runs.
 *
 * THIS SOFTWARE IS PROVIDED BY GOODDATA ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT, ARE DISCLAIMED.  IN NO EVENT SHALL ORACLE BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.gooddata.connector.driver;

import java.sql.Connection;
import java.sql.SQLException;

import com.gooddata.connector.model.PdmSchema;
import com.gooddata.integration.model.DLIPart;

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
     * @throws SQLException in case of db problems
     */
    public void executeExtractSql(Connection c, PdmSchema schema, String file) throws SQLException;

    /**
     * Executes the Derby SQL that unloads the normalized data from the database to a CSV
     * @param c JDBC connection
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
     * @param tbl table name
     * @param col column name
     * @return true if the table exists, false otherwise
     */
    public boolean exists(Connection c, String tbl, String col) throws SQLException;

}
