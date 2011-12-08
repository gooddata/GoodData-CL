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
package com.gooddata.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Unloads all tables in a database to set of CSV files
 *
 * @author zd@gooddata.com
 * @version: 1.0
 */
public class DatabaseToCsv {

    private static Logger l = Logger.getLogger(DatabaseToCsv.class);

    private static final int FETCH_SIZE = 256;

    private String jdbcDriver;
    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;


    /**
     * Constructor
     *
     * @param driver  JDBC driver
     * @param jdbcCon JDBC connection URL
     * @param usr     database username
     * @param psw     database password
     */
    public DatabaseToCsv(String driver, String jdbcCon, String usr, String psw) {
        setJdbcDriver(driver);
        setJdbcUrl(jdbcCon);
        setJdbcUsername(usr);
        setJdbcPassword(psw);
        l.debug("Loading JDBC driver " + jdbcDriver);
        try {
            Class.forName(jdbcDriver).newInstance();
        } catch (InstantiationException e) {
            l.error("Can't load JDBC driver.", e);
        } catch (IllegalAccessException e) {
            l.error("Can't load JDBC driver.", e);
        } catch (ClassNotFoundException e) {
            l.error("Can't load JDBC driver.", e);
        }
        l.debug("JDBC driver " + jdbcDriver + " loaded.");
    }


    /**
     * Connects to the database
     *
     * @return database connection
     * @throws SQLException
     */
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(getJdbcUrl(), getJdbcUsername(), getJdbcPassword());
    }

    private List<String> listSourceTables() throws SQLException {
        List<String> r = new ArrayList<String>();
        Connection con = null;
        DatabaseMetaData md = null;
        ResultSet rs = null;
        try {
            con = connect();
            md = con.getMetaData();
            rs = md.getTables(null, null, "%", null);
            while (rs.next()) {
                String tableName = rs.getString(3);
                String tableType = rs.getString(4);
                if (tableType.equalsIgnoreCase("table")) {
                    r.add(tableName);
                }
            }
        } finally {
            if (rs != null)
                rs.close();
            if (con != null)
                con.close();
        }
        return r;
    }

    /**
     * Export all DB tables to CSVs
     *
     * @param dir the target directory
     * @throws SQLException
     * @throws IOException
     */
    public void export(String dir) throws SQLException, IOException {
        List<String> tables = listSourceTables();
        for (String table : tables) {
            exportTable(table, dir + System.getProperty("file.separator") + table + ".csv");
        }
    }

    private void exportTable(String tableName, String csvName) throws SQLException, IOException {
        l.info("Exporting table " + tableName + " to " + csvName);
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        CSVWriter cw = FileUtil.createUtf8CsvEscapingWriter(new File(csvName));
        try {
            con = connect();
            st = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
            st.setFetchSize(FETCH_SIZE);
            rs = st.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData md = rs.getMetaData();
            int cnt = md.getColumnCount();
            String[] row = new String[cnt];
            for (int i = 1; i <= cnt; i++) {
                row[i - 1] = md.getColumnName(i);
            }
            cw.writeNext(row);
            while (rs.next()) {
                for (int i = 1; i <= cnt; i++) {
                    row[i - 1] = rs.getString(i);
                    if (row[i - 1] == null || rs.wasNull())
                        row[i - 1] = "";
                }
                cw.writeNext(row);
            }
            cw.flush();
            cw.close();
            l.info("Exported table " + tableName + " to " + csvName);
        } finally {
            if (rs != null)
                rs.close();
            if (con != null)
                con.close();
        }
    }


    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUsername() {
        return jdbcUsername;
    }

    public void setJdbcUsername(String jdbcUsername) {
        this.jdbcUsername = jdbcUsername;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }


}
