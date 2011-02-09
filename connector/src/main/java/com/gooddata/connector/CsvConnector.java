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

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

import com.gooddata.exception.InvalidParameterException;
import com.gooddata.naming.N;
import com.gooddata.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import com.gooddata.csv.DataTypeGuess;
import com.gooddata.exception.ProcessingException;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.processor.CliParams;
import com.gooddata.processor.Command;
import com.gooddata.processor.ProcessingContext;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * GoodData CSV Connector
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class CsvConnector extends AbstractConnector implements Connector {

    private static Logger l = Logger.getLogger(CsvConnector.class);

    // data file
    private File dataFile;

    // field separator
    private char separator = ',';

    // field separator
    private boolean hasHeader = true;

    /**
     * Creates GoodData CSV connector
     */
    protected CsvConnector() {
        super();
    }

    /**
     * Create a new GoodData CSV connector. This constructor creates the connector from a config file
     * @return new CSV Connector
     */
    public static CsvConnector createConnector() {
        return new CsvConnector();    
    }

    /**
     * {@inheritDoc}
     */
    public void extract(String dir) throws IOException {
        File dataFile = new File(dir + System.getProperty("file.separator") + "data.csv");
        extract(dataFile.getAbsolutePath(), true);
    }

    /**
     * {@inheritDoc}
     */
    public void dump(String file) throws IOException {
        extract(file, false);
    }

    /**
     * Extract rows
     * @param file name of the target file
     * @param extendDates add date/time facts
     * @throws IOException
     */
    public void extract(String file, final boolean extendDates) throws IOException {
        // Is there an IDENTITY connection point?
        int identityColumn = schema.getIdentityColumn();
        CSVReader cr = FileUtil.createUtf8CsvReader(this.getDataFile(), this.getSeparator());
        CSVWriter cw = FileUtil.createUtf8CsvWriter(new File(file));
        String[] header = this.populateCsvHeaderFromSchema(schema);
        int colCnt = header.length - ((identityColumn>=0)?1:0);
        String[] row = null;
        if(hasHeader)  {
            row = cr.readNext();
            if(row.length != colCnt) {
                throw new InvalidParameterException("The delimited file "+this.getDataFile()+" has different number of columns than " +
                        "it's configuration file. Row="+1);
            }
        }
        // add the extra date headers
        DateColumnsExtender dateExt = new DateColumnsExtender(schema);
        if(extendDates)
            header = dateExt.extendHeader(header);
        cw.writeNext(header);
        row = cr.readNext();
        int rowCnt = 0;
        while (row != null) {
            rowCnt++;
            if(row.length != colCnt) {
                if(!(row.length == 1 && row[0].length() == 0)) {
                    // this is not empty line
                    throw new InvalidParameterException("The delimited file "+this.getDataFile()+" has different number of columns than " +
                        "it's configuration file. Row="+rowCnt);
                }
                else {
                    row = cr.readNext();
                    continue;
                }
            }
            if(identityColumn>=0) {
                String key = "";
                List<String> rowL = new ArrayList<String>(row.length+1);
                List<SourceColumn> columns = schema.getColumns();
                for(int i=0; i< row.length; i++) {
                    int adjustedConfigIndex = (i >= identityColumn) ? (i+1) : (i);
                    if(SourceColumn.LDM_TYPE_ATTRIBUTE.equalsIgnoreCase(columns.get(adjustedConfigIndex).getLdmType()) ||
                       SourceColumn.LDM_TYPE_DATE.equalsIgnoreCase(columns.get(adjustedConfigIndex).getLdmType()) ||
                       SourceColumn.LDM_TYPE_REFERENCE.equalsIgnoreCase(columns.get(adjustedConfigIndex).getLdmType())
                    ) {
                        key += row[i] + "|";
                    }
                    rowL.add(row[i]);
                }
                String hex = DigestUtils.md5Hex(key);
                rowL.add(identityColumn,hex);
                row = rowL.toArray(new String[]{});
            }
            // add the extra date columns
            if(extendDates)
                row = dateExt.extendRow(row);
            cw.writeNext(row);
            row = cr.readNext();
        }
        cw.flush();
        cw.close();
        cr.close();
    }

    /**
     * Saves a template of the config file
     * @param configFileName the new config file name
     * @param dataFileName the data file
     * @param defaultLdmType default LDM type
     * @param folder default folder
     * @param separator field separator
     * @throws IOException in case of an IO issue
     */
    public static void saveConfigTemplate(String configFileName, String dataFileName, String defaultLdmType, String folder, char separator) throws IOException {
    	SourceSchema s = guessSourceSchema(configFileName, dataFileName, defaultLdmType, folder, separator);
        s.writeConfig(new File(configFileName));
    }
    
    /**
     * Generates a source schema from the headers of a CSV file with a help of a partial config file
     * @param configFileName config file name
     * @param dataFileName CSV data file name
     * @param defaultLdmType default LDM type
     * @param folder folder
     * @param separator field separator
     * @return new SourceSchema
     * @throws IOException in case of IO issues
     */
    static SourceSchema guessSourceSchema (String configFileName, String dataFileName, String defaultLdmType, String folder, char separator) throws IOException {
    	File configFile = new File(configFileName);
    	InputStream configStream = configFile.exists() ? new FileInputStream(configFile) : null;
    	return guessSourceSchema(configStream, new File(dataFileName).toURI().toURL(), defaultLdmType, folder, separator);
    }
    
    static SourceSchema guessSourceSchema (InputStream configStream, URL dataUrl, String defaultLdmType, String folder, char separator) throws IOException {
        String name = URLDecoder.decode(FileUtil.getFileName(dataUrl).split("\\.")[0], "utf-8").trim();
        String[] headers = FileUtil.getCsvHeader(dataUrl, separator);
        int i = 0;
        final SourceSchema srcSchm;
        if (configStream != null) {
        	srcSchm = SourceSchema.createSchema(configStream);
        } else {
            int idmax = Constants.MAX_SCHEMA_NAME_LENGTH - 3;
            if (name.length() > idmax)
                name = name.substring(0, idmax);
        	srcSchm = SourceSchema.createSchema(name);
        }
        final int knownColumns = srcSchm.getColumns().size();
        Set<String> srcColumnNames = getColumnNames(srcSchm.getColumns());
        NameTransformer idGen = new NameTransformer(new NameTransformer.NameTransformerCallback() {
        	public String transform(String str) {
        		String idorig = StringUtil.toIdentifier(str);
        		int idmax = Constants.MAX_TABLE_NAME_LENGTH - srcSchm.getName().length() - 3; // good enough for 999 long names
        		if (idorig.length() <= idmax)
        			return idorig;
                if(idmax < 8)
                    throw new InvalidParameterException("The schema name '"+srcSchm+"' is too long. Please use a name " +
                            "up to 32 characters.");
        		return idorig.substring(0, idmax);
        		
        	}
        }, srcColumnNames);
        NameTransformer titleGen = new NameTransformer(new NameTransformer.NameTransformerCallback() {
        	public String transform(String str) {
        		return StringUtil.toTitle(str);
        	}
        });
        if (knownColumns < headers.length) {
        	DataTypeGuess guesser = new DataTypeGuess(true);
        	guesser.setDefaultLdmType(defaultLdmType);
        	SourceColumn[] guessed = guesser.guessCsvSchema(dataUrl, separator);
        	if (guessed.length != headers.length) {
        		throw new AssertionError("The size of data file header is different than the number of guessed fields");
        	}
	        for(int j = knownColumns; j < headers.length; j++) {
	        	final String header = headers[j];
	            final SourceColumn sc;
	            final String identifier = idGen.transform(header);
	            final String title = titleGen.transform(header);
                if(identifier == null || identifier.length() <= 0) {
                    throw new InvalidParameterException("The CSV header can't contain empty names or names with all non-latin characters.");
                }
                if(title == null || title.length() <= 0) {
                    throw new InvalidParameterException("The CSV header can't contain empty names or names with all non-latin characters.");
                }
	            if (defaultLdmType != null) {
	            	sc = new SourceColumn(identifier, defaultLdmType, title, folder);
	            } else {
		            sc = guessed[j];
		            sc.setName(identifier);
		            sc.setTitle(title);
		            sc.setFolder(folder);
	            }
	            srcSchm.addColumn(sc);
	            i++;
	        }
        } 
        return srcSchm;
    }

    /**
     * Data CSV file getter
     * @return the data CSV file
     */
    public File getDataFile() {
        return dataFile;
    }

    /**
     * Data CSV file setter
     * @param dataFile the data CSV file
     */
    public void setDataFile(File dataFile) {
        this.dataFile = dataFile;
    }

    /**
     * {@inheritDoc}
     */
    public boolean processCommand(Command c, CliParams cli, ProcessingContext ctx) throws ProcessingException {
        try {
            if(c.match("GenerateCsvConfig")) {
                generateCsvConfig(c, cli, ctx);
            }
            else if(c.match("LoadCsv") || c.match("UseCsv")) {
                loadCsv(c, cli, ctx);
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
     * Loads new CSV file command processor
     * @param c command
     * @param p command line arguments
     * @param ctx current processing context
     * @throws IOException in case of IO issues
     */
    private void loadCsv(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        String configFile = c.getParamMandatory("configFile");
        String csvDataFile = c.getParamMandatory("csvDataFile");
        File conf = FileUtil.getFile(configFile);
        File csvf = FileUtil.getFile(csvDataFile);
        String sep = c.getParam("separator");
        if(sep != null) {
            if(sep.length() == 0 || (sep.length() > 1 && !"\\t".equals(sep)))
                throw new InvalidParameterException("The CSV separator be non-empty, one character only.");
            char sepChar = "\\t".equals(sep) ? '\t' : sep.charAt(0);
            setSeparator(sepChar);
        }
        else {
            setSeparator(',');
        }
        final String hasHeaderStr = c.getParam("hasHeader");
        if(hasHeaderStr != null) {
            setHasHeader("true".equalsIgnoreCase(hasHeaderStr));
        }

        initSchema(conf.getAbsolutePath());
        setDataFile(new File(csvf.getAbsolutePath()));
        // sets the current connector
        ctx.setConnector(this);
        setProjectId(ctx);
        l.info("CSV Connector successfully loaded (file: " + csvDataFile + ").");
    }

    /**
     * Generate new config file from CSV command processor
     * @param c command
     * @param p command line arguments
     * @param ctx current processing context
     * @throws IOException in case of IO issues
     */
    private void generateCsvConfig(Command c, CliParams p, ProcessingContext ctx) throws IOException {
        String configFile = c.getParamMandatory("configFile");
        String csvHeaderFile = c.getParamMandatory("csvHeaderFile");
        String defaultLdmType = c.getParam( "defaultLdmType");
    	String folder = c.getParam( "folder");
        if (folder == null) {
            // let's try a deprecated variant
            folder = c.getParam("defaultFolder");
        }
        char spr = ',';
        String sep = c.getParam("separator");
        if(sep != null) {
            if(sep.length() == 0 || sep.length() > 1)
                throw new InvalidParameterException("The CSV separator be non-empty, one character only.");
            spr = sep.charAt(0);
        }

        CsvConnector.saveConfigTemplate(configFile, csvHeaderFile, defaultLdmType, folder, spr);
        l.info("CSV Connector configuration successfully generated. See config file: "+configFile);
    }
    
    /**
     * Extracts column names from the list
     * @param columns
     * @return
     */
    private static Set<String> getColumnNames(List<SourceColumn> columns) {
    	Set<String> result = new HashSet<String>();
		for (final SourceColumn col : columns) {
			result.add(StringUtil.toIdentifier(col.getName()));
		}
		return result;
	}

    public char getSeparator() {
        return separator;
    }

    public void setSeparator(char separator) {
        this.separator = separator;
    }

    public boolean isHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }
}
