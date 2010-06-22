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
 * GoodData Data Loading Interface (DLI)
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class DLI {

    private String id;
    private String name;
    private String link;

    /**
     * Constructs the new DLI
     *
     * @param dli the JSON object from the GoodData REST API
     */
    public DLI(JSONObject dli) {
        super();
        name = dli.getString("title");
        link = dli.getString("link");
        id = dli.getString("identifier");
    }

    /**
     * Returns the DLI's name
     *
     * @return the DLI's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the DLI's URI
     *
     * @return the DLI's URI
     */
    public String getUri() {
        return link;
    }

    /**
     * Returns the DLI's ID
     *
     * @return the DLI's ID
     */
    public String getId() {
        return id;
    }


    /**
     * Returns the DLI manifest that determines how the data are loaded to the GDC platform.
     * The manifest must replace the default manifest in the template.
     *
     * @return the DLI manifest content.
     */
    public String getDLIManifest(List<DLIPart> parts) {
        JSONObject omf = new JSONObject();
        JSONObject oDataSetManifest = new JSONObject();
        JSONArray oParts = new JSONArray();
        for (DLIPart part : parts) {
            JSONObject oPart = new JSONObject();
            oPart.put("populates", part.getLDMObject());
            oPart.put("checkSum", part.getChecksum());
            oPart.put("mode", part.getLoadMode());
            oPart.put("file", part.getFileName());
            oParts.add(oPart);
        }
        oDataSetManifest.put("parts", oParts);
        oDataSetManifest.put("dataSet", id);
        omf.put("dataSetManifest", oDataSetManifest);
        return omf.toString(2);
    }


}
