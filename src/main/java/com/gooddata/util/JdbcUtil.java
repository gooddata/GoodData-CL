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


}
