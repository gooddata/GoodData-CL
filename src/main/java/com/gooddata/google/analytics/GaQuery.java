package com.gooddata.google.analytics;

import com.google.gdata.client.analytics.DataQuery;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * GoodData
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class GaQuery extends DataQuery {

    // GA API URL
    private static final String DATA_QUERY_URL = "https://www.google.com/analytics/feeds/data";

    /**
     * Constructor
     * @throws MalformedURLException internal error
     */
    public GaQuery() throws MalformedURLException {
        super(new URL(DATA_QUERY_URL));
    }

}
