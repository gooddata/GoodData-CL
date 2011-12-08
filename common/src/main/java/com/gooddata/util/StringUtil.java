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

package com.gooddata.util;

import com.ibm.icu.text.Transliterator;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * GoodData String utilities
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class StringUtil {

    /**
     * Formats a string as identifier
     * Currently only converts to the lowercase and replace spaces
     *
     * @param s the string to convert to identifier
     * @return converted string
     */
    public static String toIdentifier(String s) {
        return convertToIdentifier(s);
    }

    /**
     * Formats a string as title
     * Currently does nothing TBD
     *
     * @param s the string to convert to a title
     * @return converted string
     */
    public static String toTitle(String s) {
        if (s == null)
            return s;
        //Transliterator t = Transliterator.getInstance("Any-Latin; NFD; [:Nonspacing Mark:] Remove; NFC");
        //s = t.transliterate(s);
        s = s.replaceAll("\"", "");
        return s.trim();
    }

    private static String convertToIdentifier(String s) {
        if (s == null)
            return s;
        Transliterator t = Transliterator.getInstance("Any-Latin; NFD; [:Nonspacing Mark:] Remove; NFC");
        s = t.transliterate(s);
        s = s.replaceAll("[^a-zA-Z0-9_]", "");
        s = s.replaceAll("^[0-9_]*", "");
        //s = s.replaceAll("[_]*$", "");
        //s = s.replaceAll("[_]+", "_");
        return s.toLowerCase().trim();
    }

    /**
     * Converts a {@link Collection} to a <tt>separator<tt> separated string
     *
     * @param separator
     * @param list
     * @return <tt>separator<tt> separated string version of the given list
     */
    public static String join(String separator, Collection<String> list) {
        return join(separator, list, null);
    }

    /**
     * Converts a {@link Collection} to a <tt>separator<tt> separated string.
     * If the <tt>replacement</tt> parameter is not null, it is used to populate
     * the result string instead of list elements.
     *
     * @param separator
     * @param list
     * @param replacement
     * @return <tt>separator<tt> separated string version of the given list
     */
    public static String join(String separator, Collection<String> list, String replacement) {
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        for (final String s : list) {
            if (first)
                first = false;
            else
                sb.append(separator);
            sb.append(replacement == null ? s : replacement);
        }
        return sb.toString();
    }

    /**
     * Parse CSV line
     *
     * @param elements CSV line
     * @return alements as String[]
     */
    public static List<String> parseLine(String elements) throws java.io.IOException {
        if (elements == null) {
            return new ArrayList<String>();
        }
        CSVReader cr = new CSVReader(new StringReader(elements));
        return Arrays.asList(cr.readNext());
    }

    /**
     * Returns first <tt>limit</tt> characters of the string with last 3 letters replaced with ellipsis
     * <p/>
     * Example: <tt>previewString("potatoe", 6)</tt> returns "pot..."
     *
     * @param string
     * @param limit
     * @return
     */
    public static String previewString(String string, int limit) {
        return (string.length() > limit)
                ? string.substring(0, limit - 4) + "..."
                : string;
    }

}
