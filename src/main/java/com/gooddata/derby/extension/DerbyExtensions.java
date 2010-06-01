package com.gooddata.derby.extension;

/**
 * GoodData Derby SQL extension functions
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class DerbyExtensions {

    private static String[] DISCARD_CHARS = {"$", "%", ",", "(", ")", "Û", "£", "´"};

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

}
