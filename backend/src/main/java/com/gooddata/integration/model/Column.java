package com.gooddata.integration.model;

import net.sf.json.JSONObject;

/**
 * GoodData DLI Column
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class Column {

    private String name;
    private String type;
    private String constraints;

    /**
     * Constructs a new DLI column
     *
     * @param column the JSON object from the GoodData REST API
     */
    public Column(JSONObject column) {
        name = column.getString("name");
        type = column.getString("type");
        if (column.containsKey("constraint"))
            constraints = column.getString("constraint");
    }

    /**
     * Returns the column name
     *
     * @return the column name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the column name
     *
     * @param name the column name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the column type
     *
     * @return the column type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the column type
     *
     * @param type the column type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the column constraints
     *
     * @return the column constraints
     */
    public String getConstraints() {
        return constraints;
    }

    /**
     * Sets the column constraints
     *
     * @param constraints the column constraints
     */
    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    /**
     * The standard toString
     *
     * @return the string description of the object
     */
    public String toString() {
        return "name='" + name + "', type='" + type + "', constraints='" + constraints + "'";
    }

}