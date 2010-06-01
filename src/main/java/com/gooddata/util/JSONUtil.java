package com.gooddata.util;

import net.sf.json.JSONObject;

/**
 * JSON utils
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class JSONUtil {

    /**
     * Helper method: prints out a JSON object
     *
     * @param o the JSON Object
     */
    public static void printJson(JSONObject o) {
        System.out.println(o.toString(2));
    }

}
