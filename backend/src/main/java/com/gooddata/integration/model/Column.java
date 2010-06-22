/*
 * Copyright (c) 2009 GoodData Corporation.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Redistributions in any form must be accompanied by information on
 *    how to obtain complete source code for this software and any
 *    accompanying software that uses this software.  The source code
 *    must either be included in the distribution or be available for no
 *    more than the cost of distribution plus a nominal fee, and must be
 *    freely redistributable under reasonable conditions.  For an
 *    executable file, complete source code means the source code for all
 *    modules it contains.  It does not include source code for modules or
 *    files that typically accompany the major components of the operating
 *    system on which the executable file runs.
 *
 * THIS SOFTWARE IS PROVIDED BY GOODDATA ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT, ARE DISCLAIMED.  IN NO EVENT SHALL ORACLE BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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