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
import java.text.SimpleDateFormat;
import java.util.*;

import com.gooddata.exception.InvalidParameterException;
import com.gooddata.integration.model.Column;
import com.gooddata.naming.N;
import com.gooddata.util.*;
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

    private String[] mergeArrays(String[] a, String[] b) {
        List<String> lst = new ArrayList<String>(Arrays.asList(a));
        lst.addAll(Arrays.asList(b));
        return lst.toArray(a);
    }

    /**
     * {@inheritDoc}
     */
    public void extract(String dir) throws IOException {
        CSVReader cr = FileUtil.createUtf8CsvReader(this.getDataFile(), this.getSeparator());
        CSVWriter cw = FileUtil.createUtf8CsvWriter(new File(dir + System.getProperty("file.separator") + "data.csv"));
        String[] header = this.populateCsvHeaderFromSchema();
        String[] row = cr.readNext();
        if(row.length != header.length) {
            throw new InvalidParameterException("The delimited file "+this.getDataFile()+" has different number of columns than " +
                    "it's configuration file!");
        }

        // Add column headers for the extra date columns
        List<Integer> dateColumnIndexes = new ArrayList<Integer>();
        List<DateTimeFormatter> dateColumnFormats = new ArrayList<DateTimeFormatter>();
        List<SourceColumn> dates = schema.getDates();
        String[] rowExt = new String[dates.size()];
        for(int i= 0; i < dates.size(); i++)  {
            SourceColumn c = dates.get(i);
            rowExt[i] = StringUtil.toIdentifier(c.getName()) + N.DT_SLI_SFX;
            dateColumnIndexes.add(schema.getColumnIndex(c));
            String fmt = c.getFormat();
            if(fmt == null || fmt.length() <= 0)
                fmt = Constants.DEFAULT_DATE_FMT_STRING;
            dateColumnFormats.add(DateTimeFormat.forPattern(fmt));
        }
        if(rowExt.length > 0)
            header = mergeArrays(header, rowExt);

        // Add column headers for the extra datetime columns
        List<Integer> dateTimeColumnIndexes = new ArrayList<Integer>();
        List<DateTimeFormatter> dateTimeColumnFormats = new ArrayList<DateTimeFormatter>();
        List<SourceColumn> dateTimes = schema.getDatetimes();
        rowExt = new String[2*dateTimes.size()];
        for(int i= 0; i < dateTimes.size(); i++)  {
            SourceColumn c = dateTimes.get(i);
            rowExt[i] = StringUtil.toIdentifier(c.getName()) + N.DT_SLI_SFX;
            rowExt[i+dateTimes.size()] = StringUtil.toIdentifier(c.getName()) + N.TM_SLI_SFX;
            dateTimeColumnIndexes.add(schema.getColumnIndex(c));
            String fmt = c.getFormat();
            if(fmt == null || fmt.length() <= 0)
                fmt = Constants.DEFAULT_DATETIME_FMT_STRING;
            dateTimeColumnFormats.add(DateTimeFormat.forPattern(fmt));
        }
        if(rowExt.length > 0)
            header = mergeArrays(header, rowExt);

        cw.writeNext(header);
        row = cr.readNext();
        final DateTimeFormatter baseFmt = DateTimeFormat.forPattern(Constants.DEFAULT_DATE_FMT_STRING);
        final DateTime base = baseFmt.parseDateTime("1900-01-01");
        while (row != null) {
            // add the extra date columns
            rowExt = new String[dateColumnIndexes.size()];
            for(int i = 0; i < dateColumnIndexes.size(); i++) {
                String dateValue = row[dateColumnIndexes.get(i)];
                if(dateValue != null && dateValue.trim().length()>0) {
                    try {
                        DateTimeFormatter formatter = dateColumnFormats.get(i);
                        DateTime dt = formatter.parseDateTime(dateValue);
                        Days ds = Days.daysBetween(base, dt);
                        rowExt[i] = Integer.toString(ds.getDays() + 1);
                    }
                    catch (IllegalArgumentException e) {
                        l.debug("Can't parse date "+dateValue);
                        rowExt[i] = "";
                    }
                }
                else {
                    rowExt[i] = "";
                }
            }
            if(rowExt.length > 0)
                 row = mergeArrays(row, rowExt);

            // add the extra datetime columns
            rowExt = new String[2*dateTimeColumnIndexes.size()];
            for(int i = 0; i < dateTimeColumnIndexes.size(); i++) {
                String dateTimeValue = row[dateTimeColumnIndexes.get(i)];
                if(dateTimeValue != null && dateTimeValue.trim().length()>0) {
                    try {
                        DateTimeFormatter formatter = dateTimeColumnFormats.get(i);
                        DateTime dt = formatter.parseDateTime(dateTimeValue);
                        Days ds = Days.daysBetween(base, dt);
                        rowExt[i] = Integer.toString(ds.getDays() + 1);
                        int  ts = dt.getSecondOfDay();
                        rowExt[i+dateTimeColumnIndexes.size()] = Integer.toString(ts);
                    }
                    catch (IllegalArgumentException e) {
                        l.debug("Can't parse datetime "+dateTimeValue);
                        rowExt[i] = "";
                    }
                }
                else {
                    rowExt[i] = "";
                }
            }
            if(rowExt.length > 0)
                 row = mergeArrays(row, rowExt);

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
}
