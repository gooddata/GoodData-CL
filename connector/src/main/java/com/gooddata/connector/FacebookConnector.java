/*
 * Copyright (c) 2009, GoodData Corporation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice, this list of conditions and
 *        the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *        and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *     * Neither the name of the GoodData Corporation nor the names of its contributors may be used to endorse
 *        or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.gooddata.connector;

import com.gooddata.Constants;
import com.gooddata.exception.InvalidParameterException;
import com.gooddata.exception.ProcessingException;
import com.gooddata.exception.SfdcException;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.processor.CliParams;
import com.gooddata.processor.Command;
import com.gooddata.processor.ProcessingContext;
import com.gooddata.transform.Transformer;
import com.gooddata.util.CSVWriter;
import com.gooddata.util.FileUtil;
import com.gooddata.util.StringUtil;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;

import javassist.bytecode.annotation.Annotation;
import org.apache.commons.codec.digest.DigestUtils;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GoodData Facebook connector
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class FacebookConnector extends AbstractConnector implements Connector {


    private static Logger l = Logger.getLogger(FacebookConnector.class);

    private String oauthToken;
    private String query;

    /**
     * Creates a new Facebook connector
     * @return a new instance of the FacebookConnector
     */
    public static FacebookConnector createConnector() {
        return new FacebookConnector();
    }

    /**
     * Saves a template of the config file
     * @param name new schema name
     * @param configFileName config file name
     * @param query FQL query
     * @throws java.io.IOException if there is a problem with writing the config file
     * @throws java.sql.SQLException if there is a problem with the db
     */
    public static void saveConfigTemplate(String name, String configFileName, String query, String folder)
            throws IOException {
        l.debug("Saving Facebook config template.");
        final SourceSchema s = SourceSchema.createSchema(name);
        List<String> fields = getSelectColumns(query);
        for(String field : fields) {
            SourceColumn column = new SourceColumn(field, SourceColumn.LDM_TYPE_ATTRIBUTE, field);
            if(folder != null && folder.length()>0)
                column.setFolder(folder);
            s.addColumn(column);
            l.debug("GenerateJdbcConfig: Processing column '"+field+"' type '"+SourceColumn.LDM_TYPE_ATTRIBUTE+"'");
        }
        s.writeConfig(new File(configFileName));
        l.debug("Saved Facebook config template.");
    }


    private static List<String> getSelectColumns(String query) {
        List<String> ret = new ArrayList<String>();
        Pattern regexp = Pattern.compile("select.*?from");
        Matcher m = regexp.matcher(query.toLowerCase());
        if(m.find()) {
            String sp = m.group();
            sp = sp.substring(6,sp.length()-4).trim();
            String[] fields = sp.split(",");
            for(String field : fields) {
                field = field.trim();
                if(field.length()<=0) {
                    l.debug("The passed string '"+query+"' doesn't have the FQL SELECT ... FROM structure!");
                    throw new InvalidParameterException("The passed string '"+query+"' doesn't have the FQL SELECT ... FROM structure!");
                }
                ret.add(field);
            }
        }
        else {
            l.debug("The passed string '"+query+"' doesn't have the FQL SELECT ... FROM structure!");
            throw new InvalidParameterException("The passed string '"+query+"' doesn't have the FQL SELECT ... FROM structure!");
        }
        return ret;
    }

    private Class constructResultClass(List<String> cols) throws IOException {
        try {
            ClassPool p = ClassPool.getDefault();
            CtClass c = p.makeClass("FacebookQueryResult");
            ClassFile cf = c.getClassFile();

            AnnotationsAttribute attribute = new AnnotationsAttribute(cf.getConstPool(), AnnotationsAttribute.visibleTag);
            Annotation ant = new Annotation(cf.getConstPool(), ClassPool.getDefault().get("com.restfb.Facebook"));
            attribute.addAnnotation(ant);
            for(String col : cols) {
                CtField f = CtField.make("public String "+col+";", c);
                f.getFieldInfo().addAttribute(attribute);
                c.addField(f);
            }

            return c.toClass();
        } catch (NotFoundException e) {
            throw new IOException(e);
        } catch (CannotCompileException e) {
            throw new IOException(e);
        }

    }

    private String extractValue(Object o, String fieldName) throws IOException {
        try {
            Object value = o.getClass().getDeclaredField(fieldName).get(o);
            if(value != null)
                return value.toString();
            else
                return "";
        } catch (IllegalAccessException e) {
            throw new IOException(e);
        } catch (NoSuchFieldException e) {
            throw new IOException(e);
        }
    }

    public void dump(String file) throws IOException {
        extract(file, false);
    }

    /**
     * {@inheritDoc}
     */
    public void extract(String file, final boolean transform) throws IOException {
        File dataFile = new File(file);

        l.debug("Extracting Facebook data to file="+dataFile.getAbsolutePath());
        CSVWriter cw = FileUtil.createUtf8CsvEscapingWriter(dataFile);
        Transformer t = Transformer.create(schema);
        String[] header = t.getHeader(true);
        cw.writeNext(header);

        List<String> cols = getSelectColumns(getQuery());
        Class resultClass = constructResultClass(cols);

        FacebookClient fc = new DefaultFacebookClient(oauthToken);

        List result = fc.executeQuery(query, resultClass);

        if(result != null && result.size() > 0) {
            l.debug("Started retrieving Facebook data.");
            for(Object o : result) {
                String[] row = new String[cols.size()];
                for(int i=0; i< cols.size(); i++) {
                    row[i] = extractValue(o, cols.get(i));
                }
                if(transform)
                    row = t.transformRow(row, DATE_LENGTH_UNRESTRICTED);
                cw.writeNext(row);
                cw.flush();
            }
        }
        else {
            l.debug("The Facebook query hasn't returned any row.");
            throw new IOException("The Facebook query hasn't returned any row.");
        }
        l.debug("Retrieved " + result.size() + " rows of Facebook data.");
        cw.close();
    }

    /**
     * {@inheritDoc}
     */
    public boolean processCommand(Command c, CliParams cli, ProcessingContext ctx) throws ProcessingException {
        try {
            if(c.match("GenerateFacebookConfig")) {
                generateConfig(c, cli, ctx);
            }
            else if(c.match("LoadFacebook") || c.match("UseFacebook")) {
                load(c, cli, ctx);
            }
            else
                return super.processCommand(c, cli, ctx);
        }
        catch (IOException e) {
            throw new ProcessingException(e);
        }
        return true;
    }

    /**
     * Loads new Facebook file command processor
     * @param c command
     * @param p command line arguments
     * @param ctx current processing context
     * @throws IOException in case of IO issues
     */
    private void load(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        String configFile = c.getParamMandatory("configFile");
        String query = c.getParamMandatory("query");
        String auth = c.getParamMandatory("authToken");
        c.paramsProcessed();

        File conf = FileUtil.getFile(configFile);

        initSchema(conf.getAbsolutePath());
        setQuery(query);
        setOauthToken(auth);
        // sets the current connector
        ctx.setConnector(this);
        setProjectId(ctx);
        l.info("Facebook Connector successfully loaded (query: " + query + ").");
    }

    /**
     * Generate new config file from CSV command processor
     * @param c command
     * @param p command line arguments
     * @param ctx current processing context
     * @throws IOException in case of IO issues
     */
    private void generateConfig(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        String name = c.getParamMandatory("name");
        String configFile = c.getParamMandatory("configFile");
        String query = c.getParamMandatory("query");
    	String folder = c.getParam( "folder");
        c.paramsProcessed();

        FacebookConnector.saveConfigTemplate(name, configFile, query, folder);
        l.info("Facebook Connector configuration successfully generated. See config file: "+configFile);
    }



    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
