package org.gooddata.transformation.executor;

import com.gooddata.exceptions.ModelException;
import com.gooddata.integration.model.Column;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.transformation.executor.model.PdmColumn;
import com.gooddata.transformation.executor.model.PdmLookupReplication;
import com.gooddata.transformation.executor.model.PdmSchema;
import com.gooddata.transformation.executor.model.PdmTable;
import com.gooddata.util.JdbcUtil;
import com.gooddata.util.StringUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * GoodData abstract SQL executor. Generates the DDL (tables and indexes), DML (transformation SQL) and other
 * SQL statements necessary for the data normalization (lookup generation)
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public abstract class AbstractSqlExecutor implements SqlExecutor {

    private static Logger l = Logger.getLogger(AbstractSqlExecutor.class);

    /**
     * Executes the copying of the referenced lookup tables
     * @param c JDBC connection
     * @param schema the PDM schema
     * @throws com.gooddata.exceptions.ModelException if there is a problem with the PDM schema (e.g. multiple source or fact tables)
     * @throws java.sql.SQLException in case of db problems
     */
    public void executeLookupReplicationSql(Connection c, PdmSchema schema) throws ModelException, SQLException {
        for (PdmLookupReplication lr : schema.getLookupReplications()) {
            JdbcUtil.executeUpdate(c,
                "DELETE FROM " + lr.getReferencingLookup()
            );
            JdbcUtil.executeUpdate(c,
                "INSERT INTO " + lr.getReferencingLookup() + "(id," + lr.getReferencingColumn() +",hashid)" +
                " SELECT id," + lr.getReferencedColumn() + "," + lr.getReferencedColumn() + " FROM " + lr.getReferencedLookup()
            );
        }
    }

}