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
