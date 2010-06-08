package com.gooddata.transformation.executor;

import com.gooddata.exceptions.ModelException;
import com.gooddata.transformation.executor.model.PdmColumn;
import com.gooddata.transformation.executor.model.PdmSchema;
import com.gooddata.transformation.executor.model.PdmTable;
import com.gooddata.util.JdbcUtil;
import org.gooddata.transformation.executor.SqlExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * GoodData Derby SQL executor. Generates the DDL (tables and indexes), DML (transformation SQL) and other
 * SQL statements necessary for the data normalization (lookup generation)
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class DerbySqlExecutorUpdate extends DerbySqlExecutor implements SqlExecutor {

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
            sourceColumns += ",DTTOI(" + column.getSourceColumn() + ",'"+column.getFormat()+"')";
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

}