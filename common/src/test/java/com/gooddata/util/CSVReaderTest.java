package com.gooddata.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Test;

import junit.framework.Assert;

public class CSVReaderTest {

	@Test
	public void testLoadCrazyFile() throws IOException {
		String[] files = new String[] { "/com/gooddata/util/crazy.csv", "/com/gooddata/util/crazy.csv.win" };
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
		String[] files = new String[] { "/com/gooddata/util/escaping.csv", "/com/gooddata/util/escaping.csv.win" };
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
    	String[] files = new String[] { "/com/gooddata/util/chunk-boundary.csv", "/com/gooddata/util/chunk-boundary.csv.win" };
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
    	String[] files = new String[] { "/com/gooddata/util/long.csv", "/com/gooddata/util/long.csv.win" };
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
}
