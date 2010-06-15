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
            l.debug("Executed SQL: statement='" + sql + "', result='" + rc + "'");
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
     * Execute update.
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
