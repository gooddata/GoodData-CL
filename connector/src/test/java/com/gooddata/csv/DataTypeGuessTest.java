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
import java.io.InputStreamReader;

import com.gooddata.util.FileUtil;

import junit.framework.TestCase;
import au.com.bytecode.opencsv.CSVReader;

/**
 * GoodData
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class DataTypeGuessTest extends TestCase {

    public void testIsInteger() {
        assertTrue(DataTypeGuess.isInteger("12"));
        assertFalse(DataTypeGuess.isInteger("12.0"));
        assertFalse(DataTypeGuess.isInteger("AAA"));
        assertFalse(DataTypeGuess.isInteger("1E3"));
    }

    public void testIsDecimal() {
        assertTrue(DataTypeGuess.isDecimal("1E3"));
        assertTrue(DataTypeGuess.isDecimal("12.3"));
        assertTrue(DataTypeGuess.isDecimal("12"));
    }

    public void testIsDate() {
        assertTrue(DataTypeGuess.isDate("2010-11-12"));
        assertFalse(DataTypeGuess.isDate("2010-13-12"));
        assertTrue(DataTypeGuess.isDate("11/12/2010"));
        assertFalse(DataTypeGuess.isDate("13/12/2010"));
    }

    private String concatArray(String[] a) {
        String ret = "";
        for(String c : a)
            ret += c;
        return ret;
    }

    public void testGuessCsvSchema() throws IOException {
        CSVReader csvr = FileUtil.getResourceAsCsvReader("/com/gooddata/csv/quotes.csv");
        String[] types = DataTypeGuess.guessCsvSchema(csvr, true);
        assertEquals(concatArray(types), concatArray(new String[] {"FACT","ATTRIBUTE","ATTRIBUTE","ATTRIBUTE",
                "ATTRIBUTE","ATTRIBUTE","DATE","FACT","FACT","FACT","FACT","FACT","FACT"}));
        csvr = FileUtil.getResourceAsCsvReader("/com/gooddata/csv/department.csv");
        types = DataTypeGuess.guessCsvSchema(csvr, true);
        assertEquals(concatArray(types), concatArray(new String[] {"ATTRIBUTE","ATTRIBUTE"}));
        csvr = new CSVReader(new InputStreamReader(getClass().getResource("/com/gooddata/csv/employee.csv").openStream()));
        types = DataTypeGuess.guessCsvSchema(csvr, true);
        assertEquals(concatArray(types), concatArray(new String[] {"ATTRIBUTE","ATTRIBUTE","ATTRIBUTE","ATTRIBUTE"}));
        csvr = new CSVReader(new InputStreamReader(getClass().getResource("/com/gooddata/csv/salary.csv").openStream()));
        types = DataTypeGuess.guessCsvSchema(csvr, true);
        assertEquals(concatArray(types), concatArray(new String[] {"ATTRIBUTE","ATTRIBUTE","FACT","DATE"}));        
    }

}
