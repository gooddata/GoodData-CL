package com.gooddata.google.analytics;

import au.com.bytecode.opencsv.CSVWriter;
import com.gooddata.connector.GaConnector;
import com.google.gdata.data.analytics.DataEntry;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.data.analytics.Dimension;
import com.google.gdata.data.analytics.Metric;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FeedDumper {

    
       /**
     * Dupmps the gdata feed to CSV
     * @throws IOException in case of an IO problem
     */
    public static int dump(CSVWriter cw, DataFeed feed) throws IOException {

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
        return entries.size() - 1;   
    }

}