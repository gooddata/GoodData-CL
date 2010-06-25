package com.gooddata.connector;

import java.io.IOException;

import junit.framework.TestCase;

import com.gooddata.connector.CsvConnector;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;


public class CsvConnnectorTest extends TestCase {

	public void testGuessSourceSchema() throws IOException {
		
		/*
		 * TODO: Maven friendly file locations

		final String incompleteConfig = "com/gooddata/connector/guess_incompleteConfig.xml";
		final String expectedConfig = "com/gooddata/connector/guess_expectedConfig.xml";
		final String csvFile = "com/gooddata/connector/guess_quotes.csv"; 
		
		SourceSchema guessed = CsvConnector.guessSourceSchema(incompleteConfig, csvFile, null, null);
		SourceSchema expected = SourceSchema.createSchema(expectedConfig);
		
		assertEquals(expected.getColumns().size(), guessed.getColumns().size());
		
		for (int i = 0; i < expected.getColumns().size(); i++) {
			SourceColumn expCol = expected.getColumns().get(i);
			SourceColumn gueCol = guessed.getColumns().get(i);
			assertEquals(expCol.getLdmType(), gueCol.getLdmType());
			if (SourceColumn.LDM_TYPE_DATE.equals(expCol.getLdmType())) {
				assertEquals(expCol.getFormat(), gueCol.getFormat());
			}
		}
		
		 */
	}
}
