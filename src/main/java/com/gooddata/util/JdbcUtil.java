package com.gooddata.util;

import java.sql.Connection;
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
     */
    public static int executeUpdate(Connection con, String sql) throws SQLException {
        Statement s = null;
        int rc = 0;
        try {
            s = con.createStatement();
            rc = s.executeUpdate(sql);
            l.trace("Executed Derby SQL: statement='" + sql + "', result='" + rc + "'");
            return rc;
        }
        catch (SQLException e) {
            l.error("Error executing Derby SQL: statement='" + sql + "', result='" + rc + "'", e);
            throw e;
        }
        finally {
            if( s!= null )
                s.close();
        }

    }


}
