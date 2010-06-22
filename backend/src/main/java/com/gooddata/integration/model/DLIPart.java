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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * GoodData Data Loading Interface part (DLI)
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class DLIPart {

    /**
     * Full load mode. All the existing date will be replaced.
     */
    public static final String LM_FULL = "FULL";

    /**
     * Incremental load mode. All the existing date will be preserved. The new data are going to be appended.
     */
    public static final String LM_INCREMENTAL = "INCREMENTAL";

    private String file;
    private String populates;
    private String checksum;
    private List<Column> columns;
    private String loadMode = LM_FULL;

    /**
     * Constructs a new DLI part
     *
     * @param part the JSON object from the GoodData REST API
     */
    public DLIPart(JSONObject part) {
        super();
        this.file = part.getString("file");
        this.populates = part.getString("populates");
        this.checksum = part.getString("checkSum");
        JSONArray cls = part.getJSONArray("columns");
        if (cls != null) {
            columns = new ArrayList<Column>();
            for (Object oc : cls) {
                columns.add(new Column((JSONObject) oc));
            }
        }
    }

    /**
     * Returns the part's file name
     *
     * @return the part's file name
     */
    public String getFileName() {
        return file;
    }

    /**
     * Returns the populated LDM object (attribute/fact)
     *
     * @return the populated LDM object (attribute/fact)
     */
    public String getLDMObject() {
        return populates;
    }

    /**
     * Returns the part's checksum
     *
     * @return the part's checksum
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * Returns the part's columns in a Map
     *
     * @return the part's columns in a Map
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * Returns the load mode (LM_FULL | LM_INCREMENTAL)
     *
     * @return the load mode (LM_FULL | LM_INCREMENTAL)
     */
    public String getLoadMode() {
        return loadMode;
    }

    /**
     * Sets the load mode (LM_FULL | LM_INCREMENTAL)
     *
     * @param loadMode the load mode (LM_FULL | LM_INCREMENTAL)
     */
    public void setLoadMode(String loadMode) {
        this.loadMode = loadMode;
    }

    /**
     * The standard toString
     *
     * @return the string description of the object
     */
    public String toString() {
        return "file='" + file + "', populates='" + populates + "', checksum='" + checksum + "', load mode='" + loadMode + "'";
    }
    

}