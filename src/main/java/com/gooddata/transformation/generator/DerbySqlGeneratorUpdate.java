package com.gooddata.transformation.generator;

import com.gooddata.exceptions.ModelException;
import com.gooddata.integration.model.Column;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.transformation.generator.model.PdmColumn;
import com.gooddata.transformation.generator.model.PdmSchema;
import com.gooddata.transformation.generator.model.PdmTable;
import com.gooddata.util.StringUtil;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * GoodData Derby SQL generator. Generates the DDL (tables and indexes), DML (transformation SQL) and other
 * SQL statements necessary for the data normalization (lookup generation)
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class DerbySqlGeneratorUpdate extends DerbySqlGenerator {

    /**
     * Generates the data normalization script
     * @param schema the PDM schema
     * @return the SQL script
     * @throws com.gooddata.exceptions.ModelException if there is a problem with the PDM schema (e.g. multiople source or fact tables)
     */
    public String generateNormalizeSql(PdmSchema schema) throws ModelException {
        String script = "";
        // fact table INSERT statement components
        PdmTable factTable = schema.getFactTable();
        String uScript = "";
        for(PdmTable lookupTable : schema.getLookupTables()) {
            // INSERT tbl(insertColumns) SELECT nestedSelectColumns FROM nestedSelectFromClause
            // WHERE nestedSelectWhereClause
            String insertColumns = "hashid";
            // fact table cols
            String nestedSelectColumns = "";
            // new fact table insert's nested select from
            String nestedSelectFromClause = "";
            // concatenate all representing columns to create a unique hashid
            String concatenatedRepresentingColumns = "";
            // new fact table insert's nested select where
            String nestedSelectWhereClause = "";
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

            script += "INSERT INTO " + lookupTable.getName() + "(" + insertColumns +
                      ") SELECT DISTINCT " + nestedSelectColumns + " FROM " + schema.getSourceTable().getName() +
                      " WHERE o_genid > (SELECT MAX(lastid) FROM snapshots WHERE name='" + factTable.getName() +
                       "') AND " + concatenatedRepresentingColumns + " NOT IN (SELECT hashid FROM " +
                       lookupTable.getName() + ");\n\n";

            uScript += "UPDATE " + factTable.getName() + " SET  " + lookupTable.getRepresentedLookupColumn() +
                          "_id = (SELECT id FROM " + lookupTable.getName() + " d," + schema.getSourceTable().getName() +
                          " o WHERE " + concatenatedRepresentingColumns + " = d.hashid AND o.o_genid = " +
                          factTable.getName() + ".id) WHERE id > (SELECT MAX(lastid) FROM snapshots WHERE name = '" +
                          factTable.getName()+"');\n\n";
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
            if(factColumns.length() > 0) {
                factColumns += "," + column.getName();
                sourceColumns += "," + column.getSourceColumn();
            }
            else {
                factColumns += column.getName();
                sourceColumns += column.getSourceColumn();
            }
        }

        for(PdmColumn column : factTable.getDateColumns()) {
            if(factColumns.length() > 0) {
                factColumns += "," + column.getName();
                sourceColumns += ",DTTOI(" + column.getSourceColumn() + ",'"+column.getFormat()+"')";
            }
            else {
                factColumns += column.getName();
                sourceColumns += "DTTOI(" + column.getSourceColumn() + ",'"+column.getFormat()+"')";
            }
        }

        //script += "DELETE FROM snapshots WHERE name = '" + factTable.getName() + "' AND lastid = 0;\n\n";
        Date dt = new Date();
        script += "INSERT INTO snapshots(name,tmstmp,firstid) SELECT '" + factTable.getName() + "'," + dt.getTime()
                   + ",MAX(id)+1 FROM " + factTable.getName() + ";\n\n";
        script += "UPDATE snapshots SET firstid = 0 WHERE name = '" + factTable.getName() + "' AND firstid IS NULL;\n\n";

        script += "INSERT INTO " + factTable.getName() + "(id," + factColumns + ") SELECT o_genid," + sourceColumns +
                  " FROM " + schema.getSourceTable().getName() +
                  " WHERE o_genid > (SELECT MAX(lastid) FROM snapshots WHERE name='"+factTable.getName()+"');\n\n";

        script += uScript;

        script += "UPDATE snapshots SET lastid = (SELECT MAX(id) FROM " + factTable.getName() + ") WHERE name = '" +
                  factTable.getName() + "' AND lastid IS NULL;\n\n";
        script += "UPDATE snapshots SET lastid = 0 WHERE name = '" + factTable.getName() + "' AND lastid IS NULL;\n\n";

        return script;
    }

}