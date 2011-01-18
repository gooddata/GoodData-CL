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

import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.util.FileUtil;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;


public class FacebookConnnectorTest extends TestCase {

	public void testGuessSourceSchema() throws IOException {

        String[] queries = new String[] {"SELECT uid,name FROM Users", "select uid , name FROM Users",
                "select uid , name from Users", "select uid from Users", "select uid,name,age from Users", "SELECT uid, name FROM user WHERE uid = me() OR uid IN (SELECT uid2 FROM friend WHERE uid1 = me())"};
        int[] cols = new int[] {2,2,2,1,3,2};

        for(int i=0; i<queries.length; i++) {
            File tmpFile = FileUtil.getTempFile();
            FacebookConnector.saveConfigTemplate("TEST", tmpFile.getAbsolutePath(), queries[i], null);
            SourceSchema schema = SourceSchema.createSchema(tmpFile);
            assertEquals(cols[i],schema.getColumns().size());
            FileUtil.recursiveDelete(tmpFile);
        }
		
	}
	
}
