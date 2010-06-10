package org.gooddata.transformation.executor;

import com.gooddata.exceptions.ModelException;
import com.gooddata.integration.model.Column;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.transformation.executor.model.PdmColumn;
import com.gooddata.transformation.executor.model.PdmLookupReplication;
import com.gooddata.transformation.executor.model.PdmSchema;
import com.gooddata.transformation.executor.model.PdmTable;
import com.gooddata.util.JdbcUtil;
import com.gooddata.util.StringUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * GoodData abstract SQL executor. Generates the DDL (tables and indexes), DML (transformation SQL) and other
 * SQL statements necessary for the data normalization (lookup generation)
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public abstract class AbstractSqlExecutor implements SqlExecutor {

    private static Logger l = Logger.getLogger(AbstractSqlExecutor.class);

    // autoincrement syntax
    protected String SYNTAX_AUTOINCREMENT = "";

    /**
     * Executes the system DDL initialization
     * @param c JDBC connection
     * @throws ModelException if there is a problem with the PDM schema (e.g. multiple source or fact tables)
     * @throws SQLException in case of db problems
     */
    public void executeSystemDdlSql(Connection c) throws ModelException, SQLException {
        createSnapshotTable(c);
    }

    /**
     * Executes the DDL initialization
     * @param c JDBC connection
     * @param schema the PDM schema
     * @throws ModelException if there is a problem with the PDM schema (e.g. multiple source or fact tables)
     * @throws SQLException in case of db problems
     */
    public void executeDdlSql(Connection c, PdmSchema schema) throws ModelException, SQLException {
        for(PdmTable table : schema.getTables()) {
            createTable(c, table);
            if(PdmTable.PDM_TABLE_TYPE_SOURCE.equals(table.getType()))
                indexAllTableColumns(c, table);
        }
        JdbcUtil.executeUpdate(c,
            "INSERT INTO snapshots(name,firstid,lastid,tmstmp) VALUES ('" + schema.getFactTable().getName() + "',0,0,0)"
        );
    }

    /**
     * Executes the copying of the referenced lookup tables
     * @param c JDBC connection
     * @param schema the PDM schema
     * @throws com.gooddata.exceptions.ModelException if there is a problem with the PDM schema (e.g. multiple source or fact tables)
     * @throws java.sql.SQLException in case of db problems
     */
    public void executeLookupReplicationSql(Connection c, PdmSchema schema) throws ModelException, SQLException {
        for (PdmLookupReplication lr : schema.getLookupReplications()) {
            JdbcUtil.executeUpdate(c,
                "DELETE FROM " + lr.getReferencingLookup()
            );
            JdbcUtil.executeUpdate(c,
                "INSERT INTO " + lr.getReferencingLookup() + "(id," + lr.getReferencingColumn() +",hashid)" +
                " SELECT id," + lr.getReferencedColumn() + "," + lr.getReferencedColumn() + " FROM " + lr.getReferencedLookup()
            );
        }
    }

    protected void indexAllTableColumns(Connection c, PdmTable table) throws SQLException {
        for( PdmColumn column : table.getColumns()) {
            if(!column.isPrimaryKey() && !column.isUnique())
                JdbcUtil.executeUpdate(c,"CREATE INDEX idx_" + table.getName() + "_" + column.getName() + " ON " +
                              table.getName() + "("+column.getName()+")");
        }
    }

    protected void createTable(Connection c, PdmTable table) throws SQLException {
        String pk = "";
        String sql = "CREATE TABLE " + table.getName() + " (\n";
        for( PdmColumn column : table.getColumns()) {
            sql += " "+ column.getName() + " " + column.getType();
            if(column.isUnique())
                sql += " UNIQUE";
            if(column.isAutoIncrement())
                sql += " " + SYNTAX_AUTOINCREMENT;
            if(column.isPrimaryKey())
                if(pk != null && pk.length() > 0)
                    pk += "," + column.getName();
                else
                    pk += column.getName();
            sql += ",";
        }
        sql += " PRIMARY KEY (" + pk + "))";

        JdbcUtil.executeUpdate(c, sql);
    }

    protected void createSnapshotTable(Connection c) throws SQLException {
        JdbcUtil.executeUpdate(c,
            "CREATE TABLE snapshots (" +
                " id INT " + SYNTAX_AUTOINCREMENT + "," +
                " name VARCHAR(255)," +
                " tmstmp BIGINT," +
                " firstid INT," +
                " lastid INT," +
                " PRIMARY KEY (id)" +
                ")"
        );
    }

}