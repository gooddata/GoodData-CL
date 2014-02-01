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
    private final boolean skipSpaces;

    public CsvConfiguration(boolean hasHeader, char separator) {
        this(hasHeader, separator, CSVReader.DEFAULT_ESCAPE_CHARACTER);
    }

    public CsvConfiguration(boolean hasHeader, char separator, char escape) {
        this(hasHeader, separator, escape, CSVReader.DEFAULT_QUOTE_CHARACTER);
    }

    public CsvConfiguration(boolean hasHeader, char separator, char escape, char quotechar) {
        this(hasHeader, separator, escape, quotechar, false);
    }

    public CsvConfiguration(boolean hasHeader, char separator, char escape, char quotechar, boolean skipSpaces) {
        this.hasHeader = hasHeader;
        this.separator = separator;
        this.quotechar = quotechar;
        this.escape = escape;
        this.skipSpaces = skipSpaces;
    }

    public CsvConfiguration(CsvConfigurationBuilder builder) {
        this(
                builder.hasHeader,
                builder.separator,
                builder.escape,
                builder.quoteChar,
                builder.skipSpaces
        );
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

    public boolean getSkipSpaces() {
        return skipSpaces;
    }

    @Override
    public String toString() {
        return "CsvConfiguration [hasHeader=" + hasHeader + ", separator=" + separator + ", quotechar=" + quotechar + ", escape=" + escape + ", skipSpaces=" + skipSpaces + "]";
    }

    public static CsvConfigurationBuilder builder() {
        return new CsvConfigurationBuilder();
    }

    public static class CsvConfigurationBuilder {

        private boolean hasHeader = true;
        private char separator = ',';
        private char quoteChar = '"';
        private char escape = '"';
        private boolean skipSpaces = false;

        private CsvConfigurationBuilder() {}

        public CsvConfiguration build() {
            return new CsvConfiguration(this);
        }

        public CsvConfigurationBuilder setHasHeader(boolean hasHeader) {
            this.hasHeader = hasHeader;
            return this;
        }

        public CsvConfigurationBuilder setSeparator(char separator) {
            this.separator = separator;
            return this;
        }

        public CsvConfigurationBuilder setQuoteChar(char quoteChar) {
            this.quoteChar = quoteChar;
            return this;
        }

        public CsvConfigurationBuilder setEscape(char escape) {
            this.escape = escape;
            return this;
        }

        public CsvConfigurationBuilder setSkipSpaces(boolean skipSpaces) {
            this.skipSpaces = skipSpaces;
            return this;
        }

    }


}