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

package com.gooddata.google.analytics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gooddata.connector.AbstractConnector;
import com.gooddata.transform.Transformer;
import com.gooddata.util.DateUtil;
import com.restfb.util.DateUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.gooddata.connector.DateColumnsExtender;
import com.gooddata.connector.GaConnector;
import com.gooddata.exception.InvalidParameterException;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.util.CSVWriter;
import com.google.gdata.data.analytics.DataEntry;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.data.analytics.Dimension;
import com.google.gdata.data.analytics.Metric;

/**
 * Google feed dumper dumps the Google result data to CSV
 *
 * @author ZD <zd@gooddata.com>
 * @version 1.0
 */
public class FeedDumper {

	private static final String UNKNOWN_DATE = "(other)";

    private static final String IN_FMT = "yyyyMMdd";
    private static final String OUT_FMT = "yyyy-MM-dd";

    private static Logger l = Logger.getLogger(FeedDumper.class);

    /**
     * Dupmps the gdata feed to CSV
     * @param cw CSVWriter
     * @param feed Google feed
     * @param gaq Google Analytics Query
     * @param t Transformer
     * @param transform perform transformations?
     * @throws IOException in case of an IO problem
     */
    public static int dump(CSVWriter cw, DataFeed feed, GaQuery gaq, Transformer t, boolean transform) throws IOException {
        l.debug("Dumping GA feed.");
        String profileId = gaq.getIds();
        if(profileId == null || profileId.length() <=0)
            throw new InvalidParameterException("Empty Google Analytics profile ID in query.");
        List<DataEntry> entries = feed.getEntries();
        List<Dimension> dimensions = null;
        List<String> dimensionNames = new ArrayList<String>();
        List<Metric> metrics = null;

        if (!entries.isEmpty()) {
            DataEntry singleEntry = entries.get(0);
            dimensions = singleEntry.getDimensions();
            metrics = singleEntry.getMetrics();
        }
        else
            return 0;

        final List<String> headers = new ArrayList<String>();
        for (Dimension dimension : dimensions) {
            headers.add(dimension.getName());
            dimensionNames.add(dimension.getName());
        }
        for (Metric metric : metrics) {
            headers.add(metric.getName());
        }

        final DateTimeFormatter inFmt = DateUtil.getDateFormatter(IN_FMT, false);
        final DateTimeFormatter outFmt = DateUtil.getDateFormatter(OUT_FMT, false);
        for (DataEntry entry : entries) {
            final List<String> row = new ArrayList<String>();
            for (String dataName : headers) {
                final String valueIn = entry.stringValueOf(dataName);
                String valueOut;
                if (GaConnector.GA_DATE.equalsIgnoreCase(dataName)) {
                	if (valueIn == null || valueIn.length() !=8 || UNKNOWN_DATE.equals(valueIn)) {
                		valueOut = "";
                        l.debug("Invalid date value '"+valueIn+"'");
                	} else {
                        try {
                            DateTime dt = inFmt.parseDateTime(valueIn);
                            valueOut = outFmt.print(dt);
                        }
                        catch(IllegalArgumentException e) {
                            valueOut = "";
                            l.debug("Invalid date value '"+valueIn+"'");
                        }
                	}
                } else {
                    valueOut = valueIn;
                }
                row.add(valueOut);
            }
            row.add(0,profileId);
            String[] r = row.toArray(new String[]{});
            if(transform)
                r = t.transformRow(r, AbstractConnector.DATE_LENGTH_UNRESTRICTED);
            cw.writeNext(r);
        }
        l.debug("Dumped "+entries.size()+" rows from GA feed.");
        return entries.size();
    }

}