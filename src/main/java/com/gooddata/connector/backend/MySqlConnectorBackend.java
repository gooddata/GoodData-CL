package com.gooddata.connector.backend;

import com.gooddata.connector.driver.MySqlDriver;
import com.gooddata.naming.N;
import com.gooddata.util.JdbcUtil;
import org.apache.log4j.Logger;
import org.gooddata.connector.backend.AbstractConnectorBackend;
import org.gooddata.connector.backend.ConnectorBackend;

import java.io.IOException;
import java.sql.*;

/**
 * GoodData  MySQL connector backend. This connector backend is the performance option. It provides reasonable
 * performance for large data files. This connector backend assumes that MySQL is installed on the computer where
 * it runs.
 * Connector backend handles communication with the specific SQL database. Specifically it handles the DB connection
 * and other communication specifics of the Derby SQL. It uses the SQL driver that generates appropriate SQL dialect.
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */

public class MySqlConnectorBackend extends AbstractConnectorBackend implements ConnectorBackend {

    private static Logger l = Logger.getLogger(MySqlConnectorBackend.class);
    
    /**
     * static initializer of the Derby SQL JDBC driver
     */
    static {
        l.debug("Loading MySQL driver.");
        String driver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driver).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        l.debug("Finished loading MySQL driver.");
    }

    /**
     * Constructor
     * @param username database backend username
     * @param username database backend password
     * @throws java.io.IOException in case of an IO issue
     */
    protected MySqlConnectorBackend(String username, String password) throws IOException {
        super(username, password);
        sg = new MySqlDriver();
    }

    /**
     * Create
     * @param username MySQL username
     * @param password MySQL password 
     * @throws java.io.IOException in case of an IO issue
     */
    public static MySqlConnectorBackend create(String username, String password) throws IOException {
        return new MySqlConnectorBackend(username, password);
    }

    /**
     * {@inheritDoc}
     */
    public Connection connect() throws SQLException {
        String dbName = N.DB_PREFIX+getProjectId()+ N.DB_SUFFIX;
        String protocol = "jdbc:mysql:";
        Connection con = null;
        try {
            con = DriverManager.getConnection(protocol + "//localhost/" + dbName, getUsername(), getPassword());
        }
        catch (SQLException e) {
            con = DriverManager.getConnection(protocol + "//localhost/mysql", getUsername(), getPassword());
            JdbcUtil.executeUpdate(con,
                "CREATE DATABASE IF NOT EXISTS " + dbName + " CHARACTER SET utf8"
            );
            con.close();
            con = DriverManager.getConnection(protocol + "//localhost/" + dbName, getUsername(), getPassword());
        }
        return con;
    }


    /**
     * {@inheritDoc}
     */
    public void dropSnapshots() {
        String dbName = N.DB_PREFIX+getProjectId()+N.DB_SUFFIX;
        l.debug("Dropping MySQL snapshots "+dbName);
        Connection con = null;
        Statement s = null;
        try {
            con = connect();
            s = con.createStatement();
            s.execute("DROP DATABASE IF EXISTS " + dbName);

        } catch (SQLException e) {
            l.error("Can't drop MySQL database.", e);
        }
        finally {
            try  {
                if(s != null && !s.isClosed())
                    s.close();
                if(con != null && !con.isClosed())
                    con.close();
            }
            catch (SQLException e) {
                l.error("Can't close MySQL connection.", e);    
            }
        }
        l.debug("Finished dropping MySQL snapshots "+dbName);
    }

}