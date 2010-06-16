package com.gooddata.connector.backend;

import com.gooddata.connector.driver.MySqlDriver;
import com.gooddata.util.JdbcUtil;
import org.apache.log4j.Logger;
import org.gooddata.connector.backend.AbstractConnectorBackend;
import org.gooddata.connector.backend.ConnectorBackend;

import java.io.IOException;
import java.sql.*;

/**
 * GoodData  abstract derby connector
 * This connector creates a GoodData LDM schema from a source schema, extracts the data from the source,
 * normalizes the data, and create the GoodData data deployment package. This connector uses the embedded MySQL
 * database.
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
     * Connects to the Derby database
     * @return JDBC connection
     * @throws java.sql.SQLException
     */
    public Connection connect() throws SQLException {
        String protocol = "jdbc:mysql:";
        Connection con = null;
        try {
            con = DriverManager.getConnection(protocol + "//localhost/" + getProjectId(), getUsername(), getPassword());
        }
        catch (SQLException e) {
            con = DriverManager.getConnection(protocol + "//localhost/mysql", getUsername(), getPassword());
            JdbcUtil.executeUpdate(con,
                "CREATE DATABASE IF NOT EXISTS " + getProjectId() + " CHARACTER SET utf8"
            );
            con.close();
            con = DriverManager.getConnection(protocol + "//localhost/" + getProjectId(), getUsername(), getPassword());
        }
        return con;
    }


    /**
     * Drops all snapshots
     */
    public void dropSnapshots() {
        Connection con = null;
        Statement s = null;
        try {
            con = connect();
            s = con.createStatement();
            s.execute("DROP DATABASE IF EXISTS " + getProjectId());

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
    }

}