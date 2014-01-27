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

import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class CSVReaderTest {

    @Test
    public void testLoadCrazyFile() throws IOException {
        String[] files = new String[]{"/com/gooddata/util/crazy.csv", "/com/gooddata/util/crazy.csv.win"};
        for (final String f : files) {
            Reader reader = new InputStreamReader(
                    CSVReaderTest.class.getResourceAsStream(f),
                    "utf8");
            CSVReader csv = new CSVReader(reader);

            final int expectedRows = 6;
            int i = 0;
            String[] line = null;
            while ((line = csv.readNext()) != null) {
                i++;
                Assert.assertEquals(f + ":" + i, 4, line.length);
                if (i == expectedRows) {
                    for (Integer j = 1; j <= 4; j++) {
                        Assert.assertEquals(j.toString(), line[j - 1]);
                    }
                }
            }
            Assert.assertEquals(f, expectedRows, i);
        }
    }

    @Test
    public void testEscaping() throws IOException {
        String[] files = new String[]{"/com/gooddata/util/escaping.csv", "/com/gooddata/util/escaping.csv.win"};
        for (final String f : files) {
            Reader reader = new InputStreamReader(
                    CSVReaderTest.class.getResourceAsStream(f),
                    "utf8");

            CSVReader csv = new CSVReader(reader);

            final int expectedRows = 2;
            final int expectedCols = 4;

            int i = 0;
            String[] line = null;
            while ((line = csv.readNext()) != null) {
                i++;
                Assert.assertEquals(f + ":" + i, expectedCols, line.length);
            }
            Assert.assertEquals(f, expectedRows, i);
        }
    }

    @Test
    public void testChunkBoundaryEscape() throws IOException {
        String[] files = new String[]{"/com/gooddata/util/chunk-boundary.csv", "/com/gooddata/util/chunk-boundary.csv.win"};
        for (final String f : files) {
            Reader reader = new InputStreamReader(
                    CSVReaderTest.class.getResourceAsStream(f),
                    "utf8");
            CSVReader csv = new CSVReader(reader);

            final int expectedRows = 58;
            final int expectedCols = 13;
            int i = 0;
            String[] line = null;
            while ((line = csv.readNext()) != null) {
                i++;
                Assert.assertEquals(f + ":" + i, expectedCols, line.length);
            }
            Assert.assertEquals(f, expectedRows, i);
        }
    }

    @Test
    public void testLong() throws IOException {
        String[] files = new String[]{"/com/gooddata/util/long.csv", "/com/gooddata/util/long.csv.win"};
        for (final String f : files) {
            Reader reader = new InputStreamReader(
                    CSVReaderTest.class.getResourceAsStream(f),
                    "utf8");
            CSVReader csv = new CSVReader(reader);

            final int expectedRows = 753;
            final int expectedCols = 10;
            int i = 0;
            String[] line = null;
            while ((line = csv.readNext()) != null) {
                i++;
                Assert.assertEquals(f + ":" + i, expectedCols, line.length);
            }
            Assert.assertEquals(f, expectedRows, i);
        }
    }

    @Test
    public void testNoClosingQuotes() throws IOException {
        Reader reader = new InputStreamReader(
                CSVReaderTest.class.getResourceAsStream("/com/gooddata/util/no-closing-quotes.csv"),
                "utf8");
        CSVReader csv = new CSVReader(reader);
        try {
            while (csv.readNext() != null) {
                ;
            }
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().endsWith(" [2,9]")); // location of unclosed quote
            return; // ok
        }
        Assert.assertFalse("IllegalStateException expected", true);
    }

    @Test
    public void testSpacesInFrontOfQuotes() throws IOException {
        Reader reader = new InputStreamReader(
                CSVReaderTest.class.getResourceAsStream("/com/gooddata/util/spaces-quoted.csv"),
                "utf8");
        CSVReader csv = new CSVReader(reader);

        String[] line = null;
        while ((line = csv.readNext()) != null) {
            Assert.assertEquals(3, line.length);
        }
    }
}
