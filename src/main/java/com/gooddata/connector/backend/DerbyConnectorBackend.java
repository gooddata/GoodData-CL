package com.gooddata.connector.backend;

import com.gooddata.connector.driver.DerbySqlDriver;
import com.gooddata.naming.N;
import org.apache.log4j.Logger;
import org.gooddata.connector.backend.AbstractConnectorBackend;
import org.gooddata.connector.backend.ConnectorBackend;

import java.io.*;
import java.sql.*;

import static org.apache.derby.tools.ij.runScript;

/**
 * GoodData  Derby SQL connector backend. This connector backend is zero-install option. It provides reasonable
 * performance for smaller data files. Please use the MySQL connector backend for large data files.
 * Connector backend handles communication with the specific SQL database. Specifically it handles the DB connection
 * and other communication specifics of the Derby SQL. It uses the SQL driver that generates appropriate SQL dialect.
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
        sg = new DerbySqlDriver();
    }

    /**
     * Create method
     * @throws java.io.IOException in case of an IO issue
     */
    public static DerbyConnectorBackend create() throws IOException {
        return new DerbyConnectorBackend();
    }


    /**
     * {@inheritDoc}
     */
    public Connection connect() throws SQLException {
        String dbName = N.DB_PREFIX+getProjectId()+ N.DB_SUFFIX;
        String protocol = "jdbc:derby:";
        return DriverManager.getConnection(protocol + dbName + ";create=true");
    }

    /**
     * {@inheritDoc}
     */
    public void dropSnapshots() {
        String dbName = N.DB_PREFIX+getProjectId()+N.DB_SUFFIX;
        l.debug("Dropping derby snapshots "+dbName);
        File derbyDir = new File (System.getProperty("derby.system.home") +
                System.getProperty("file.separator") + dbName);
        derbyDir.delete();
        l.debug("Finished dropping derby snapshots "+dbName);
    }

}
