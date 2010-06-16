package com.gooddata.connector.backend;

import com.gooddata.connector.driver.DerbySqlDriver;
import org.apache.log4j.Logger;
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

    private static Logger l = Logger.getLogger(DerbyConnectorBackend.class);

    /**
     * static initializer of the Derby SQL JDBC driver
     */
    static {
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
    }


    /**
     * Constructor
     * @throws java.io.IOException in case of an IO issue
     */
    protected DerbyConnectorBackend() throws IOException {
        super(null, null);
        sg = new DerbySqlDriver();
    }

    /**
     * Create
     * @throws java.io.IOException in case of an IO issue
     */
    public static DerbyConnectorBackend create() throws IOException {
        return new DerbyConnectorBackend();
    }


    /**
     * Connects to the Derby database
     * @return JDBC connection
     * @throws SQLException
     */
    public Connection connect() throws SQLException {
        String protocol = "jdbc:derby:";
        return DriverManager.getConnection(protocol + getProjectId() + ";create=true");
    }

    /**
     * Drops all snapshots
     */
    public void dropSnapshots() {
        File derbyDir = new File (System.getProperty("derby.system.home") +
                System.getProperty("file.separator") + getProjectId());
        derbyDir.delete();
    }

}
