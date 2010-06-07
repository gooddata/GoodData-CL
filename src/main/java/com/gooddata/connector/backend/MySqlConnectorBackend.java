package com.gooddata.connector.backend;

import com.gooddata.transformation.executor.DerbySqlExecutorUpdate;
import com.gooddata.transformation.executor.model.PdmSchema;
import com.gooddata.util.JdbcUtil;
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

    /**
     * The derby SQL executor
     */
    protected DerbySqlExecutorUpdate sg = new DerbySqlExecutorUpdate();


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
     * @param projectId project id
     * @param configFileName config file name
     * @param pdm PDM schema
     * @throws java.io.IOException in case of an IO issue
     */
    protected MySqlConnectorBackend(String projectId, String configFileName, PdmSchema pdm) throws IOException {
        super(projectId, configFileName, pdm);
    }

    /**
     * Create
     * @param projectId project id
     * @param configFileName config file name
     * @param pdm PDM schema
     * @throws java.io.IOException in case of an IO issue
     */
    public static MySqlConnectorBackend create(String projectId, String configFileName, PdmSchema pdm) throws IOException {
        return new MySqlConnectorBackend(projectId, configFileName, pdm);
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
            con = DriverManager.getConnection(protocol + "//localhost/" + projectId +"_" + name);
        }
        catch (SQLException e) {
            con = DriverManager.getConnection(protocol + "//localhost");
            JdbcUtil.executeUpdate(con,
                "CREATE DATABASE IF NOT EXISTS " + projectId +"_" + name + " CHARACTER SET utf8"
            );
            con.close();
            con = DriverManager.getConnection(protocol + "//localhost/" + projectId +"_" + name);            
        }
        return con;
    }


    /**
     * Drops all snapshots
     */
    public void dropSnapshots() {
        //TODO
    }

}