package com.gooddata.derby.extension;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * GoodData Derby SQL extension functions
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class DerbyExtensions {

    private final static String[] DISCARD_CHARS = {"$", "%", ",", "(", ")", "Û", "£", "´"};
    //private final static SimpleDateFormat dtfb = new SimpleDateFormat("YYYY-MM-DD");

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
                for ( String r : DISCARD_CHARS ) {
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
        
          return 0;

    }

}
