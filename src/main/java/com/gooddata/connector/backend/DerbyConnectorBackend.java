package com.gooddata.connector.backend;

import com.gooddata.connector.executor.DerbySqlExecutor;
import com.gooddata.connector.model.PdmSchema;
import org.gooddata.connector.backend.AbstractConnectorBackend;
import org.gooddata.connector.backend.ConnectorBackend;

import java.io.*;
import java.sql.*;

import static org.apache.derby.tools.ij.runScript;

/**
 * GoodData  abstract derby connector
 * This connector creates a GoodData LDM schema from a source schema, extracts the data from the source,
 * normalizes the data, and create the GoodData data deployment package. This connector uses the embedded Derby SQL
 * database.
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class DerbyConnectorBackend extends AbstractConnectorBackend implements ConnectorBackend {

    /**
     * static initializer of the Derby SQL JDBC driver
     */
    static {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
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
    protected DerbyConnectorBackend(String projectId, String configFileName, PdmSchema pdm) throws IOException {
        super(projectId, configFileName, pdm, null, null);
        sg = new DerbySqlExecutor();
    }

    /**
     * Create 
     * @param projectId project id
     * @param configFileName config file name
     * @param pdm PDM schema
     * @throws java.io.IOException in case of an IO issue
     */
    public static DerbyConnectorBackend create(String projectId, String configFileName, PdmSchema pdm) throws IOException {
        return new DerbyConnectorBackend(projectId, configFileName, pdm);
    }


    /**
     * Connects to the Derby database
     * @return JDBC connection
     * @throws SQLException
     */
    public Connection connect() throws SQLException {
        String protocol = "jdbc:derby:";
        return DriverManager.getConnection(protocol + projectId + ";create=true");
    }

    /**
     * Drops all snapshots
     */
    public void dropSnapshots() {
        File derbyDir = new File (System.getProperty("derby.system.home") +
                System.getProperty("file.separator") + projectId);
        derbyDir.delete();
    }

}
