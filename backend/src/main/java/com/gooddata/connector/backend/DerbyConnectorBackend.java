/*
 * Copyright (c) 2009 GoodData Corporation.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Redistributions in any form must be accompanied by information on
 *    how to obtain complete source code for this software and any
 *    accompanying software that uses this software.  The source code
 *    must either be included in the distribution or be available for no
 *    more than the cost of distribution plus a nominal fee, and must be
 *    freely redistributable under reasonable conditions.  For an
 *    executable file, complete source code means the source code for all
 *    modules it contains.  It does not include source code for modules or
 *    files that typically accompany the major components of the operating
 *    system on which the executable file runs.
 *
 * THIS SOFTWARE IS PROVIDED BY GOODDATA ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT, ARE DISCLAIMED.  IN NO EVENT SHALL ORACLE BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.gooddata.connector.backend;

import com.gooddata.connector.driver.DerbySqlDriver;
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
        String protocol = "jdbc:derby:";
        return DriverManager.getConnection(protocol + getProjectId() + ";create=true");
    }

    /**
     * {@inheritDoc}
     */
    public void dropSnapshots() {
        l.debug("Dropping derby snapshots "+getProjectId());
        File derbyDir = new File (System.getProperty("derby.system.home") +
                System.getProperty("file.separator") + getProjectId());
        derbyDir.delete();
        l.debug("Finished dropping derby snapshots "+getProjectId());
    }

}
