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
import java.sql.*;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.gooddata.connector.model.PdmColumn;
import com.gooddata.connector.model.PdmSchema;
import com.gooddata.connector.model.PdmTable;
import com.gooddata.exception.ConnectorBackendException;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.naming.N;
import com.gooddata.util.JdbcUtil;
import com.gooddata.util.StringUtil;

/**
 * GoodData  Derby SQL connector backend. This connector backend is zero-install option. It provides reasonable
 * performance for smaller data files. Please use the MySQL connector backend for large data files.
 * Connector backend handles communication with the specific SQL database. Specifically it handles the DB connection
 * and other communication specifics of the Derby SQL. It uses the SQL driver that generates appropriate SQL dialect.
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class DerbyConnectorBackend extends AbstractSqlConnectorBackend implements ConnectorBackend {

    private static Logger l = Logger.getLogger(DerbyConnectorBackend.class);

    /**
     * static initializer of the Derby SQL JDBC driver
     */
    static {
        l.debug("Loading Derby SQL driver.");
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        try {
            Class.forName(driver).newInstance();
        } catch (InstantiationException e) {
            l.error("Error loading Derby SQL JDBC driver.", e);
        } catch (IllegalAccessException e) {
            l.error("Error loading Derby SQL JDBC driver.", e);
        } catch (ClassNotFoundException e) {
            l.error("Error loading Derby SQL JDBC driver.", e);
        }
        l.debug("Derby SQL driver loaded.");
    }

    /**
     * Constructor
     * @throws java.io.IOException in case of an IO issue
     */
    protected DerbyConnectorBackend() throws IOException {
        super(null, null);

        // autoincrement syntax
        SYNTAX_AUTOINCREMENT = "GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1)";
        SYNTAX_CONCAT_FUNCTION_PREFIX = "";
        SYNTAX_CONCAT_FUNCTION_SUFFIX = "";
        SYNTAX_CONCAT_OPERATOR = " || '" + HASH_SEPARATOR + "' || ";
    }

    /**
     * Create method
     * @return a new instance of the connector backend 
     * @throws java.io.IOException in case of an IO issue
     */
    public static DerbyConnectorBackend create() throws IOException {
        return new DerbyConnectorBackend();
    }


    /**
     * {@inheritDoc}
     */
    public Connection getConnection() throws SQLException {
        String dbName = N.DB_PREFIX+getProjectId()+N.DB_SUFFIX;
    	if (connection == null) {
	        String protocol = "jdbc:derby:";
	        connection = DriverManager.getConnection(protocol + dbName + ";create=true");
    	}
    	return connection;
    }

    /**
     * {@inheritDoc}
     */
    public void dropSnapshots() {
        String dbName = N.DB_PREFIX+getProjectId()+N.DB_SUFFIX;
        l.debug("Dropping derby snapshots "+dbName);
        File derbyDir = new File (System.getProperty("derby.system.home") +
                System.getProperty("file.separator") + dbName);
        try {
        	FileUtils.deleteDirectory(derbyDir);
        	l.debug("Finished dropping derby snapshots "+dbName);
        } catch (IOException e) {
        	throw new ConnectorBackendException("Cannot delete derby snapshots from " + derbyDir.getAbsolutePath() +
                    ": " + e.getMessage(), e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    protected void createFunctions(Connection c) throws SQLException {
        l.debug("Creating system functions.");
        JdbcUtil.executeUpdate(c,
            "CREATE FUNCTION ATOD(str VARCHAR(255)) RETURNS VARCHAR(255)\n" +
            " PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA" +
            " EXTERNAL NAME 'com.gooddata.derby.extension.DerbyExtensions.atod'"
        );

        JdbcUtil.executeUpdate(c,
            "CREATE FUNCTION DTTOI(str VARCHAR(255), fmt VARCHAR(30)) RETURNS INT\n" +
            " PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA" +
            " EXTERNAL NAME 'com.gooddata.derby.extension.DerbyExtensions.dttoi'"
        );
        l.debug("System functions creation finished.");
    }

    /**
     * {@inheritDoc}
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
            whereClause = ",snapshots WHERE " + dliTable.toUpperCase() +
                    ".ID BETWEEN snapshots.firstid and snapshots.lastid AND snapshots.id IN (" + inClause + ")";
        }
        return whereClause;
    }

    /**
     * {@inheritDoc}
     */
    protected String decorateFactColumnForLoad(String cols, PdmColumn cl, String table) {
        if (cols.length() > 0)
            cols += ",ATOD(" + table.toUpperCase() + "." +
                    StringUtil.toIdentifier(cl.getName())+")";
        else
            cols +=  "ATOD(" + table.toUpperCase() + "." +
                    StringUtil.toIdentifier(cl.getName())+")";
        return cols;
    }

    /**
     * {@inheritDoc}
     */
    protected String decorateLookupColumnForLoad(String cols, PdmColumn cl, String table) {
        if (cols != null && cols.length() > 0)
            cols += ",CAST(" + table.toUpperCase() + "." + StringUtil.toIdentifier(cl.getName())+
                    " AS VARCHAR("+Constants.LABEL_MAX_LENGTH+"))";
        else
            cols +=  "CAST("+table.toUpperCase() + "." + StringUtil.toIdentifier(cl.getName())+
                    " AS VARCHAR("+Constants.LABEL_MAX_LENGTH+"))";
        return cols;
    }

    /**
     * {@inheritDoc}
     */
    protected String decorateOtherColumnForLoad(String cols, PdmColumn cl, String table) {
        if (cols != null && cols.length() > 0)
            cols += "," + table.toUpperCase() + "." + StringUtil.toIdentifier(cl.getName());
        else
            cols +=  table.toUpperCase() + "." + StringUtil.toIdentifier(cl.getName());
        return cols;
    }

    /**
     * {@inheritDoc}
     */
    protected void insertFactsToFactTable(Connection c, PdmSchema schema) throws SQLException {
        PdmTable factTable = schema.getFactTable();
        PdmTable sourceTable = schema.getSourceTable();
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
            sourceColumns += ",DTTOI(" + column.getSourceColumn() + ",'"+column.getFormat()+"')";
        }
        JdbcUtil.executeUpdate(c,
            "INSERT INTO "+fact+"("+N.ID+factColumns+") SELECT "+N.SRC_ID + sourceColumns +
            " FROM " + source + " WHERE "+N.SRC_ID+" > (SELECT MAX(lastid) FROM snapshots WHERE name='"+fact+"')"
        );
    }

    /**
     * {@inheritDoc}
     * Derby SQL catalog is all in uppercase! We need to convert all names to uppercase :(
     * @throws SQLException
     */
    public boolean exists(Connection c, String tbl) throws SQLException {
    	return super.exists(c,tbl.toUpperCase());
    }

    /**
     * {@inheritDoc}
     * Derby SQL catalog is all in uppercase! We need to convert all names to uppercase :(
     * @throws SQLException
     */    
    public boolean exists(Connection c, String tbl, String col) throws SQLException {
    	return super.exists(c,tbl.toUpperCase(), col.toUpperCase());
    }

    

}
