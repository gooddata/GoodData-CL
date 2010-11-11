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

package com.gooddata.csv;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gooddata.connector.Constants;
import com.gooddata.exception.InvalidParameterException;
import com.gooddata.util.CSVReader;

import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.util.FileUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * GoodData CSV data type guessing
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class DataTypeGuess {

    private static final String[] DATE_FORMATS = {"yyyy-MM-dd", "MM/dd/yyyy","M/d/yyyy","MM-dd-yyyy",
            "yyyy-M-d","M-d-yyyy"};
    private static DateTimeFormatter[] KNOWN_FORMATS;

	private final boolean hasHeader;
	private String defaultLdmType = null;
	
	public DataTypeGuess(boolean hasHeader) {
		this.hasHeader = hasHeader;
        KNOWN_FORMATS = new DateTimeFormatter[DATE_FORMATS.length];
        for(int i=0; i<DATE_FORMATS.length; i++) {
            KNOWN_FORMATS[i] = DateTimeFormat.forPattern(DATE_FORMATS[i]);
        }
	}
	
	/**
     * Tests if the String is integer
     * @param t the tested String
     * @return true if the String is integer, false otherwise
     */
    public static boolean isInteger(String t) {
        try {
            Integer.parseInt(t);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Tests if the String is decimal
     * @param t the tested String
     * @return true if the String is decimal, false otherwise
     */
    public static boolean isDecimal(String t) {
    	for (String c : Constants.DISCARD_CHARS) {
    		t = t.replace(c, "");
    	}
        try {
            /*
            if(isInteger(t))
                return false;
            */
            Double.parseDouble(t);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Tests if the String is date
     * @param t the tested String
     * @return true if the String is date, false otherwise
     */
    public static String getDateFormat(String t) {
        for(int i=0; i<KNOWN_FORMATS.length; i++) {
            DateTimeFormatter d = KNOWN_FORMATS[i];
            try {
                DateTime dt = d.parseDateTime(t);
                if(t.equals(d.print(dt)))
                    return DATE_FORMATS[i];
            }
            catch(IllegalArgumentException e) {
                // do nothing    
            }
        }
        return null;
    }
    
    /**
     * Guesses the CSV schema
     * @param separator field separator
     * @return the String[] with the CSV column types
     * @throws IOException in case of IO issue
     */
    public SourceColumn[] guessCsvSchema(URL url, char separator) throws IOException {
    	return guessCsvSchema(url.openStream(), separator);
    }

    /**
     * Guesses the CSV schema
     * @param is CSV stream
     * @param separator field separator 
     * @return the String[] with the CSV column types
     * @throws IOException in case of IO issue
     */
    public SourceColumn[] guessCsvSchema(InputStream is, char separator) throws IOException {
    	CSVReader cr = FileUtil.createUtf8CsvReader(is, separator);
    	return guessCsvSchema(cr);
    }

    /**
     * Guesses the CSV schema
     * @param cr CSV reader
     * @return the String[] with the CSV column types
     * @throws IOException in case of IO issue
     */
    public SourceColumn[] guessCsvSchema(CSVReader cr) throws IOException {
        String[] header = null;

        if(hasHeader) {
            header = cr.readNext();
        }

        List<Set<String>> excludedColumnTypes = new ArrayList<Set<String>>();
        String[] dateFormats = new String[header.length];

        if (defaultLdmType == null) {
	        String[] row = cr.readNext();
	        for(int i=0; i < row.length; i++) {
	            HashSet<String> allTypes = new HashSet<String>();
	            excludedColumnTypes.add(allTypes);
	        }
	        int countdown = 1000;
	        while(row != null && countdown-- >0) {
	            for(int i=0; i< row.length; i++) {
                    if(i >= excludedColumnTypes.size())
                        throw new InvalidParameterException("The CSV file contains rows with different number of columns." +
                                " Quitting.");
	                Set<String> types = excludedColumnTypes.get(i);
	                String value = row[i];
	                String dateFormat = getDateFormat(value);
	                if(dateFormat == null) {
	                    types.add(SourceColumn.LDM_TYPE_DATE);
	                } else {
	                	dateFormats[i] = dateFormat;
	                }
	                if(!isDecimal(value)) {
	                    types.add(SourceColumn.LDM_TYPE_FACT);
	                }
	            }
	            row = cr.readNext();
	        }
        }
        
        final int columns = (header == null) ? excludedColumnTypes.size() : header.length;
        SourceColumn[] ret = new SourceColumn[columns];
        for(int i=0; i < columns; i++) {
            final String ldmType;
            if (defaultLdmType != null)
            	ldmType = defaultLdmType;
        	else {
                if(i >= excludedColumnTypes.size())
                        throw new InvalidParameterException("The CSV file contains rows with different number of columns." +
                                " Quitting.");
        		final Set<String> excludedColumnType = excludedColumnTypes.get(i);
        		if(!excludedColumnType.contains(SourceColumn.LDM_TYPE_DATE))
	            	ldmType = SourceColumn.LDM_TYPE_DATE;
	            else if(!excludedColumnType.contains(SourceColumn.LDM_TYPE_FACT))
	            	ldmType = SourceColumn.LDM_TYPE_FACT;
	            else
	            	ldmType = SourceColumn.LDM_TYPE_ATTRIBUTE;
        	}

            ret[i] = new SourceColumn(null, ldmType, null);
            if (SourceColumn.LDM_TYPE_DATE.equals(ldmType)) {
            	ret[i].setFormat(dateFormats[i]);
            }
        }
        return ret;
    }

    /**
     * returns default LDM type to be associated with detected fields rather
     * than by guessing
     * 
     * @return
     */
	public String getDefaultLdmType() {
		return defaultLdmType;
	}

	/**
	 * sets the default LDM type to be associated with detected fields rather
     * than by guessing
     * 
	 * @param defaultLdmType
	 */
	public void setDefaultLdmType(String defaultLdmType) {
		this.defaultLdmType = defaultLdmType;
	}


}
