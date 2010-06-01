package com.gooddata.transformation.generator;

import com.gooddata.integration.model.Column;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.util.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * GoodData Derby SQL generator. Generates the DDL (tables and indexes), DML (transformation SQL) and other
 * SQL statements necessary for the data normalization (lookup generation)
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class DerbySqlGenerator {

    // separates the different LABELs when we concatenate them to create an unique identifier out of them
    private static final String HASH_SEPARATOR = "%";
    // Derby SQL concat operator to merge LABEL content
    private static final String CONCAT_OPERATOR = " || '" + HASH_SEPARATOR + "' || ";

    /**
     * Generates the Derby SQL tables and indexes that accommodate the normalization
     * @param schema the LDM schema
     * @return the SQL script
     */
    public String generateDDL(SourceSchema schema) {
        String ssn = StringUtil.formatShortName(schema.getName());
        String script = "CREATE FUNCTION ATOD(str VARCHAR(255)) RETURNS DOUBLE\n" +
                " PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA" +
                " EXTERNAL NAME 'com.gooddata.derby.extension.DerbyExtensions.atod';\n\n";
        script += "CREATE TABLE snapshots (\n"+
                        " id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n" +
                        " name VARCHAR(255),\n tmstmp BIGINT,\n firstid INT,\n lastid INT,\n PRIMARY KEY (id));\n\n";
        script += "CREATE TABLE o_" + ssn + " (\n";
        // fact table
        String fscript = "CREATE TABLE f_" + ssn + " (\n";
        script += " genid INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n";
        fscript += " id INT NOT NULL,\n";

        HashMap<String, List<SourceColumn>> lookups = new HashMap<String, List<SourceColumn>>();

        for (SourceColumn column : schema.getColumns()) {
            String scn = StringUtil.formatShortName(column.getName());
            if (column.getLdmType().equals(SourceColumn.LDM_TYPE_ATTRIBUTE)) {
                script += " o_" + scn + " VARCHAR(255),\n";
                fscript += " " + scn + "_id INT,\n";
                if (!lookups.containsKey(scn)) {
                    lookups.put(scn, new ArrayList<SourceColumn>());
                }
                List<SourceColumn> l = lookups.get(scn);
                l.add(column);
            }
            if (column.getLdmType().equals(SourceColumn.LDM_TYPE_LABEL)) {
                script += " o_" + scn + " VARCHAR(255),\n";
                String scnPk = StringUtil.formatShortName(column.getPk());
                if (!lookups.containsKey(scnPk)) {
                    lookups.put(scnPk, new ArrayList<SourceColumn>());
                }
                List<SourceColumn> l = lookups.get(scnPk);
                l.add(column);
            }
            if (column.getLdmType().equals(SourceColumn.LDM_TYPE_FACT)) {
                script += " o_" + scn + " VARCHAR(255),\n";
                fscript += " f_" + scn + " VARCHAR(255),\n";
            }
        }
        script += " PRIMARY KEY (genid)\n);\n";
        fscript += " PRIMARY KEY (id)\n);\n\n";

        script += "\n" + fscript + "\n";

        for (String column : lookups.keySet()) {
            script += "CREATE TABLE d_" + ssn + "_" + column + "(\n";
            script += " id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n";
            script += " hashid VARCHAR(1000) UNIQUE,\n";
            List<SourceColumn> l = lookups.get(column);
            for (SourceColumn c : l) {
                String scnNm = StringUtil.formatShortName(c.getName());
                script += " nm_" + scnNm + " VARCHAR(255),\n";
            }
            script += " PRIMARY KEY (id)\n);\n\n";
        }

        return script;
    }

    /**
     * Generates the Derby SQL statements that perform the normalization
     * @param schema the LDM schema
     * @return the SQL script
     */
    public String generateDML(SourceSchema schema) {
        String script = "";
        String ssn = StringUtil.formatShortName(schema.getName());
        HashMap<String, List<SourceColumn>> lookups = new HashMap<String, List<SourceColumn>>();
        List<SourceColumn> facts = new ArrayList<SourceColumn>();

        for (SourceColumn column : schema.getColumns()) {
            String scn = StringUtil.formatShortName(column.getName());
            if (column.getLdmType().equals(SourceColumn.LDM_TYPE_ATTRIBUTE)) {
                if (!lookups.containsKey(scn)) {
                    lookups.put(scn, new ArrayList<SourceColumn>());
                }
                List<SourceColumn> l = lookups.get(scn);
                l.add(column);
            }
            if (column.getLdmType().equals(SourceColumn.LDM_TYPE_LABEL)) {
                String scnPk = StringUtil.formatShortName(column.getPk());
                if (!lookups.containsKey(scnPk)) {
                    lookups.put(scnPk, new ArrayList<SourceColumn>());
                }
                List<SourceColumn> l = lookups.get(scnPk);
                l.add(column);
            }
            if (column.getLdmType().equals(SourceColumn.LDM_TYPE_FACT)) {
                facts.add(column);
            }
        }
        // source table cols
        String fcols = "";
        // fact table cols
        String nfcols = "";
        // new fact table insert's nested select from
        String fromClause = "";
        // new fact table insert's nested select where
        String whereClause = "";
        for (String column : lookups.keySet()) {
            String scn = StringUtil.formatShortName(column);
            if (fcols.length() > 0) {
                fcols += ",d_" + ssn + "_" + scn + ".id AS " + scn + "_id";
                nfcols += "," + scn + "_id";
                fromClause += ",d_" + ssn + "_" + scn;
            } else {
                fcols = "o_" + ssn + ".genid,d_" + ssn + "_" + scn + ".id AS " + scn + "_id";
                nfcols = "id," + scn + "_id";
                fromClause += "o_" + ssn + ",d_" + ssn + "_" + scn;
            }
            List<SourceColumn> l = lookups.get(column);
            // lookup cols
            String lcols = "";
            // original table cols
            String cols = "";
            for (SourceColumn c : l) {
                String scnNm = StringUtil.formatShortName(c.getName());
                if (lcols.length() > 0) {
                    lcols += CONCAT_OPERATOR + "nm_" + scnNm;
                    cols += CONCAT_OPERATOR + "o_" + ssn + "." + "o_" + scnNm;
                } else {
                    lcols = "nm_" + scnNm;
                    cols = "o_" + ssn + "." + "o_" + scnNm;
                }
            }

            if (whereClause.length() > 0) {
                whereClause += " AND " + cols + " = d_" + ssn + "_" + scn + ".hashid";
            } else {
                whereClause = cols + " = d_" + ssn + "_" + scn + ".hashid";
            }
            //script += "DELETE FROM d_" + ssn + "_" + scn + ";";
            script += "INSERT INTO d_" + ssn + "_" + scn + "(" + lcols.replace(CONCAT_OPERATOR, ",") +
                      ",hashid) SELECT DISTINCT " + cols.replace(CONCAT_OPERATOR, ",") + "," + cols +
                      " FROM " + "o_" + ssn + " WHERE "+ cols + " NOT IN (SELECT hashid FROM d_" +
                      ssn + "_" + scn + ");\n\n";
        }

        for (SourceColumn column : facts) {
            String scn = StringUtil.formatShortName(column.getName());
            if (fcols.length() > 0) {
                fcols += "," + "o_" + scn;
                nfcols += "," + "f_" + scn;
            } else {
                fcols = "genid," + "o_" + scn;
                nfcols = "id," + "f_" + scn;
            }
        }

        if (whereClause.length() > 0) {
            whereClause += " AND " + "o_" + ssn + ".genid NOT IN (SELECT id FROM f_" + ssn + ")";
        } else {
            whereClause += "o_" + ssn + ".genid NOT IN (SELECT id FROM f_" + ssn + ")";
        }
        //delete the unfinished snapshots
        script += "DELETE FROM snapshots WHERE name = 'f_" + ssn
                  + "' AND lastid IS NULL;\n\n";
        Date dt = new Date();
        script += "INSERT INTO snapshots(name,tmstmp,firstid) SELECT 'f_" + ssn + "'," + dt.getTime()
                   + ",MAX(id)+1 FROM f_" + ssn + ";\n\n";
        script += "UPDATE snapshots SET firstid = 0 WHERE name = 'f_" + ssn + "' AND firstid IS NULL;\n\n";
        script += "INSERT INTO f_" + ssn + "(" + nfcols + ") SELECT " + fcols + " FROM " + fromClause +
                  " WHERE " + whereClause + ";\n\n";
        script += "UPDATE snapshots SET lastid = (SELECT MAX(id) FROM f_" + ssn + ") WHERE name = 'f_" + ssn
                  + "' AND lastid IS NULL;\n\n";
        return script;
    }

    /**
     * Generates the Derby SQL that extracts the data from a CSV file to the normalization database
     * @param schema the LDM schema
     * @return the SQL script
     */
    public String generateExtract(SourceSchema schema, String file) {
        String ssn = StringUtil.formatShortName(schema.getName());
        String cols = "";
        for (SourceColumn c : schema.getColumns()) {
            if (cols != null && cols.length() > 0)
                cols += "," + StringUtil.formatShortName("o_" + c.getName());
            else
                cols += StringUtil.formatShortName("o_" + c.getName());
        }
        return "CALL SYSCS_UTIL.SYSCS_IMPORT_DATA " +
                "(NULL, '" + ("o_" + ssn).toUpperCase() + "', '" + cols.toUpperCase() + 
                "', null, '" + file + "', null, null, null,0);\n\n";
    }

    /**
     * Generates the Derby SQL that unloads the data from the normalization database to a CSV
     * @param part DLI part
     * @param dir target directory
     * @param snapshotIds specific snapshots IDs that will be integrated
     * @return the SQL script
     */
    public String generateLoad(DLIPart part, String dir, int[] snapshotIds) {
        String file = dir + System.getProperty("file.separator") + part.getFileName();
        String ssn = StringUtil.formatShortName(part.getFileName().split("\\.")[0]);
        List<Column> columns = part.getColumns();
        String cols = "";
        for (Column c : columns) {
            // fact table fact columns
            if(ssn.startsWith("f_") && !c.getName().endsWith("id")) {
                if (cols != null && cols.length() > 0)
                    cols += ",ATOD(" + ssn.toUpperCase() + "." + StringUtil.formatShortName("f_" + c.getName())+")";
                else
                    cols +=  "ATOD(" + ssn.toUpperCase() + "." + StringUtil.formatShortName("f_" + c.getName())+")";
            }
            // lookup table name column
            else if (c.getName().startsWith("nm")) {
                if (cols != null && cols.length() > 0)
                    cols += ",CAST(" + ssn.toUpperCase() + "." + StringUtil.formatShortName(c.getName())+
                            " AS VARCHAR(128))";
                else
                    cols +=  "CAST("+ssn.toUpperCase() + "." + StringUtil.formatShortName(c.getName())+
                            " AS VARCHAR(128))";
            }
            else {
                if (cols != null && cols.length() > 0)
                    cols += "," + ssn.toUpperCase() + "." + StringUtil.formatShortName(c.getName());
                else
                    cols +=  ssn.toUpperCase() + "." + StringUtil.formatShortName(c.getName());
            }
        }
        String whereClause = "";
        if(ssn.startsWith("f_") && snapshotIds != null && snapshotIds.length > 0) {
            String inClause = "";
            for(int i : snapshotIds) {
                if(inClause.length()>0)
                    inClause += ","+i;
                else
                    inClause = "" + i;
            }
            whereClause = ",SNAPSHOTS WHERE " + ssn.toUpperCase() +
                    ".ID BETWEEN SNAPSHOTS.FIRSTID and SNAPSHOTS.LASTID AND SNAPSHOTS.ID IN (" + inClause + ")";
        }
        return "CALL SYSCS_UTIL.SYSCS_EXPORT_QUERY " +
                "('SELECT " + cols + " FROM " + ssn.toUpperCase() + whereClause + "', '" + file
                + "', null, null, null);\n\n";
    }

}
