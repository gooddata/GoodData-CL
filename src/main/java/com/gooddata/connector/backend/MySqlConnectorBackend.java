package com.gooddata.connector.backend;

import com.gooddata.transformation.executor.MySqlExecutor;
import com.gooddata.transformation.executor.model.PdmSchema;
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

    // MySQL username
    private String username;

    // MySQL password
    private String password;


    /**
     * Constructor
     * @param projectId project id
     * @param configFileName config file name
     * @param pdm PDM schema
     * @param username MySQL username
     * @param password MySQL password
     * @throws java.io.IOException in case of an IO issue
     */
    protected MySqlConnectorBackend(String projectId, String configFileName, PdmSchema pdm, String username,
                                    String password) throws IOException {
        super(projectId, configFileName, pdm);
        this.username = username;
        this.password = password;        
        sg = new MySqlExecutor();
    }

    /**
     * Create
     * @param projectId project id
     * @param configFileName config file name
     * @param pdm PDM schema
     * @param username MySQL username
     * @param password MySQL password 
     * @throws java.io.IOException in case of an IO issue
     */
    public static MySqlConnectorBackend create(String projectId, String configFileName, PdmSchema pdm, String username,
                                    String password) throws IOException {
        return new MySqlConnectorBackend(projectId, configFileName, pdm, username, password);
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
            con = DriverManager.getConnection(protocol + "//localhost/" + projectId, username, password);
        }
        catch (SQLException e) {
            con = DriverManager.getConnection(protocol + "//localhost/mysql", username, password);
            JdbcUtil.executeUpdate(con,
                "CREATE DATABASE IF NOT EXISTS " + projectId  + " CHARACTER SET utf8"
            );
            con.close();
            con = DriverManager.getConnection(protocol + "//localhost/" + projectId, username, password);            
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
            s.execute("DROP DATABASE IF EXISTS " + projectId);

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