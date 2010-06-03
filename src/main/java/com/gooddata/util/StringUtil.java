package com.gooddata.util;

/**
 * GoodData
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class StringUtil {

    private static String[] DISCARD_CHARS = {"\"", " ", "!", "?", "%", "&", "#", "*", "+", "-", "=", "/", ",", ".", ">", "<",
            "$", "%", ",", "(", ")", "�", "�", "�","@", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "{" ,"}",
            "[", "]","\\"};

    private static String[] INVALID_CSV_HEADER_CHARS = {"\"", "'", "!", "?", "%", "&", "#", "*", "+", "-", "=", "/", ",", ".", ">", "<",
            "$", "%", ",", "(", ")", "�", "�", "�","@", "{" ,"}",
            "[", "]","\\"};
    
    /**
     * Formats a string as identifier
     * Currently only converts to the lowercase and replace spaces
     * @param s the string to convert to identifier
     * @return converted string
     */
    public static String formatShortName(String s) {
        for ( String r : DISCARD_CHARS ) {
            s = s.replace(r,"_");
        }
        return s.toLowerCase();
    }

    /**
     * Formats a string as title
     * Currently does nothing TBD
     * @param s the string to convert to a title
     * @return converted string
     */
    public static String formatLongName(String s) {
        return s;
    }

    /**
     * Formats a CSV header
     * @param s the string to convert to identifier
     * @return converted string
     */
    public static String csvHeaderToIdentifier(String s) {
        for ( String r : INVALID_CSV_HEADER_CHARS ) {
            s = s.replace(r,"");
        }
        return s.toLowerCase();
    }

    /**
     * Formats a CSV header
     * @param s the string to convert to identifier
     * @return converted string
     */
    public static String csvHeaderToTitle(String s) {
        for ( String r : INVALID_CSV_HEADER_CHARS ) {
            s = s.replace(r,"");
        }
        return s;
    }
}
