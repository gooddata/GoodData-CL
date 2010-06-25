package com.gooddata.connector;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;


public class CsvConnnectorTest extends TestCase {

	public void testGuessSourceSchema() throws IOException {
		
		final String incompleteConfig =  getClass().getResource("/com/gooddata/connector/guess_incompleteConfig.xml").getPath();
		final String expectedConfig = getClass().getResource("/com/gooddata/connector/guess_expectedConfig.xml").getPath();
		final String csvFile = getClass().getResource("/com/gooddata/connector/guess_quotes.csv").getPath(); 
		
		SourceSchema guessed = CsvConnector.guessSourceSchema(incompleteConfig, csvFile, null, null);
		SourceSchema pokus = SourceSchema.createSchema(new File(incompleteConfig));
		SourceSchema expected = SourceSchema.createSchema(new File(expectedConfig));
		
		assertEquals(expected.getColumns().size(), guessed.getColumns().size());
		
		for (int i = 0; i < expected.getColumns().size(); i++) {
			SourceColumn expCol = expected.getColumns().get(i);
			SourceColumn gueCol = guessed.getColumns().get(i);
			assertEquals(expCol.getLdmType(), gueCol.getLdmType());
			// date format guessing not supported yet
			// if (SourceColumn.LDM_TYPE_DATE.equals(expCol.getLdmType())) {
			// 	assertEquals(expCol.getFormat(), gueCol.getFormat());
			// }
		}
	}
}
