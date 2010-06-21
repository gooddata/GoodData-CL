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
