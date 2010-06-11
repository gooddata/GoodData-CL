package org.gooddata.connector.driver;

import com.gooddata.exceptions.ModelException;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.connector.model.PdmSchema;

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
     * Executes the system DDL initialization
     * @param c JDBC connection
     * @throws ModelException if there is a problem with the PDM schema (e.g. multiple source or fact tables)
     * @throws SQLException in case of db problems
     */
    public void executeSystemDdlSql(Connection c) throws ModelException, SQLException;

    /**
     * Executes the DDL initialization
     * @param c JDBC connection
     * @param schema the PDM schema
     * @throws com.gooddata.exceptions.ModelException if there is a problem with the PDM schema
     * (e.g. multiple source or fact tables)
     * @throws java.sql.SQLException in case of db problems
     */
    public void executeDdlSql(Connection c, PdmSchema schema) throws ModelException, SQLException;

    /**
     * Executes the data normalization script
     * @param c JDBC connection
     * @param schema the PDM schema
     * @throws ModelException if there is a problem with the PDM schema (e.g. multiple source or fact tables)
     * @throws SQLException in case of db problems
     */
    public void executeNormalizeSql(Connection c, PdmSchema schema) throws ModelException, SQLException;

    /**
     * Executes the Derby SQL that extracts the data from a CSV file to the normalization database
     * @param c JDBC connection
     * @param schema the PDM schema
     * @throws ModelException in case when there is a problem with the PDM model
     * @throws SQLException in case of db problems
     */
    public void executeExtractSql(Connection c, PdmSchema schema, String file) throws ModelException, SQLException;

    /**
     * Executes the Derby SQL that unloads the data from the normalization database to a CSV
     * @param c JDBC connection
     * @param part DLI part
     * @param dir target directory
     * @param snapshotIds specific snapshots IDs that will be integrated
     * @throws SQLException in case of db problems
     */
    public void executeLoadSql(Connection c, PdmSchema schema, DLIPart part, String dir, int[] snapshotIds)
            throws ModelException, SQLException;


    /**
     * Executes the copying of the referenced lookup tables
     * @param c JDBC connection
     * @param schema the PDM schema
     * @throws com.gooddata.exceptions.ModelException if there is a problem with the PDM schema (e.g. multiple source or fact tables)
     * @throws java.sql.SQLException in case of db problems
     */
    public void executeLookupReplicationSql(Connection c, PdmSchema schema) throws ModelException, SQLException;

}
