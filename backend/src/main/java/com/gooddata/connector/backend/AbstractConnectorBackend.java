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

import com.gooddata.connector.driver.SqlDriver;
import com.gooddata.connector.model.PdmSchema;
import com.gooddata.exception.GdcIntegrationErrorException;
import com.gooddata.exception.InternalErrorException;
import com.gooddata.integration.model.Column;
import com.gooddata.integration.model.DLI;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.integration.rest.GdcRESTApiWrapper;
import com.gooddata.util.FileUtil;
import com.gooddata.util.JdbcUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

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

    // SQL driver for executing DBMS specific SQL
    protected SqlDriver sg;

    // PDM schema
    private PdmSchema pdm;

    // Project id
    private String projectId;


    // MySQL username
    private String username;

    // MySQL password
    private String password;
    

    /**
     * Constructor
     * @param username database backend username
     * @param password database backend password 
     * @throws IOException in case of an IO issue 
     */
    protected AbstractConnectorBackend(String username, String password) throws IOException {
        setUsername(username);
        setPassword(password);
    }

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
        	con = connect();
            if(!isInitialized()) {
                l.debug("Initializing system schema.");
                sg.executeSystemDdlSql(con);
                l.debug("System schema initialized.");
            }
            sg.executeDdlSql(con, getPdm());
            l.debug("Schema initialized.");
        }
        catch (SQLException e) {
            throw new InternalErrorException("Error initializing pdm schema '" + getPdm().getName() + "'", e);
        }
        finally {
            try {
                if (con != null && !con.isClosed())
                    con.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void transform() {
        Connection con = null;
        try {
            con = connect();
            sg.executeNormalizeSql(con, getPdm());
        }
        catch (SQLException e) {
            throw new InternalErrorException("Error normalizing PDM Schema " + getPdm().getName() + " " + getPdm().getTables(), e);
        }
        finally {
            try {
                if (con != null && !con.isClosed())
                    con.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
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
            con = connect();
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
                if(con != null && !con.isClosed())
                    con.close();
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
    public int getLastSnapshotId() {
        Connection con = null;
        Statement s = null;
        ResultSet r = null;
        try {
            con = connect();
            s = con.createStatement();
            r = s.executeQuery("SELECT MAX(id) FROM snapshots");
            for(boolean rc = r.next(); rc; rc = r.next()) {
                int id = r.getInt(1);
                l.debug("Last snapshot is "+id);
                return id;
            }
        }
        catch (SQLException e) {
            throw new InternalErrorException(e.getMessage());
        }
        finally {
            try {
                if(r != null)
                    r.close();
                if(s != null)
                    s.close();
                if(con != null && !con.isClosed())
                    con.close();
            }
            catch (SQLException ee) {
                ee.printStackTrace();
            }
        }
        throw new InternalErrorException("Can't retrieve the last snapshot number.");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInitialized() {
        return exists("snapshots");
    }

    /**
     * {@inheritDoc}
     */
    public boolean exists(String tbl) {
        Connection con = null;
        try {
            con = connect();
            return sg.exists(con, tbl);
        }
        catch (SQLException e) {
        	throw new InternalErrorException(e);
		}
        finally {
            try {
                if(con != null && !con.isClosed())
                    con.close();
            }
            catch (SQLException ee) {
                ee.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void extract(File dataFile) {
        Connection con = null;
        try {
            con = connect();
            sg.executeExtractSql(con, getPdm(), dataFile.getAbsolutePath());
        }
        catch (SQLException e) {
            throw new InternalErrorException(e);
        }
        finally {
            try {
                if (con != null && !con.isClosed())
                    con.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

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
            con = connect();
            // generate SELECT INTO CSV Derby SQL
            // the required data structures are taken for each DLI part
            for (DLIPart p : parts) {
                sg.executeLoadSql(con, getPdm(), p, dir, snapshotIds);
            }
        }
        catch (SQLException e) {
            throw new InternalErrorException(e);
        }
        finally {
            try {
                if (con != null && !con.isClosed())
                    con.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getUsername() {
        return username;
    }

    /**
     * {@inheritDoc}
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * {@inheritDoc}
     */
    public String getPassword() {
        return password;
    }

    /**
     * {@inheritDoc}
     */
    public void setPassword(String password) {
        this.password = password;
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
}
