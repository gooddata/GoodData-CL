package org.gooddata.connector.driver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.gooddata.connector.model.PdmColumn;
import com.gooddata.connector.model.PdmLookupReplication;
import com.gooddata.connector.model.PdmSchema;
import com.gooddata.connector.model.PdmTable;
import com.gooddata.exception.ModelException;
import com.gooddata.integration.model.Column;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.naming.N;
import com.gooddata.util.JdbcUtil;
import com.gooddata.util.StringUtil;
import com.gooddata.util.JdbcUtil.DummyResultSetHandler;
import com.gooddata.util.JdbcUtil.StatementHandler;

/**
 * GoodData abstract SQL driver. Generates the DDL (tables and indexes), DML (transformation SQL) and other
 * SQL statements necessary for the data normalization (lookup generation)
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public abstract class AbstractSqlDriver implements SqlDriver {

    private static Logger l = Logger.getLogger(AbstractSqlDriver.class);

    // autoincrement syntax
    protected String SYNTAX_AUTOINCREMENT = "";

    // SQL concat function prefix and suffix
    protected String SYNTAX_CONCAT_FUNCTION_PREFIX = "";
    protected String SYNTAX_CONCAT_FUNCTION_SUFFIX = "";
    protected String SYNTAX_CONCAT_OPERATOR = "";

    // separates the different LABELs when we concatenate them to create an unique identifier out of them
    protected String HASH_SEPARATOR = "%";


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
        	if (!exists(c, table.getName())) {
        		createTable(c, table);
        		if (PdmTable.PDM_TABLE_TYPE_LOOKUP.equals(table.getType())) {
        			prepopulateLookupTable(c, table);
        		} else if (PdmTable.PDM_TABLE_TYPE_CONNECTION_POINT.equals(table.getType())) {
        			final List<Map<String,String>> rows = prepareInitialTableLoad(c, table);
        			if (!rows.isEmpty()) {
        				l.warn("Prepopulating of connection point tables is not suppported (table = " + table.getName() + ")");
        			}
        		}
	            if(PdmTable.PDM_TABLE_TYPE_SOURCE.equals(table.getType()))
	                indexAllTableColumns(c, table);
        	} else {
        		for (PdmColumn column : table.getColumns()) {
        			if (!exists(c, table.getName(), column.getName())) {
        				addColumn(c, table, column);
        				if (PdmTable.PDM_TABLE_TYPE_SOURCE.equals(table.getType()))
        					indexTableColumn(c, table, column);
        			}
        		}
        	}
        }
        JdbcUtil.executeUpdate(c,
            "INSERT INTO snapshots(name,firstid,lastid,tmstmp) VALUES ('" + schema.getFactTable().getName() + "',0,0,0)"
        );
    }

    /**
     * Executes the data normalization script
     * @param c JDBC connection
     * @param schema the PDM schema
     * @throws com.gooddata.exception.ModelException if there is a problem with the PDM schema
     * (e.g. multiple source or fact tables)
     * @throws SQLException in case of db problems
     */
    public void executeNormalizeSql(Connection c, PdmSchema schema) throws ModelException, SQLException {

        //populate REFERENCEs lookups from the referenced lookups
        executeLookupReplicationSql(c, schema);

        populateLookupTables(c, schema);
        populateConnectionPointTables(c, schema);
        // nothing for the reference columns

        insertSnapshotsRecord(c, schema);
        insertFactsToFactTable(c, schema);

        for(PdmTable tbl : schema.getLookupTables())
            updateForeignKeyInFactTable(c, tbl, schema);
        for(PdmTable tbl : schema.getReferenceTables())
            updateForeignKeyInFactTable(c, tbl, schema);

        updateSnapshotsRecord(c, schema);
    }

    /**
     * Executes the copying of the referenced lookup tables
     * @param c JDBC connection
     * @param schema the PDM schema
     * @throws com.gooddata.exception.ModelException if there is a problem with the PDM schema (e.g. multiple source or fact tables)
     * @throws java.sql.SQLException in case of db problems
     */
    public void executeLookupReplicationSql(Connection c, PdmSchema schema) throws ModelException, SQLException {
        for (PdmLookupReplication lr : schema.getLookupReplications()) {
            JdbcUtil.executeUpdate(c,
                "DELETE FROM " + lr.getReferencingLookup()
            );
            JdbcUtil.executeUpdate(c,
                "INSERT INTO " + lr.getReferencingLookup() + "("+N.ID+"," + lr.getReferencingColumn() +","+N.HSH+")" +
                " SELECT "+ N.ID+"," + lr.getReferencedColumn() + "," + lr.getReferencedColumn() + " FROM " +
                lr.getReferencedLookup()
            );
        }
    }
    
    /**
     * Returns true if the specified table exists in the DB
     * @param tbl table name
     * @return true if the table exists, false otherwise
     * @throws SQLException 
     */
    public boolean exists(Connection c, String tbl) {
    	String sql = "SELECT * FROM " + tbl + " WHERE 1=0";
    	try {
	    	JdbcUtil.executeQuery(c, sql, new DummyResultSetHandler());
	    	return true;
    	} catch (SQLException e) {
    		return false;
    	}
    }

    /**
     * Returns true if the specified column of the specified table exists in the DB
     * @param tbl table name
     * @param col column name
     * @return true if the table exists, false otherwise
     * @throws java.lang.IllegalArgumentException if the provided table does not exist
     */
    public boolean exists(Connection c, String tbl, String col) {
    	String sql = "SELECT " + col + " FROM " + tbl + " WHERE 1=0";
    	try {
	    	JdbcUtil.executeQuery(c, sql, new DummyResultSetHandler());
	    	return true;
    	} catch (SQLException e) {
    		if (exists(c, tbl))
    			return false;
    		else
    			throw new IllegalArgumentException(e);
    	}
    }

    protected void indexAllTableColumns(Connection c, PdmTable table) throws SQLException {
        for( PdmColumn column : table.getColumns()) {
            indexTableColumn(c, table, column);
        }
    }
    
    private void indexTableColumn(Connection c, PdmTable table, PdmColumn column) throws SQLException {
    	if(!column.isPrimaryKey() && !column.isUnique()) {
            JdbcUtil.executeUpdate(c,"CREATE INDEX idx_" + table.getName()
            		+ "_" + column.getName()
            		+ " ON " + table.getName() + "("+column.getName()+")");
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
    
    private void prepopulateLookupTable(Connection c, PdmTable table) throws SQLException {
    	final List<Map<String,String>> rows = prepareInitialTableLoad(c, table);
    	if (rows.isEmpty())
    		return;
    	
    	// create the list to make sure consistent keys order in the following loop
		final List<String> columns = new ArrayList<String>(rows.get(0).keySet());
    	final String placeholders = StringUtil.join(", ", columns, "?");

    	for (final Map<String,String> row : rows) {
    		
    		final String sql = "INSERT INTO " + table.getName() + " ("
    						 + N.HSH + ", " + StringUtil.join(", ", columns)
    						 + ") VALUES (?, " + placeholders + ")";
    		
    		JdbcUtil.executeUpdate(c, sql, new StatementHandler() {

				public void prepare(PreparedStatement stmt) throws SQLException {
					boolean first = true;
					final StringBuffer hashbf = new StringBuffer();
					int index = 2;
					for (final String col : columns) {
						if (first)
							first = false;
						else
							hashbf.append(HASH_SEPARATOR);
						hashbf.append(row.get(col));
						stmt.setString(index++, row.get(col));
					}
					stmt.setString(1, hashbf.toString());
				}
			});
    	}
    }
    
    private List<Map<String,String>> prepareInitialTableLoad(Connection c, PdmTable table) {
    	final List<Map<String,String>> result = new ArrayList<Map<String,String>>();
    	final List<PdmColumn> toLoad = new ArrayList<PdmColumn>();
    	int max = 0;
    	for (final PdmColumn col : table.getColumns()) {
    		if (col.getElements() != null && !col.getElements().isEmpty()) {
    			int size = col.getElements().size();
    			if (max == 0)
    				max = size;
    			else if (size != max)
    				throw new IllegalStateException(
    						"Column " + col.getName() + " of table " + table.getName()
    						+ " has a different number of elements than: " + toLoad.toString());
    			toLoad.add(col);
    		}
    	}
    	if (!toLoad.isEmpty()) {    	
	    	for (int i = 0; i < toLoad.get(0).getElements().size(); i++) {
	    		final Map<String,String> row = new HashMap<String, String>();
	    		for (final PdmColumn col : toLoad) {
	    			row.put(col.getName(), col.getElements().get(i));
	    		}
	    		result.add(row);
	    	}
    	}
    	return result;
    }
    
    private void addColumn(Connection c, PdmTable table, PdmColumn column) throws SQLException {
    	String sql = "ALTER TABLE " + table.getName() + " ADD COLUMN "
    			   + column.getName() + " " + column.getType();
    	if (column.isUnique())
    		sql += " UNIQUE";
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

    protected void insertSnapshotsRecord(Connection c, PdmSchema schema) throws ModelException, SQLException {
        PdmTable factTable = schema.getFactTable();
        String fact = factTable.getName();
        Date dt = new Date();
        JdbcUtil.executeUpdate(c,
            "INSERT INTO snapshots(name,tmstmp,firstid) SELECT '"+fact+"',"+dt.getTime()+",MAX("+N.ID+")+1 FROM " + fact
        );
        // compensate for the fact that MAX returns NULL when there are no rows in the SELECT
        JdbcUtil.executeUpdate(c,
            "UPDATE snapshots SET firstid = 0 WHERE name = '"+fact+"' AND firstid IS NULL"
        );
    }

    protected void updateSnapshotsRecord(Connection c, PdmSchema schema) throws ModelException, SQLException {
        PdmTable factTable = schema.getFactTable();
        String fact = factTable.getName();
        JdbcUtil.executeUpdate(c,
            "UPDATE snapshots SET lastid = (SELECT MAX("+N.ID+") FROM " + fact + ") WHERE name = '" +
            fact + "' AND lastid IS NULL"
        );
        // compensate for the fact that MAX returns NULL when there are no rows in the SELECT
        JdbcUtil.executeUpdate(c,
            "UPDATE snapshots SET lastid = 0 WHERE name = '" + fact + "' AND lastid IS NULL"
        );
    }

    protected abstract void insertFactsToFactTable(Connection c, PdmSchema schema) throws ModelException, SQLException;

    protected void populateLookupTables(Connection c, PdmSchema schema) throws ModelException, SQLException {
        for(PdmTable lookupTable : schema.getLookupTables()) {
            populateLookupTable(c, lookupTable, schema);
        }
    }

    protected void populateConnectionPointTables(Connection c, PdmSchema schema) throws SQLException, ModelException {
        for(PdmTable cpTable : schema.getConnectionPointTables())
            populateConnectionPointTable(c, cpTable, schema);
    }

    protected void updateForeignKeyInFactTable(Connection c, PdmTable lookupTable, PdmSchema schema)
            throws ModelException, SQLException {
        String lookup = lookupTable.getName();
        String fact = schema.getFactTable().getName();
        String source = schema.getSourceTable().getName();
        String associatedSourceColumns = concatAssociatedSourceColumns(lookupTable);
        JdbcUtil.executeUpdate(c,
              "UPDATE " + fact + " SET  " + lookupTable.getAssociatedSourceColumn() + "_"+N.ID+" = (SELECT "+N.ID+" FROM " +
              lookup + " d," + source + " o WHERE " + associatedSourceColumns + " = d."+N.HSH+" AND o."+N.SRC_ID+"= " +
              fact + "."+N.ID+") WHERE "+N.ID+" > (SELECT MAX(lastid) FROM snapshots WHERE name = '" + fact+"')"
        );
    }

    protected void populateLookupTable(Connection c, PdmTable lookupTable, PdmSchema schema)
            throws ModelException, SQLException {
        String lookup = lookupTable.getName();
        String fact = schema.getFactTable().getName();
        String source = schema.getSourceTable().getName();
        String insertColumns = N.HSH+"," + getInsertColumns(lookupTable);
        String associatedSourceColumns = getAssociatedSourceColumns(lookupTable);
        String concatAssociatedSourceColumns = concatAssociatedSourceColumns(lookupTable);
        String nestedSelectColumns = concatAssociatedSourceColumns+","+associatedSourceColumns;
        JdbcUtil.executeUpdate(c,
            "INSERT INTO " + lookup + "(" + insertColumns +
            ") SELECT DISTINCT " + nestedSelectColumns + " FROM " + source +
            " WHERE "+N.SRC_ID+" > (SELECT MAX(lastid) FROM snapshots WHERE name='" + fact +
            "') AND " + concatAssociatedSourceColumns + " NOT IN (SELECT "+N.HSH+" FROM " +
            lookupTable.getName() + ")"
        );
    }

    protected void populateConnectionPointTable(Connection c, PdmTable lookupTable, PdmSchema schema)
            throws ModelException, SQLException {
        String lookup = lookupTable.getName();
        String fact = schema.getFactTable().getName();
        String source = schema.getSourceTable().getName();
        String insertColumns = N.ID+","+N.HSH+"," + getInsertColumns(lookupTable);
        String associatedSourceColumns = getAssociatedSourceColumns(lookupTable);
        String concatAssociatedSourceColumns = concatAssociatedSourceColumns(lookupTable);
        String nestedSelectColumns = N.SRC_ID+","+concatAssociatedSourceColumns+","+associatedSourceColumns;
        /*
        JdbcUtil.executeUpdate(c,
            "INSERT INTO " + lookup + "(" + insertColumns + ") SELECT DISTINCT " + nestedSelectColumns +
            " FROM " + source + " WHERE "+N.SRC_ID+" > (SELECT MAX(lastid) FROM snapshots WHERE name='" + fact +
            "') AND " + associatedSourceColumns + " NOT IN (SELECT "+N.HSH+" FROM " + lookup + ")"
        );
        */
        // TODO: when snapshotting, there are duplicate CONNECTION POINT VALUES
        // we need to decide if we want to accumultae the connection point lookup or not
        /*
        JdbcUtil.executeUpdate(c,
            "INSERT INTO " + lookup + "(" + insertColumns + ") SELECT DISTINCT " + nestedSelectColumns +
            " FROM " + source + " WHERE "+N.SRC_ID+" > (SELECT MAX(lastid) FROM snapshots WHERE name='" + fact +"')"
        );
        */
        String sql = "INSERT INTO " + lookup + "(" + insertColumns +
	        ") SELECT DISTINCT " + nestedSelectColumns + " FROM " + source +
	        " WHERE "+N.SRC_ID+" > (SELECT MAX(lastid) FROM snapshots WHERE name='" + fact +
	        "') AND " + concatAssociatedSourceColumns + " NOT IN (SELECT "+N.HSH+" FROM " +
	        lookupTable.getName() + ")";
        JdbcUtil.executeUpdate(c, sql);
    }

    protected String concatAssociatedSourceColumns(PdmTable lookupTable) {
        String associatedColumns = "";
        for(PdmColumn column : lookupTable.getAssociatedColumns()) {
            // if there are LABELS, the lookup can't be added twice to the FROM clause
            if(associatedColumns.length() > 0)
                associatedColumns += SYNTAX_CONCAT_OPERATOR +  column.getSourceColumn();
            else
                associatedColumns = column.getSourceColumn();
        }
        associatedColumns = SYNTAX_CONCAT_FUNCTION_PREFIX + associatedColumns + SYNTAX_CONCAT_FUNCTION_SUFFIX;
        return associatedColumns;
    }

    protected String getInsertColumns(PdmTable lookupTable) {
        String insertColumns = "";
        for(PdmColumn column : lookupTable.getAssociatedColumns()) {
            if(insertColumns.length() > 0)
                insertColumns += "," + column.getName();
            else
                insertColumns += column.getName();
        }
        return insertColumns;
    }

    protected String getAssociatedSourceColumns(PdmTable lookupTable) {
        String sourceColumns = "";
        for(PdmColumn column : lookupTable.getAssociatedColumns()) {
            if(sourceColumns.length() > 0)
                sourceColumns += "," + column.getSourceColumn();
            else
                sourceColumns += column.getSourceColumn();
        }
        return sourceColumns;
    }

    protected String getNonAutoincrementColumns(PdmTable tbl) {
        String cols = "";
        for (PdmColumn col : tbl.getColumns()) {
            String cn = col.getName();
            if(!col.isAutoIncrement())
                if (cols != null && cols.length() > 0)
                    cols += "," + cn;
                else
                    cols += cn;
        }
        return cols;
    }

    protected String getLoadWhereClause(DLIPart part, PdmSchema schema, int[] snapshotIds) throws ModelException {
        String dliTable = getTableNameFromPart(part);
        PdmTable pdmTable = schema.getTableByName(dliTable);
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
        return whereClause;
    }

    protected String getLoadColumns(DLIPart part, PdmSchema schema) throws ModelException {
        String dliTable = getTableNameFromPart(part);
        PdmTable pdmTable = schema.getTableByName(dliTable);
        List<Column> columns = part.getColumns();
        String cols = "";
        for (Column cl : columns) {
            PdmColumn col = pdmTable.getColumnByName(cl.getName());
            // fact table fact columns
            if(PdmTable.PDM_TABLE_TYPE_FACT.equals(pdmTable.getType()) &&
                    SourceColumn.LDM_TYPE_FACT.equals(col.getLdmTypeReference()))
                cols = decorateFactColumnForLoad(cols, cl, dliTable);
            // lookup table name column
            else if (PdmTable.PDM_TABLE_TYPE_LOOKUP.equals(pdmTable.getType()) &&
                    SourceColumn.LDM_TYPE_ATTRIBUTE.equals(col.getLdmTypeReference()))
                cols = decorateLookupColumnForLoad(cols, cl, dliTable);
            else
                cols = decorateOtherColumnForLoad(cols, cl, dliTable);
        }
        return cols;
    }

    protected String decorateFactColumnForLoad(String cols, Column cl, String table) {
        return decorateOtherColumnForLoad(cols, cl, table);
    }

    protected String decorateLookupColumnForLoad(String cols, Column cl, String table) {
        return decorateOtherColumnForLoad(cols, cl, table);
    }
    
    protected String decorateOtherColumnForLoad(String cols, Column cl, String table) {
        if (cols != null && cols.length() > 0)
            cols += "," + table.toUpperCase() + "." + StringUtil.formatShortName(cl.getName());
        else
            cols +=  table.toUpperCase() + "." + StringUtil.formatShortName(cl.getName());
        return cols;
    }

    protected String getTableNameFromPart(DLIPart part) {
        return StringUtil.formatShortName(part.getFileName().split("\\.")[0]);
    }

}