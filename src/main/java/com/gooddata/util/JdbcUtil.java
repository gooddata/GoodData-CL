package com.gooddata.util;

import java.sql.Connection;
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
            rc = s.executeUpdate(sql);
            l.trace("Executed Derby SQL: statement='" + sql + "', result='" + rc + "'");
            System.err.println("Executed Derby SQL: statement='" + sql + "', rows='" + rc + "'");
            return rc;
        }
        catch (SQLException e) {
            l.error("Error executing Derby SQL: statement='" + sql + "', result='" + rc + "'", e);
            System.err.println("Error executing Derby SQL: statement='" + sql + "', result='" + rc + "'");
            throw e;
        }
        finally {
            if( s!= null )
                s.close();
        }
    }

    /**
     * Execute query
     * @param con connection
     * @param sql sql statement
     * @return Jdbc ResultSet
     * @throws SQLException in case of a db issue 
     */
    public static ResultSet executeQuery(Connection con, String sql) throws SQLException {
        Statement s = null;
        ResultSet rs = null;
        try {
            s = con.createStatement();
            rs = s.executeQuery(sql);
            l.trace("Executed Derby SQL: statement='" + sql + "'");
            return rs;
        }
        catch (SQLException e) {
            l.error("Error executing Derby SQL: statement='" + sql + "'", e);
            throw e;
        }
        finally {
            if( s!= null )
                s.close();
        }

    }


}
