package com.gooddata.integration.model;

import net.sf.json.JSONObject;

/**
 * GoodData project object wrapper
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class Project {

    private String link;
    private String id;
    private String title;

    /**
     * Constructs the GoodData project from the JSON structure
     *
     * @param obj the JSON structure taken from the GoodData REST API
     */
    public Project(JSONObject obj) {
        super();
        link = obj.getString("link");
        id = obj.getString("identifier");
        title = obj.getString("title");
    }

    /**
     * Returns the project's URI
     *
     * @return the project's URI
     */
    public String getUri() {
        return link;
    }

    /**
     * Returns the project's ID
     *
     * @return the project's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the project's name
     *
     * @return the project's name
     */
    public String getName() {
        return title;
    }

}