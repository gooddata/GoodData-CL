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

import au.com.bytecode.opencsv.CSVWriter;
import com.gooddata.connector.GaConnector;
import com.google.gdata.data.analytics.DataEntry;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.data.analytics.Dimension;
import com.google.gdata.data.analytics.Metric;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Google feed dumper dumps the Google result data to CSV
 *
 * @author ZD <zd@gooddata.com>
 * @version 1.0
 */
public class FeedDumper {

    private static Logger l = Logger.getLogger(FeedDumper.class);
    
    /**
     * Dupmps the gdata feed to CSV
     * @param cw CSVWriter
     * @param feed Google feed
     * @throws IOException in case of an IO problem
     */
    public static int dump(CSVWriter cw, DataFeed feed) throws IOException {
        l.debug("Dumping GA feed.");
        List<DataEntry> entries = feed.getEntries();
        List<Dimension> dimensions = null;
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
        }
        for (Metric metric : metrics) {
            headers.add(metric.getName());
        }

        for (DataEntry entry : entries) {
            final List<String> row = new ArrayList<String>();

            for (String dataName : headers) {
                final String valueIn = entry.stringValueOf(dataName);
                final String valueOut;
                if (GaConnector.GA_DATE.equalsIgnoreCase(dataName)) {
                    valueOut = valueIn.substring(0, 4) + "-"
                            + valueIn.substring(4, 6) + "-"
                            + valueIn.substring(6, 8);
                } else {
                    valueOut = valueIn;
                }
                row.add(valueOut);
            }
            cw.writeNext(row.toArray(new String[]{}));
        }
        l.debug("Dumped "+entries.size()+" rows from GA feed.");
        return entries.size() - 1;   
    }

}