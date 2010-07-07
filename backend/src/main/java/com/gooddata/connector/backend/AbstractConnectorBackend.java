/*
 * Copyright (c) 2009, GoodData Corporation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice, this list of conditions and
 *        the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *        and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *     * Neither the name of the GoodData Corporation nor the names of its contributors may be used to endorse
 *        or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.gooddata.connector.backend;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.gooddata.connector.model.PdmColumn;
import com.gooddata.connector.model.PdmSchema;
import com.gooddata.connector.model.PdmTable;
import com.gooddata.exception.ConnectorBackendException;
import com.gooddata.exception.InternalErrorException;
import com.gooddata.integration.model.Column;
import com.gooddata.integration.model.DLI;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.integration.rest.GdcRESTApiWrapper;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.util.FileUtil;
import com.gooddata.util.JdbcUtil;
import com.gooddata.util.StringUtil;

/**
 * GoodData abstract connector backend. This connector backend provides the base implementation that the specific
 * connector backends reuse.
 * Connector backend handles communication with the specific SQL database. Specifically it handles the DB connection
 * and other communication specifics of the DBMS. It uses the SQL driver that generates appropriate SQL dialect.
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */public abstract class AbstractConnectorBackend implements ConnectorBackend {

    private static Logger l = Logger.getLogger(AbstractConnectorBackend.class);

    // PDM schema
    private PdmSchema pdm;

    // Project id
    private String projectId;

    // separates the different LABELs when we concatenate them to create an unique identifier out of them
    protected String HASH_SEPARATOR = "%";

    /**
     * {@inheritDoc}
     */
    public abstract void dropSnapshots();
        

    /**
     * {@inheritDoc}
     */
    public void deploy(DLI dli, List<DLIPart> parts, String dir, String archiveName)
            throws IOException {
        deploySnapshot(dli, parts, dir, archiveName, null);
    }

    /**
     * Adds CSV headers to all CSV files
     * @param parts the Data Loading Interface parts
     * @param dir target directory where the data package will be stored
     * @throws IOException IO issues
     */
    protected void addHeaders(List<DLIPart> parts, String dir) throws IOException {
        for(DLIPart part : parts) {
            String fn = part.getFileName();
            List<Column> cols = part.getColumns();
            String header = "";
            for(Column col : cols) {
                if(header != null && header.length() > 0) {
                    header += ","+col.getName();
                }
                else {
                    header += col.getName();                    
                }
            }
            File original = new File(dir + System.getProperty("file.separator") + fn);
            File tmpFile = FileUtil.appendCsvHeader(header, original);
            original.delete();
            tmpFile.renameTo(original);
        }
    }

    
    /**
     * {@inheritDoc}
     */
    public void deploySnapshot(DLI dli, List<DLIPart> parts, String dir, String archiveName, int[] snapshotIds)
            throws IOException {
        l.debug("Deploying snapshots ids "+snapshotIds);
        loadSnapshot(parts, dir, snapshotIds);
        String fn = dir + System.getProperty("file.separator") +
                GdcRESTApiWrapper.DLI_MANIFEST_FILENAME;
        String cn = dli.getDLIManifest(parts);
        FileUtil.writeStringToFile(cn, fn);
        l.debug("Manifest file written to file '"+fn+"'. Content: "+cn);
        addHeaders(parts, dir);
        FileUtil.compressDir(dir, archiveName);
        l.debug("Snapshots ids "+snapshotIds+" deployed.");
    }

    /**
     * {@inheritDoc}
     */
    public PdmSchema getPdm() {
        return pdm;
    }

    /**
     * {@inheritDoc}
     */
    public void setPdm(PdmSchema pdm) {
        this.pdm = pdm;
    }

    /**
     * {@inheritDoc}
     */
    public void initialize() {
        Connection con = null;
        try {
            l.debug("Initializing schema.");
        	con = getConnection();
            if(!isInitialized()) {
                l.debug("Initializing system schema.");
                initializeLocalProject();
                l.debug("System schema initialized.");
            }
            initializeLocalDataSet(getPdm());
            l.debug("Schema initialized.");
        }
        catch (SQLException e) {
            throw new InternalErrorException("Error initializing pdm schema '" + getPdm().getName() + "'", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void transform() {
        Connection con = null;
        try {
            con = getConnection();
            createSnowflake(getPdm());
        }
        catch (SQLException e) {
            throw new InternalErrorException("Error normalizing PDM Schema " + getPdm().getName() + " " + getPdm().getTables(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String listSnapshots() {
        String result = "ID        FROM ROWID        TO ROWID        TIME\n";
              result += "------------------------------------------------\n";
        Connection con = null;
        Statement s = null;
        ResultSet r = null;
        try {
            con = getConnection();
            s = con.createStatement();
            r = JdbcUtil.executeQuery(s, "SELECT id,firstid,lastid,tmstmp FROM snapshots");
            for(boolean rc = r.next(); rc; rc = r.next()) {
                int id = r.getInt(1);
                int firstid = r.getInt(2);
                int lastid = r.getInt(3);
                long tmstmp = r.getLong(4);
                Date tm = new Date(tmstmp);
                result += id + "        " + firstid + "        " + lastid + "        " + tm + "\n";
            }
        }
        catch (SQLException e) {
            throw new InternalErrorException(e.getMessage());
        }
        finally {
            try {
                if(r != null)
                    r.close();
                if (s != null)
                    s.close();
            }
            catch (SQLException ee) {
               ee.printStackTrace();
            }
        }
        l.debug("Current snapshots: \n"+result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public abstract int getLastSnapshotId();

    /**
     * {@inheritDoc}
     */
    public boolean isInitialized() {
        return exists("snapshots");
    }

    /**
     * {@inheritDoc}
     */
    protected abstract boolean exists(String tbl);
    
    /**
     * {@inheritDoc}
     */
    public void extract(File dataFile) {
        l.debug("Extracting CSV file="+dataFile.getAbsolutePath());
        if(!dataFile.exists()) {
            l.error("The file "+dataFile.getAbsolutePath()+" doesn't exists!");
            throw new InternalErrorException("The file "+dataFile.getAbsolutePath()+" doesn't exists!");
        }
        try {
            l.debug("The file "+dataFile.getAbsolutePath()+" does exists size="+dataFile.length());
            Connection con = getConnection();
            executeExtractSql(con, getPdm(), dataFile.getAbsolutePath());
        }
        catch (SQLException e) {
            throw new InternalErrorException(e);
        }
        l.debug("Extracted CSV file="+dataFile.getAbsolutePath());
    }
    
    protected abstract Connection getConnection() throws SQLException;

	protected abstract void executeLoadSql(Connection con, PdmSchema pdm, DLIPart p, String dir, int[] snapshotIds) throws SQLException;

    protected abstract void executeExtractSql(Connection con, PdmSchema pdm, String absolutePath) throws SQLException;

	/**
     * {@inheritDoc}
     */
    public void load(List<DLIPart> parts, String dir) {
        loadSnapshot(parts, dir, null);
    }

    /**
     * {@inheritDoc}
     */
    public void loadSnapshot(List<DLIPart> parts, String dir, int[] snapshotIds) {
        Connection con = null;
        try {
            con = getConnection();
            // generate SELECT INTO CSV Derby SQL
            // the required data structures are taken for each DLI part
            for (DLIPart p : parts) {
                executeLoadSql(con, getPdm(), p, dir, snapshotIds);
            }
        }
        catch (SQLException e) {
            throw new InternalErrorException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @throws SQLException 
     */
    protected boolean exists(Connection c, String tbl) throws SQLException {
    	DatabaseMetaData md = c.getMetaData();
    	ResultSet rs = md.getTables(null, null, tbl, null);
    	try {
	    	return rs.next();
    	} finally {
    		if (rs != null)
    			rs.close();
    	}
    }

    /**
     * Returns true if the specified column of the specified table exists in the DB. Case sensitive!
     * @param tbl table name
     * @param col column name
     * @return true if the table exists, false otherwise
     * @throws IllegalArgumentException if the required table does not exist
     * @throws SQLException if other database related problem occures 
     */
    protected boolean exists(Connection c, String tbl, String col) throws SQLException {
    	if (!exists(c, tbl))
    		throw new IllegalArgumentException("Table '" + tbl + "' does not exist.");
    	String sql = "SELECT * FROM " + tbl + " WHERE 1=0";
		Statement st = c.createStatement();
		try {
            ResultSet rs = st.executeQuery(sql);
            try {
	            ResultSetMetaData md = rs.getMetaData();
	            int cols = md.getColumnCount();
	            for (int i = 1; i <= cols; i++) {
	            	if (col.equals(md.getColumnName(i)))
	            		return true;
	            }
	            return false;
    		} finally {
    			if (rs != null)
    				rs.close();
    		}
		} finally {
			if (st != null)
				st.close();
		}
    }


    /**
     * Indexes all table columns
     * @param c JDBC connection
     * @param table target table
     * @throws SQLException in case of SQL issues
     */
    protected void indexAllTableColumns(Connection c, PdmTable table) throws SQLException {
        for( PdmColumn column : table.getColumns()) {
            indexTableColumn(c, table, column);
        }
    }

    /**
     * Indexes table's column
     * @param c JDBC connection
     * @param table target table
     * @param column target table's columns
     * @throws SQLException in case of SQL issues
     */
    private void indexTableColumn(Connection c, PdmTable table, PdmColumn column) throws SQLException {
    	if(!column.isPrimaryKey() && !column.isUnique()) {
            JdbcUtil.executeUpdate(c,"CREATE INDEX idx_" + table.getName()
            		+ "_" + column.getName()
            		+ " ON " + table.getName() + "("+column.getName()+")");
    	}
    }

    /**
     * TODO: PK to document
     * @param table target table
     * @return
     */
    protected final List<Map<String,String>> prepareInitialTableLoad(PdmTable table) {
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

    /**
     * Inserts rows from the source table to the fact table
     * @param c JDBC connection
     * @param schema PDM schema
     * @throws SQLException in case of a DB issue
     */
    protected abstract void insertFactsToFactTable(Connection c, PdmSchema schema) throws SQLException;

    /**
     * Get all columns that will be inserted (exclude autoincrements)
     * @param lookupTable lookup table
     * @return all columns eglibile for insert
     */
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

    /**
     * Returns associted columns in the source table
     * @param lookupTable lookup table
     * @return list of associated source columns
     */
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

    /**
     * Returns non-autoincrement columns
     * @param tbl table
     * @return non-autoincrement columns
     */
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

    /**
     * Generates the where clause for unloading data to CSVs in the data loading package
     * @param part DLI part
     * @param schema PDM schema
     * @param snapshotIds ids of snapshots to unload
     * @return SQL where clause
     */
    protected String getLoadWhereClause(DLIPart part, PdmSchema schema, int[] snapshotIds) {
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
            whereClause = ",snapshots WHERE " + dliTable +
                    ".ID BETWEEN snapshots.firstid and snapshots.lastid AND snapshots.id IN (" + inClause + ")";
        }
        return whereClause;
    }

    /**
     * Generates the list of columns for unloading data to CSVs in the data loading package
     * @param part DLI part
     * @param schema PDM schema
     * @return list of columns
     */
    protected String getLoadColumns(DLIPart part, PdmSchema schema)  {
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

    /**
     * Uses DBMS specific functions for decorating fact columns for unloading from DB to CSV
     * @param cols column list
     * @param cl column to add to cols
     * @param table table name
     * @return the amended list
     */
    protected String decorateFactColumnForLoad(String cols, Column cl, String table) {
        return decorateOtherColumnForLoad(cols, cl, table);
    }

    /**
     * Uses DBMS specific functions for decorating lookup columns for unloading from DB to CSV
     * @param cols column list
     * @param cl column to add to cols
     * @param table table name
     * @return the amended list
     */
    protected String decorateLookupColumnForLoad(String cols, Column cl, String table) {
        return decorateOtherColumnForLoad(cols, cl, table);
    }

    /**
     * Uses DBMS specific functions for decorating generic columns for unloading from DB to CSV
     * @param cols column list
     * @param cl column to add to cols
     * @param table table name
     * @return the amended list
     */
    protected String decorateOtherColumnForLoad(String cols, Column cl, String table) {
        if (cols != null && cols.length() > 0)
            cols += "," + table + "." + StringUtil.formatShortName(cl.getName());
        else
            cols +=  table + "." + StringUtil.formatShortName(cl.getName());
        return cols;
    }

    /**
     * Get tab,e name from DLI part
     * @param part DLI part
     * @return table name
     */
    protected String getTableNameFromPart(DLIPart part) {
        return StringUtil.formatShortName(part.getFileName().split("\\.")[0]);
    }


    /**
     * {@inheritDoc}
     */
    public String getProjectId() {
        return projectId;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    
	/**
     * {@inheritDoc}
     */
    protected abstract void initializeLocalProject() throws ConnectorBackendException;

    /**
     * {@inheritDoc}
     */
    protected abstract void initializeLocalDataSet(PdmSchema schema) throws ConnectorBackendException;

    /**
     * {@inheritDoc}
     */
    protected abstract void createSnowflake(PdmSchema schema) throws ConnectorBackendException;

}
