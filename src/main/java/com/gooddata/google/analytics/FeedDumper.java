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

    

    private final CSVWriter writer;
    private final List<DataEntry> entries;
    private DataEntry singleEntry = null;
    private List<Dimension> dimensions = null;
    private List<Metric> metrics = null;

    /**
     * Dupmps the gdata feed to CSV
     * @throws IOException in case of an IO problem
     */
    public void dump() throws IOException {

        if (entries.isEmpty()) {
            return;
        }

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
            writer.writeNext(row.toArray(new String[]{}));
        }
        writer.close();
    }


    /**
     * Dumps a gdata feed to CSV
     * @param feed gdata feed
     * @param os CSV OutputStream
     */
    public FeedDumper(final DataFeed feed, final OutputStream os) {
        this.writer = new CSVWriter(new OutputStreamWriter(os));
        entries = feed.getEntries();
        if (!entries.isEmpty()) {
            singleEntry = entries.get(0);
            dimensions = singleEntry.getDimensions();
            metrics = singleEntry.getMetrics();
        }
    }

}