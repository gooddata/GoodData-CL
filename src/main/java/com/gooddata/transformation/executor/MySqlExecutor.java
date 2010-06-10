package com.gooddata.transformation.executor;

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
import org.gooddata.transformation.executor.AbstractSqlExecutor;
import org.gooddata.transformation.executor.SqlExecutor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * GoodData Derby SQL executor. Generates the DDL (tables and indexes), DML (transformation SQL) and other
 * SQL statements necessary for the data normalization (lookup generation)
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class MySqlExecutor extends AbstractSqlExecutor implements SqlExecutor {
    //TODO: refactor
    private static Logger l = Logger.getLogger(MySqlExecutor.class);

    // separates the different LABELs when we concatenate them to create an unique identifier out of them
    protected static final String HASH_SEPARATOR = "%";
    // Derby SQL concat operator to merge LABEL content
    protected static final String CONCAT_OPERATOR = ",'" + HASH_SEPARATOR + "',";

    /**
     * Default constructor
     */
    public MySqlExecutor() {
        // autoincrement syntax
        SYNTAX_AUTOINCREMENT = "AUTO_INCREMENT";
    }
    
    /**
     * Executes the data normalization script
     * @param c JDBC connection
     * @param schema the PDM schema
     * @throws com.gooddata.exceptions.ModelException if there is a problem with the PDM schema
     * (e.g. multiple source or fact tables)
     * @throws SQLException in case of db problems
     */
    public void executeNormalizeSql(Connection c, PdmSchema schema) throws ModelException, SQLException {

        //populate REFERENCEs lookups from the referenced lookups
        executeLookupReplicationSql(c, schema);

        List<String> usql = new ArrayList<String>();
        // fact table INSERT statement components
        PdmTable factTable = schema.getFactTable();
        for(PdmTable lookupTable : schema.getLookupTables()) {
            // INSERT tbl(insertColumns) SELECT nestedSelectColumns FROM nestedSelectFromClause
            // WHERE nestedSelectWhereClause
            String insertColumns = "hashid";
            // fact table cols
            String nestedSelectColumns = "";
            // concatenate all representing columns to create a unique hashid
            String concatenatedRepresentingColumns = "";
            for(PdmColumn column : lookupTable.getRepresentingColumns()) {
                insertColumns += "," + column.getName();
                nestedSelectColumns += "," + column.getSourceColumn();
                // if there are LABELS, the lookup can't be added twice to the FROM clause
                if(concatenatedRepresentingColumns.length() > 0)
                    concatenatedRepresentingColumns += CONCAT_OPERATOR +  column.getSourceColumn();
                else
                    concatenatedRepresentingColumns = column.getSourceColumn();

            }

            concatenatedRepresentingColumns =  "CONCAT(" + concatenatedRepresentingColumns + ") ";

            // add the concatenated columns that fills the hashid to the beginning
            nestedSelectColumns = concatenatedRepresentingColumns + nestedSelectColumns;

            JdbcUtil.executeUpdate(c,
                "INSERT INTO " + lookupTable.getName() + "(" + insertColumns +
                ") SELECT DISTINCT " + nestedSelectColumns + " FROM " + schema.getSourceTable().getName() +
                " WHERE o_genid > (SELECT MAX(lastid) FROM snapshots WHERE name='" + factTable.getName() +
                "') AND " + concatenatedRepresentingColumns + " NOT IN (SELECT hashid FROM " +
                lookupTable.getName() + ")"
            );

            usql.add("UPDATE " + factTable.getName() + " SET  " + lookupTable.getRepresentedLookupColumn() +
                          "_id = (SELECT id FROM " + lookupTable.getName() + " d," + schema.getSourceTable().getName() +
                          " o WHERE " + concatenatedRepresentingColumns + " = d.hashid AND o.o_genid = " +
                          factTable.getName() + ".id) WHERE id > (SELECT MAX(lastid) FROM snapshots WHERE name = '" +
                          factTable.getName()+"')");
        }

        for(PdmTable lookupTable : schema.getConnectionPointTables()) {
            // INSERT tbl(insertColumns) SELECT nestedSelectColumns FROM nestedSelectFromClause
            // WHERE nestedSelectWhereClause
            String insertColumns = "id,hashid";
            // fact table cols
            String nestedSelectColumns = "";
            // concatenate all representing columns to create a unique hashid
            String concatenatedRepresentingColumns = "";
            for(PdmColumn column : lookupTable.getRepresentingColumns()) {
                insertColumns += "," + column.getName();
                nestedSelectColumns += "," + column.getSourceColumn();
                // if there are LABELS, the lookup can't be added twice to the FROM clause
                if(concatenatedRepresentingColumns.length() > 0)
                    concatenatedRepresentingColumns += CONCAT_OPERATOR +  column.getSourceColumn();
                else
                    concatenatedRepresentingColumns = column.getSourceColumn();

            }
            concatenatedRepresentingColumns =  "CONCAT(" + concatenatedRepresentingColumns + ") ";

            // add the concatenated columns that fills the hashid to the beginning
            nestedSelectColumns = "o_genid," + concatenatedRepresentingColumns + nestedSelectColumns;

            /*
            JdbcUtil.executeUpdate(c,
                "INSERT INTO " + lookupTable.getName() + "(" + insertColumns +
                ") SELECT DISTINCT " + nestedSelectColumns + " FROM " + schema.getSourceTable().getName() +
                " WHERE o_genid > (SELECT MAX(lastid) FROM snapshots WHERE name='" + factTable.getName() +
                "') AND " + concatenatedRepresentingColumns + " NOT IN (SELECT hashid FROM " +
                lookupTable.getName() + ")"
            );
            */
            // TODO: when snapshotting, there are duplicate CONNECTION POINT VALUES
            // we need to decide if we want to accumultae the connection point lookup or not
            JdbcUtil.executeUpdate(c,
                "INSERT INTO " + lookupTable.getName() + "(" + insertColumns +
                ") SELECT DISTINCT " + nestedSelectColumns + " FROM " + schema.getSourceTable().getName() +
                " WHERE o_genid > (SELECT MAX(lastid) FROM snapshots WHERE name='" + factTable.getName() +
                "')"
            );

        }

        for(PdmTable lookupTable : schema.getReferenceTables()) {
            // concatenate all representing columns to create a unique hashid
            String concatenatedRepresentingColumns = "";
            for(PdmColumn column : lookupTable.getRepresentingColumns()) {
                if(concatenatedRepresentingColumns.length() > 0)
                    concatenatedRepresentingColumns += CONCAT_OPERATOR +  column.getSourceColumn();
                else
                    concatenatedRepresentingColumns = column.getSourceColumn();
            }
            concatenatedRepresentingColumns =  "CONCAT(" + concatenatedRepresentingColumns + ") ";

            usql.add("UPDATE " + factTable.getName() + " SET  " + lookupTable.getRepresentedLookupColumn() +
                          "_id = (SELECT id FROM " + lookupTable.getName() + " d," + schema.getSourceTable().getName() +
                          " o WHERE " + concatenatedRepresentingColumns + " = d.hashid AND o.o_genid = " +
                          factTable.getName() + ".id) WHERE id > (SELECT MAX(lastid) FROM snapshots WHERE name = '" +
                          factTable.getName()+"')");

        }

        String insertColumns = "";
        String nestedSelectColumns = "";
        for(PdmColumn factTableColumn : factTable.getColumns()) {
            if(insertColumns.length() >0) {
                insertColumns += "," + factTableColumn.getName();
                nestedSelectColumns += "," + factTableColumn.getSourceColumn();
            }
            else {
                insertColumns += factTableColumn.getName();
                nestedSelectColumns += factTableColumn.getSourceColumn();
            }
        }

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

        //script += "DELETE FROM snapshots WHERE name = '" + factTable.getName() + "' AND lastid = 0;\n\n";
        Date dt = new Date();
        JdbcUtil.executeUpdate(c,
            "INSERT INTO snapshots(name,tmstmp,firstid) SELECT '" + factTable.getName() + "'," + dt.getTime() +
            ",MAX(id)+1 FROM " + factTable.getName()
        );

        JdbcUtil.executeUpdate(c,
            "UPDATE snapshots SET firstid = 0 WHERE name = '" + factTable.getName() + "' AND firstid IS NULL"
        );

        JdbcUtil.executeUpdate(c,
            "INSERT INTO " + factTable.getName() + "(id" + factColumns + ") SELECT o_genid" + sourceColumns +
            " FROM " + schema.getSourceTable().getName() +
            " WHERE o_genid > (SELECT MAX(lastid) FROM snapshots WHERE name='"+factTable.getName()+"')"
        );

        for(String s : usql) {
            JdbcUtil.executeUpdate(c, s);
        }

        JdbcUtil.executeUpdate(c,
            "UPDATE snapshots SET lastid = (SELECT MAX(id) FROM " + factTable.getName() + ") WHERE name = '" +
            factTable.getName() + "' AND lastid IS NULL"
        );

        JdbcUtil.executeUpdate(c,
            "UPDATE snapshots SET lastid = 0 WHERE name = '" + factTable.getName() + "' AND lastid IS NULL"
        );

    }

    /**
     * Executes the Derby SQL that extracts the data from a CSV file to the normalization database
     * @param c JDBC connection
     * @param schema the PDM schema
     * @throws ModelException in case when there is a problem with the PDM model
     * @throws SQLException in case of db problems
     */
    public void executeExtractSql(Connection c, PdmSchema schema, String file) throws ModelException, SQLException {
        String cols = "";
        PdmTable sourceTable = schema.getSourceTable();
        for (PdmColumn col : sourceTable.getColumns()) {
            if(!col.isAutoIncrement())
                if (cols != null && cols.length() > 0)
                    cols += "," + StringUtil.formatShortName( col.getName());
                else
                    cols += StringUtil.formatShortName(col.getName());
        }

        JdbcUtil.executeUpdate(c,
            "LOAD DATA INFILE '" + file + "' INTO TABLE " + sourceTable.getName().toUpperCase() + " CHARACTER SET UTF8 "+
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
        String sql = "";
        int rc = 0;

        String file = dir + System.getProperty("file.separator") + part.getFileName();
        String dliTable = StringUtil.formatShortName(part.getFileName().split("\\.")[0]);

        PdmTable pdmTable = schema.getTableByName(dliTable);

        List<Column> columns = part.getColumns();
        String cols = "";
        for (Column cl : columns) {
            PdmColumn col = pdmTable.getColumnByName(cl.getName());
            // fact table fact columns
            if(PdmTable.PDM_TABLE_TYPE_FACT.equals(pdmTable.getType()) &&
                    SourceColumn.LDM_TYPE_FACT.equals(col.getLdmTypeReference())) {
                if (cols != null && cols.length() > 0)
                    cols += "," + dliTable.toUpperCase() + "." +
                            StringUtil.formatShortName(cl.getName());
                else
                    cols +=  dliTable.toUpperCase() + "." +
                            StringUtil.formatShortName(cl.getName());
            }
            // lookup table name column
            else if (PdmTable.PDM_TABLE_TYPE_LOOKUP.equals(pdmTable.getType()) &&
                    SourceColumn.LDM_TYPE_ATTRIBUTE.equals(col.getLdmTypeReference())) {
                if (cols != null && cols.length() > 0)
                    cols += "," + dliTable.toUpperCase() + "." + StringUtil.formatShortName(cl.getName());
                else
                    cols +=  dliTable.toUpperCase() + "." + StringUtil.formatShortName(cl.getName());
            }
            else {
                if (cols != null && cols.length() > 0)
                    cols += "," + dliTable.toUpperCase() + "." + StringUtil.formatShortName(cl.getName());
                else
                    cols +=  dliTable.toUpperCase() + "." + StringUtil.formatShortName(cl.getName());
            }
        }
        String whereClause = "";
        if(PdmTable.PDM_TABLE_TYPE_FACT.equals(pdmTable.getType()) && snapshotIds != null && snapshotIds.length > 0) {
            String inClause = "";
            for(int i : snapshotIds) {
                if(inClause.length()>0)
                    inClause += ","+i;
                else
                    inClause = "" + i;
            }
            whereClause = ",SNAPSHOTS WHERE " + dliTable.toUpperCase() +
                    ".ID BETWEEN SNAPSHOTS.FIRSTID and SNAPSHOTS.LASTID AND SNAPSHOTS.ID IN (" + inClause + ")";
        }

        ResultSet rs = JdbcUtil.executeQuery(c,
            "SELECT " + cols + " INTO OUTFILE '" + file +
            "' FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\\n' FROM " +
            dliTable.toUpperCase() + whereClause
        );
        rs.close();

    }

}
