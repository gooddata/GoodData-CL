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

package com.gooddata.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;


/**
 * GoodData JDBC utilities
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class JdbcUtil {

    private static Logger l = Logger.getLogger(JdbcUtil.class);

    /**
     * Execute update
     * @param con connection
     * @param sql sql statement
     * @return number of affected rows
     * @throws SQLException in case of a db issue
     */
    public static int executeUpdate(Connection con, String sql) throws SQLException {
        Statement s = null;
        int rc = 0;
        try {
            s = con.createStatement();
            l.debug("Executing SQL: statement='" + sql + "'");
            rc = s.executeUpdate(sql);
            l.debug("Executed SQL: statement='" + sql + "' rows="+rc);
            return rc;
        }
        catch (SQLException e) {
            l.error("Error executing SQL: statement='" + sql + "', result='" + rc + "'", e);
            throw e;
        }
        finally {
            if( s!= null )
                s.close();
        }
    }
    
    /**
     * Executes an update using a prepared statement.
     * <p>
     * Example:
     * <pre> final int myid = 42;
     * JdbcUtil.executeUpdate(con, "select * from table where id = ?", new StatementHandler() {
     *     public void prepare(PreparedStatement stmt) throws SQLException {
     *         stmt.setInt(1, myid);
     *     }
     * });
     * </pre>
     * 
     * @param con connection
     * @param sql sql prepared statement (i.e. may contain the "?" placeholders to be populated by the <tt>sh</tt> handler
     * @param sh {@link StatementHandler} instance to setup the prepared statement
     * @return number of affected rows
     * @throws SQLException in case of a db issue
     */
    public static int executeUpdate(Connection con, String sql, StatementHandler sh) throws SQLException {
    	PreparedStatement s = null;
    	int rc = 0;
    	try {
    		s = con.prepareStatement(sql);
    		sh.prepare(s);
            l.debug("Executing SQL: statement='" + sh.toString() + "'");
    		rc = s.executeUpdate();
    		return rc;
    	} catch (SQLException e) {
    		l.error("Error executing SQL: statement='" + sql + "', result='" + rc + "'", e);
            throw e;
    	} finally {
    		if (s != null)
    			s.close();
    	}
    }

    /**
     * Execute query
     * @param s JDBC statement
     * @param sql sql statement
     * @return Jdbc ResultSet
     * @throws SQLException in case of a db issue 
     */
    public static ResultSet executeQuery(Statement s, String sql) throws SQLException {
        ResultSet rs = null;
        try {
            l.debug("Executing SQL: statement='" + sql + "'");
            rs = s.executeQuery(sql);
            l.debug("Executed SQL: statement='" + sql + "'");
            return rs;
        }
        catch (SQLException e) {
            l.error("Error executing SQL: statement='" + sql + "'", e);
            throw e;
        }
    }

    /**
     * Execute query an passes the ResultSet to the given handler on each record
     * @param c JDBC connection
     * @param sql sql statement
     * @param Jdbc ResultSet handler
     * @throws SQLException in case of a db issue 
     */
    public static void executeQuery(Connection c, String sql, ResultSetHandler handler) throws SQLException {
    	Statement st = null;
    	ResultSet rs = null;
    	try {
    		st = c.createStatement();
            l.debug("Executing SQL: statement='" + st.toString() + "'");
    		rs = executeQuery(st, sql);
    		while (rs.next()) {
    			handler.handle(rs);
    		}
    	} finally {
    		if (rs != null)
    			rs.close();
    		if (st != null)
    			st.close();
    	}
    }
    
    /**
     * Result set handler callback interface for {@link JdbcUtil#executeQuery(Connection, String, ResultSetHandler)}
     */
    public static interface ResultSetHandler {
    	public void handle(ResultSet rs) throws SQLException;
    }
    
    /**
     * Dummy resultset handler callback; literally does nothing
     */
    public static class DummyResultSetHandler implements ResultSetHandler {
    	public void handle(ResultSet rs) throws SQLException {
    		; // intentionally does nothing
    	}
    }
    
    /**
     * Statement handler callback interface for {@link JdbcUtil#executeUpdate(Connection, String, StatementHandler)}
     */
    public static interface StatementHandler {
    	public void prepare(PreparedStatement stmt) throws SQLException;
    }

}
