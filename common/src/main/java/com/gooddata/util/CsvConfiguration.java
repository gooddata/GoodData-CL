/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.gooddata.util;

/**
 * Encapsulates CSV configuration.
 */
public class CsvConfiguration {
    private final boolean hasHeader;
    private final char separator;
    private final char quotechar;
    private final char escape;

    public CsvConfiguration(boolean hasHeader, char separator) {
        this(hasHeader, separator, CSVReader.DEFAULT_ESCAPE_CHARACTER);
    }

    public CsvConfiguration(boolean hasHeader, char separator, char escape) {
        this(hasHeader, separator, escape, CSVReader.DEFAULT_QUOTE_CHARACTER);
    }

    public CsvConfiguration(boolean hasHeader, char separator, char escape, char quotechar) {
        this.hasHeader = hasHeader;
        this.separator = separator;
        this.quotechar = quotechar;
        this.escape = escape;
    }

    public boolean hasHeader() {
        return hasHeader;
    }

    public char getSeparator() {
        return separator;
    }

    public char getQuotechar() {
        return quotechar;
    }

    public char getEscape() {
        return escape;
    }

    @Override
    public String toString() {
        return "CsvConfiguration [hasHeader=" + hasHeader + ", separator=" + separator + ", quotechar=" + quotechar + ", escape=" + escape + "]";
    }


}