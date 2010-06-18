package com.gooddata.util;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

/**
 * JSON utils
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class JSONUtil {

    private static Logger l = Logger.getLogger(JSONUtil.class);

    /**
     * Helper method: prints out a JSON object
     *
     * @param o the JSON Object
     */
    public static void printJson(JSONObject o) {
        l.info(o.toString(2));
    }

}
