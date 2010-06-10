package com.gooddata.connector.executor;

import com.gooddata.exceptions.ModelException;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.connector.model.PdmColumn;
import com.gooddata.connector.model.PdmSchema;
import com.gooddata.connector.model.PdmTable;
import com.gooddata.util.JdbcUtil;
import com.gooddata.util.StringUtil;
import org.apache.log4j.Logger;
import org.gooddata.connector.executor.AbstractSqlExecutor;
import org.gooddata.connector.executor.SqlExecutor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * GoodData Derby SQL executor. Generates the DDL (tables and indexes), DML (transformation SQL) and other
 * SQL statements necessary for the data normalization (lookup generation)
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class MySqlExecutor extends AbstractSqlExecutor implements SqlExecutor {
    //TODO: refactor
    private static Logger l = Logger.getLogger(MySqlExecutor.class);

 /**
     * Default constructor
     */
    public MySqlExecutor() {
        // autoincrement syntax
        SYNTAX_AUTOINCREMENT = "AUTO_INCREMENT";
        SYNTAX_CONCAT_FUNCTION_PREFIX = "CONCAT(";
        SYNTAX_CONCAT_FUNCTION_SUFFIX = ")";
        SYNTAX_CONCAT_OPERATOR = ",'" + HASH_SEPARATOR + "',";
    }

    /**
     * Executes the Derby SQL that extracts the data from a CSV file to the normalization database
     * @param c JDBC connection
     * @param schema the PDM schema
     * @throws ModelException in case when there is a problem with the PDM model
     * @throws SQLException in case of db problems
     */
    public void executeExtractSql(Connection c, PdmSchema schema, String file) throws ModelException, SQLException {
        PdmTable sourceTable = schema.getSourceTable();
        String source = sourceTable.getName();
        String cols = getNonAutoincrementColumns(sourceTable);
        JdbcUtil.executeUpdate(c,
            "LOAD DATA INFILE '" + file + "' INTO TABLE " + source.toUpperCase() + " CHARACTER SET UTF8 "+
            "COLUMNS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\\n' (" + cols + ")"
        );
    }

    /**
     * Executes the Derby SQL that unloads the data from the normalization database to a CSV
     * @param c JDBC connection
     * @param part DLI part
     * @param dir target directory
     * @param snapshotIds specific snapshots IDs that will be integrated
     * @throws SQLException in case of db problems
     */
    public void executeLoadSql(Connection c, PdmSchema schema, DLIPart part, String dir, int[] snapshotIds)
            throws ModelException, SQLException {
        String file = dir + System.getProperty("file.separator") + part.getFileName();
        String cols = getLoadColumns(part, schema);
        String whereClause = getLoadWhereClause(part, schema, snapshotIds);
        String dliTable = getTableNameFromPart(part);
        ResultSet rs = JdbcUtil.executeQuery(c,
            "SELECT " + cols + " INTO OUTFILE '" + file +
            "' FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\\n' FROM " +
            dliTable.toUpperCase() + whereClause
        );
        rs.close();
    }

    protected void insertFactsToFactTable(Connection c, PdmSchema schema) throws ModelException, SQLException {
        PdmTable factTable = schema.getFactTable();
        PdmTable sourceTable = schema.getFactTable();
        String fact = factTable.getName();
        String source = sourceTable.getName();
        String factColumns = "";
        String sourceColumns = "";
        for(PdmColumn column : factTable.getFactColumns()) {
            factColumns += "," + column.getName();
            sourceColumns += "," + column.getSourceColumn();
        }

        for(PdmColumn column : factTable.getDateColumns()) {
            factColumns += "," + column.getName();
            sourceColumns += ",DATEDIFF(STR_TO_DATE(" + column.getSourceColumn() + ",'" +
                    StringUtil.convertJavaDateFormatToMySql(column.getFormat())+"'),'1900-01-01')+1";
        }
        JdbcUtil.executeUpdate(c,
            "INSERT INTO "+fact+"(id"+factColumns+") SELECT o_genid" + sourceColumns +
            " FROM " + source + " WHERE o_genid > (SELECT MAX(lastid) FROM snapshots WHERE name='"+fact+"')"
        );
    }

}
