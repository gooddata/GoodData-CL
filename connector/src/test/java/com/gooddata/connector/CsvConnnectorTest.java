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

package com.gooddata.connector;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;

import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;


public class CsvConnnectorTest extends TestCase {

	public void testGuessSourceSchema() throws IOException {
		
		final InputStream incompleteConfig =  getClass().getResourceAsStream("/com/gooddata/connector/guess_incompleteConfig.xml");
		final InputStream expectedConfig = getClass().getResourceAsStream("/com/gooddata/connector/guess_expectedConfig.xml");
		final URL csvUrl = getClass().getResource("/com/gooddata/connector/guess_quotes.csv");
		
		SourceSchema guessed = CsvConnector.guessSourceSchema(incompleteConfig, csvUrl, null, null);
		SourceSchema expected = SourceSchema.createSchema(expectedConfig);
		
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
	
	public void testSaveConfigTemplateDuplicity() throws IOException {
		final InputStream incompleteConfig =  getClass().getResourceAsStream("/com/gooddata/connector/guess_incompleteConfig.xml");
		final InputStream expectedConfig = getClass().getResourceAsStream("/com/gooddata/connector/guess_expected_dups.xml");
		final URL csvUrl = getClass().getResource("/com/gooddata/connector/guess_dups.csv");
		
		SourceSchema guessed = CsvConnector.guessSourceSchema(incompleteConfig, csvUrl, null, null);
		SourceSchema expected = SourceSchema.createSchema(expectedConfig);
		
		assertEquals(expected.getColumns().size(), guessed.getColumns().size());
		
		for (int i = 0; i < expected.getColumns().size(); i++) {
			SourceColumn expCol = expected.getColumns().get(i);
			SourceColumn gueCol = guessed.getColumns().get(i);
			System.out.println(i);
			assertEquals(expCol.getLdmType(), gueCol.getLdmType());
			assertEquals(expCol.getName(), gueCol.getName());
			// date format guessing not supported yet
			// if (SourceColumn.LDM_TYPE_DATE.equals(expCol.getLdmType())) {
			// 	assertEquals(expCol.getFormat(), gueCol.getFormat());
			// }
		}
	}
}
