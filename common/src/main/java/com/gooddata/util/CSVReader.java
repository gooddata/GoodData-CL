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

package com.gooddata.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple CSV reader written from the scratch to replace Bytecode's CSV reader
 *
 * @author Pavel Kolesnikov
 */
public class CSVReader implements Closeable {

    public static char DEFAULT_SEPARATOR = ',';
    public static char DEFAULT_QUOTE_CHARACTER = '"';
    public static char DEFAULT_ESCAPE_CHARACTER = '"';

    private static int CHUNK_SIZE = 4096;

    private final Reader r;

    // configuration
    private final char separator;
    private final char quote;
    private final char escape;
    private boolean hasCommentSupport = false;
    private char commentChar;

    // status variables
    private final List<String> openRecord = new ArrayList<String>();
    private final StringBuffer openField = new StringBuffer();
    private char lastChar = 0;
    private boolean quotedField = false;
    private int quotedFieldStartRow = 0;
    private int quotedFieldStartCol = 0;
    private boolean wasEscapeOrNotOpeningQuote = false;
    private boolean commentedLine = false;
    private final LinkedList<String[]> recordsQueue = new LinkedList<String[]>();
    private final boolean eof = false;

    private int row = 1;
    private int col = 0;

    /**
     * Constructs CSVReader using a comma for the separator.
     *
     * @param reader the reader to an underlying CSV source.
     */
    public CSVReader(Reader r) {
        this(r, DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * Constructs CSVReader with supplied separator.
     *
     * @param reader    the reader to an underlying CSV source.
     * @param separator the delimiter to use for separating entries.
     */
    public CSVReader(Reader r, char separator) {
        this(r, separator, DEFAULT_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * Constructs CSVReader with supplied separator and quote char.
     *
     * @param reader    the reader to an underlying CSV source.
     * @param separator the delimiter to use for separating entries
     * @param quotechar the character to use for quoted elements
     */
    public CSVReader(Reader r, char separator, char quotechar) {
        this(r, separator, quotechar, DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * Constructs CSVReader with supplied separator and quote char.
     *
     * @param reader    the reader to an underlying CSV source.
     * @param separator the delimiter to use for separating entries
     * @param quotechar the character to use for quoted elements
     * @param escape    the character to use for escaping a separator or quote
     */

    public CSVReader(Reader r, char separator, char quotechar, char escape) {
        this.r = r;
        this.separator = separator;
        this.quote = quotechar;
        this.escape = escape;
    }

    public CSVReader(BufferedReader reader, CsvConfiguration csvConfig) {
        this(reader, csvConfig.getSeparator(), csvConfig.getQuotechar(), csvConfig.getEscape());
    }

    /**
     * Reads the entire file into a List with each element being a String[] of
     * tokens.
     *
     * @return a List of String[], with each String[] representing a line of the
     *         file.
     * @throws IOException if bad things happen during the read
     */
    public List<String[]> readAll() throws IOException {

        List<String[]> allElements = new ArrayList<String[]>();
        String line[];
        while ((line = readNext()) != null) {
            allElements.add(line);
        }
        return allElements;
    }

    /**
     * Reads the next line from the buffer and converts to a string array.
     *
     * @return a string array with each comma-separated element as a separate
     *         entry.
     * @throws IOException if bad things happen during the read
     */
    public String[] readNext() throws IOException {
        while (recordsQueue.isEmpty() && !eof) {
            char[] data = new char[CHUNK_SIZE];
            int size = r.read(data);
            if (size == -1) {
                break;
            }
            processChunk(data, size);
        }
        if (recordsQueue.isEmpty()) {
            if (wasEscapeOrNotOpeningQuote) {
                handlePreviousEscapeOrQuote(null);
            }
            if (quotedField) {
                throw new IllegalStateException("Missing quote character to close the quote char at ["
                        + quotedFieldStartRow + "," + quotedFieldStartCol + "]");
            }
            if (openRecord.isEmpty()) {
                return null;
            } else {
                if (openField.length() > 0) {
                    openRecord.add(openField.toString());
                    openField.delete(0, openField.length());
                }
                String[] result = openRecord.toArray(new String[]{});
                openRecord.clear();
                return result;
            }
        }
        return recordsQueue.removeFirst();
    }

    private void processChunk(final char[] data, final int size) {
        for (int i = 0; i < size; i++) {
            col++;
            final char c = data[i];
            if (wasEscapeOrNotOpeningQuote) {
                handlePreviousEscapeOrQuote(c);
            } else if (c == escape || c == quote) {
                handleEscapeOrQuote(c);
            } else if (c == separator) {
                handleSeparator(c);
            } else if (c == '\n' || c == '\r') {
                handleCrOrLf(c);
            } else if (hasCommentSupport && (c == commentChar)) {
                handleComment(c);
            } else {
                if (commentedLine)
                    break;
                addCharacter(c);
            }
            lastChar = c;
        }
    }

    private void handleCrOrLf(final char c) {
        if (!quotedField && (lastChar == '\r')) {
            return;
        }
        handleEndOfLine(c);
    }

    private void handleComment(final char c) {
        if (commentedLine)
            return;
        if (openRecord.isEmpty() && (openField.length() == 0) && !quotedField) {
            commentedLine = true;
        } else {
            addCharacter(c);
        }
    }

    private void handleEndOfLine(final char c) {
        if (commentedLine) {
            commentedLine = false;
        } else if (quotedField) {
            addCharacter(c);
        } else {
            addField();
            addRecord();
        }
        row++;
        col = 0;
    }

    private void addRecord() {
        recordsQueue.add(openRecord.toArray(new String[]{}));
        openRecord.clear();
        openField.delete(0, openField.length());
        quotedField = false;
    }

    private void handleSeparator(final char c) {
        if (commentedLine)
            return;
        if (quotedField) {
            this.addCharacter(c);
        } else {
            this.addField();
        }
    }

    private void handlePreviousEscapeOrQuote(Character c) {
        boolean wasEscape = false;
        if (lastChar == escape && c != null) {
            if (isEscapableCharacter(c)) {
                addCharacter(c);
                wasEscape = true;
            }
        }

        if (!wasEscape && (lastChar == quote)) {
            if (quotedField) { // closing quote should be followed by separator
                if (c == null || c == '\r' || c == '\n') {
                    quotedField = false;
                    if (c != null) {
                        handleCrOrLf(c);
                    } // c == null is handled after the main loop
                } else if (c == separator) {
                    quotedField = false;
                    handleSeparator(c);
                } else {
                    throw new IllegalStateException(
                            "separator expected after a closing quote; found " + c + getPositionString());
                }
            } else if (openField.length() == 0) {
                startQuotedField();
            } else {
                throw new IllegalStateException("odd quote character at " + getPositionString());
            }
        }

        wasEscapeOrNotOpeningQuote = false;
    }

    private void handleEscapeOrQuote(char c) {
        if (commentedLine)
            return;

        // handle start of a new quoted field
        if (openField.toString().trim().length() == 0 && !quotedField) {
            startQuotedField();
            wasEscapeOrNotOpeningQuote = false;
        } else {
            wasEscapeOrNotOpeningQuote = true;
        }
    }

    private void addField() {
        openRecord.add(openField.toString());
        openField.delete(0, openField.length());
        quotedField = false;
    }

    private void addCharacter(final char c) {
        openField.append(c);
    }

    private boolean isEscapableCharacter(final char c) {
        return (c == escape || c == quote);
    }

    private void startQuotedField() {
        quotedField = true;
        quotedFieldStartRow = row;
        quotedFieldStartCol = col;
    }

    /**
     * Closes the underlying reader.
     *
     * @throws IOException if the close fails
     */
    public void close() throws IOException {
        r.close();
    }

    private String getPositionString() {
        return " [" + row + "," + col + "]";
    }

    public void setCommentChar(char c) {
        hasCommentSupport = true;
        commentChar = c;
    }

    public int getRow() {
        return row;
    }
}
