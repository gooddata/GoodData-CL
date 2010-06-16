package com.gooddata.connector;

import au.com.bytecode.opencsv.CSVWriter;
import com.gooddata.exception.*;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.processor.CliParams;
import com.gooddata.processor.Command;
import com.gooddata.util.FileUtil;
import com.gooddata.util.JdbcUtil;
import com.gooddata.util.StringUtil;
import org.apache.log4j.Logger;
import org.gooddata.connector.AbstractConnector;
import org.gooddata.connector.Connector;
import org.gooddata.connector.backend.ConnectorBackend;
import com.gooddata.processor.ProcessingContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

/**
 * GoodData JDBC Connector
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class JdbcConnector extends AbstractConnector implements Connector {

    private static Logger l = Logger.getLogger(JdbcConnector.class);

    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;
    private String sqlQuery;

    /**
     * Creates a new JDBC connector
     * @param connectorBackend connector backend
     */
    protected JdbcConnector(ConnectorBackend connectorBackend) {
        super(connectorBackend);
    }

    /**
     * Creates a new JDBC connector
     * @param connectorBackend connector backend
     */
    public static JdbcConnector createConnector(ConnectorBackend connectorBackend) {
        return new JdbcConnector(connectorBackend);
    }

    /**
     * Saves a template of the config file
     * @throws com.gooddata.exception.InvalidArgumentException if there is a problem with arguments
     * @throws java.io.IOException if there is a problem with writing the config file
     * @throws SQLException if there is a problem with the db
     * @throws InvalidArgumentException  
     */
    public static void saveConfigTemplate(String name, String configFileName, String jdbcUsr, String jdbcPsw,
                                  String jdbcDriver, String jdbcUrl,String query)
            throws InvalidArgumentException, IOException, SQLException {
        try {
            Class.forName(jdbcDriver).newInstance();
        } catch (InstantiationException e) {
            l.error("Can't load JDBC driver.", e);
        } catch (IllegalAccessException e) {
            l.error("Can't load JDBC driver.", e);
        } catch (ClassNotFoundException e) {
            l.error("Can't load JDBC driver.", e);
        }
        SourceSchema s = SourceSchema.createSchema(name);
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        ResultSetMetaData rsm = null;
        try {
            con = connect(jdbcUrl, jdbcUsr, jdbcPsw);
            st = con.createStatement();
            rs = JdbcUtil.executeQuery(st, query);
            rs.next();
            rsm = rs.getMetaData();
            int cnt = rsm.getColumnCount();
            for(int i=1; i <= cnt; i++) {
                String cnm = StringUtil.formatShortName(rsm.getColumnName(i));
                String cdsc = rsm.getColumnName(i);
                String type = getColumnType(rsm.getColumnType(i));
                s.addColumn(new SourceColumn(cnm, type, cdsc));
            }
            s.writeConfig(new File(configFileName));
        }
        finally {
            if(rs != null && !rs.isClosed())
                rs.close();
            if(st != null && !st.isClosed())
                st.close();
            if(con != null && !con.isClosed())
                con.close();
        }
    }

    private static String getColumnType(int jct) {
        String type;
        switch (jct) {
            case Types.CHAR:
                type = SourceColumn.LDM_TYPE_ATTRIBUTE;
                break;
            case Types.VARCHAR:
                type = SourceColumn.LDM_TYPE_ATTRIBUTE;
                break;
            case Types.NCHAR:
                type = SourceColumn.LDM_TYPE_ATTRIBUTE;
                break;
            case Types.NVARCHAR:
                type = SourceColumn.LDM_TYPE_ATTRIBUTE;
                break;
            case Types.INTEGER:
                type = SourceColumn.LDM_TYPE_ATTRIBUTE;
                break;
            case Types.BIGINT:
                type = SourceColumn.LDM_TYPE_ATTRIBUTE;
                break;
            case Types.FLOAT:
                type = SourceColumn.LDM_TYPE_FACT;
                break;
            case Types.DOUBLE:
                type = SourceColumn.LDM_TYPE_FACT;
                break;
            case Types.DECIMAL:
                type = SourceColumn.LDM_TYPE_FACT;
                break;
            case Types.NUMERIC:
                type = SourceColumn.LDM_TYPE_FACT;
                break;
            case Types.DATE:
                type = SourceColumn.LDM_TYPE_DATE;
                break;
            case Types.TIMESTAMP:
                type = SourceColumn.LDM_TYPE_DATE;
                break;
            default:
                type = SourceColumn.LDM_TYPE_ATTRIBUTE;
                break;
        }
        return type;
    }

    /**
     * Extracts the source data CSV to the Derby database where it is going to be transformed
     * @throws com.gooddata.exception.ModelException in case of PDM schema issues
     */
    public void extract() throws ModelException, IOException {
        Connection con = null;
        Statement s = null;
        ResultSet rs = null;
        try {
            con = connect();
            File dataFile = FileUtil.getTempFile();
            CSVWriter cw = new CSVWriter(new FileWriter(dataFile));
            s = con.createStatement();
            rs = JdbcUtil.executeQuery(s, getSqlQuery());
            l.debug("Started retrieving JDBC data.");
            cw.writeAll(rs,false);
            l.debug("Finished retrieving JDBC data.");
            cw.flush();
            cw.close();
            getConnectorBackend().extract(dataFile);
            FileUtil.recursiveDelete(dataFile);
        }
        catch (SQLException e) {
            l.error("Error retrieving data from the JDBC source.", e);    
        }
        finally {
            try {
                if (rs != null && !rs.isClosed())
                    rs.close();
                if (s != null && !s.isClosed())
                    s.close();
                if (con != null && !con.isClosed())
                    con.close();
            }
            catch (SQLException e) {
                throw new InternalError(e.getMessage());
            }
        }
    }

    /**
     * Connect the database
     * @param jdbcUrl JDBC url
     * @param usr JDBC username
     * @param psw JDBC pasword
     * @return JDBC connection
     * @throws SQLException in case of connection issues
     */
    private static Connection connect(String jdbcUrl, String usr, String psw) throws SQLException {
        return DriverManager.getConnection(jdbcUrl, usr, psw);
    }

    /**
     * Connects to the Derby database
     * @return JDBC connection
     * @throws SQLException
     */
    public Connection connect() throws SQLException {
        return connect(getJdbcUrl(), getJdbcUsername(), getJdbcPassword());
    }

    /**
     * JDBC username getter
     * @return JDBC username
     */
    public String getJdbcUsername() {
        return jdbcUsername;
    }

    /**
     * JDBC username setter
     * @param jdbcUsername JDBC username
     */
    public void setJdbcUsername(String jdbcUsername) {
        this.jdbcUsername = jdbcUsername;
    }

    /**
     * JDBC password getter
     * @return JDBC password
     */
    public String getJdbcPassword() {
        return jdbcPassword;
    }

    /**
     * JDBC password setter
     * @param jdbcPassword JDBC password
     */
    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    /**
     * JDBC query getter
     * @return JDBC query
     */
    public String getSqlQuery() {
        return sqlQuery;
    }

    /**
     * JDBC query setter
     * @param sqlQuery JDBC query
     */
    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    /**
     * JDBC url getter
     * @return JDBC url
     */
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    /**
     * JDBC url setter
     * @param jdbcUrl JDBC url
     */
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    /**
     * Processes single command
     * @param c command to be processed
     * @param cli parameters (commandline params)
     * @param ctx processing context
     * @return true if the command has been processed, false otherwise
     */
    public boolean processCommand(Command c, CliParams cli, ProcessingContext ctx) throws ProcessingException {
        try {
            if(c.match("GenerateJdbcConfig")) {
                generateJdbcConfig(c, cli, ctx);
            }
            else if(c.match("LoadJdbc")) {
                loadJdbc(c, cli, ctx);
            }
            else
                return super.processCommand(c, cli, ctx);
        }
        catch (SQLException e) {
            throw new ProcessingException(e);
        }
        catch (IOException e) {
            throw new ProcessingException(e);
        }
        return true;
    }


    private void loadDriver(String drv) {
        try {
            Class.forName(drv).newInstance();
        } catch (InstantiationException e) {
            l.error("Can't load JDBC driver.", e);
        } catch (IllegalAccessException e) {
            l.error("Can't load JDBC driver.", e);
        } catch (ClassNotFoundException e) {
            l.error("Can't load JDBC driver.", e);
        }
    }

    private void loadJdbc(Command c, CliParams p, ProcessingContext ctx) throws IOException, SQLException {
        String configFile = c.getParamMandatory("configFile");
        String usr = null;
        if(c.checkParam("username"))
            usr = c.getParam("username");
        String psw = null;
        if(c.checkParam("password"))
            psw = c.getParam("password");
        String drv = c.getParamMandatory("driver");
        String url = c.getParamMandatory("url");
        String q = c.getParamMandatory("query");
        loadDriver(drv);
        File conf = FileUtil.getFile(configFile);
        initSchema(conf.getAbsolutePath());
        setJdbcUsername(usr);
        setJdbcPassword(psw);
        setJdbcUrl(url);
        setSqlQuery(q);
        // sets the current connector
        ctx.setConnector(this);
        try {
            this.checkProjectId();
        }
        catch(InvalidParameterException e) {
            this.getConnectorBackend().setProjectId(ctx.getProjectId());
        }
    }

    private void generateJdbcConfig(Command c, CliParams p, ProcessingContext ctx) throws IOException, SQLException {
        String configFile = c.getParamMandatory("configFile");
        String name = c.getParamMandatory("name");
        String usr = null;
        if(c.checkParam("username"))
            usr = c.getParam("username");
        String psw = null;
        if(c.checkParam("password"))
            psw = c.getParam("password");
        String drv = c.getParamMandatory("driver");
        String url = c.getParamMandatory("url");
        String query = c.getParamMandatory("query");
        loadDriver(drv);
        File cf = new File(configFile);
        JdbcConnector.saveConfigTemplate(name, cf.getAbsolutePath(), usr, psw, drv, url, query);
    }


}