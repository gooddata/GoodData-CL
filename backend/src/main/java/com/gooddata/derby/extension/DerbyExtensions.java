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

package com.gooddata.derby.extension;

import com.gooddata.connector.driver.Constants;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * GoodData Derby SQL extension functions
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class DerbyExtensions {

    private final static DateTimeFormatter baseFmt = DateTimeFormat.forPattern("yyyy-MM-dd");

    // Empty date id
    private final static int EMPTY_DATE_ID = 2147483647;

    /**
     * Aggresivelly converts string to a numeric type.
     * This function is used as a stored procedure in Derby SQL
     * @param str String to convert
     * @return the converted as double
     */
    public static double atod(String str) {
        try {
            return Double.parseDouble(str);
        }
        catch (NumberFormatException e) {
            try {
                for ( String r : Constants.DISCARD_CHARS ) {
                    str = str.replace(r,"");
                }
                Double.parseDouble(str);
            }
            catch (NumberFormatException e1) {
                return 0;
            }
            return 0;
        }
    }

    /**
     * Converts date to int (number of days from 1900-01-01
     * @param dt the converted date
     * @param fmt date format (e.g. YYYY-MM-DD)
     * @return
     */
    public static int dttoi(String dt, String fmt) {
        try {
            if(fmt == null || fmt.length() <= 0 || fmt.equals("null"))
                fmt = "yyyy-MM-dd";
            DateTime base = baseFmt.parseDateTime("1900-01-01");
            DateTimeFormatter dtFmt = DateTimeFormat.forPattern(fmt);
            DateTime d = dtFmt.parseDateTime(dt);
            Days ds = Days.daysBetween(base, d);
            return ds.getDays() + 1;
        }
        catch (IllegalArgumentException e) {
            return EMPTY_DATE_ID;
        }
    }

}
