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

package com.gooddata.integration.rest;

import com.gooddata.integration.rest.configuration.NamePasswordConfiguration;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * GoodData
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class TestGdcRestApiWrapper extends TestCase {

    private static Logger l = Logger.getLogger(TestGdcRestApiWrapper.class);

    NamePasswordConfiguration config = null;

    //NamePasswordConfiguration config = new NamePasswordConfiguration("https","js-rrrrd-devel.getgooddata.com","bear@gooddata.com","xxx");

    String[] checkUrls = {
            "/gdc/md/FoodMartDemo/obj/130003800",
            "/gdc/md/FoodMartDemo/obj/130001937",
            "/gdc/md/FoodMartDemo/obj/130002154",
            "/gdc/md/FoodMartDemo/obj/130002361",
            "/gdc/md/FoodMartDemo/obj/130002376",
            "/gdc/md/FoodMartDemo/obj/130002297",
            "/gdc/md/FoodMartDemo/obj/130002162",
            "/gdc/md/FoodMartDemo/obj/130002654",
            "/gdc/md/FoodMartDemo/obj/130002587",
            "/gdc/md/FoodMartDemo/obj/130002121",
            "/gdc/md/FoodMartDemo/obj/130002004",
            "/gdc/md/FoodMartDemo/obj/130002614",
            "/gdc/md/FoodMartDemo/obj/130002675",
            "/gdc/md/FoodMartDemo/obj/130002632",
            "/gdc/md/FoodMartDemo/obj/130002292",
            "/gdc/md/FoodMartDemo/obj/130002187",
            "/gdc/md/FoodMartDemo/obj/130002355",
            "/gdc/md/FoodMartDemo/obj/130001832",
            "/gdc/md/FoodMartDemo/obj/130002891",
            "/gdc/md/FoodMartDemo/obj/130002151",
            "/gdc/md/FoodMartDemo/obj/130002449",
            "/gdc/md/FoodMartDemo/obj/130001911",
            "/gdc/md/FoodMartDemo/obj/130001982",
            "/gdc/md/FoodMartDemo/obj/130002111",
            "/gdc/md/FoodMartDemo/obj/130002371",
            "/gdc/md/FoodMartDemo/obj/130002310",
            "/gdc/md/FoodMartDemo/obj/130002317",
            "/gdc/md/FoodMartDemo/obj/130002188",
            "/gdc/md/FoodMartDemo/obj/130002303",
            "/gdc/md/FoodMartDemo/obj/130002024",
            "/gdc/md/FoodMartDemo/obj/130002469",
            "/gdc/md/FoodMartDemo/obj/130001892",
            "/gdc/md/FoodMartDemo/obj/130002087",
            "/gdc/md/FoodMartDemo/obj/130002505",
            "/gdc/md/FoodMartDemo/obj/130001874",
            "/gdc/md/FoodMartDemo/obj/130001844",
            "/gdc/md/FoodMartDemo/obj/130002356",
            "/gdc/md/FoodMartDemo/obj/130001858",
            "/gdc/md/FoodMartDemo/obj/130002366",
            "/gdc/md/FoodMartDemo/obj/130001967",
            "/gdc/md/FoodMartDemo/obj/130002225",
            "/gdc/md/FoodMartDemo/obj/130002209",
            "/gdc/md/FoodMartDemo/obj/130002431",
            "/gdc/md/FoodMartDemo/obj/130001900",
            "/gdc/md/FoodMartDemo/obj/130002324",
            "/gdc/md/FoodMartDemo/obj/130002567",
            "/gdc/md/FoodMartDemo/obj/130002868",
            "/gdc/md/FoodMartDemo/obj/130002036",
            "/gdc/md/FoodMartDemo/obj/130002195"
    };

    public void testComputeMetric() throws Exception {
        try {
            if (config != null) {
                GdcRESTApiWrapper rest = new GdcRESTApiWrapper(config);
                rest.login();
                for (String url : checkUrls) {
                    String value = rest.getReportDefinition(url);
                    System.out.println("Result "+value);
                }
                rest.logout();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void testSplitMaqlSmall(){
   	GdcRESTApiWrapper rest = new GdcRESTApiWrapper(config);
   	String input = "SYNCHRONIZE {dataset.shippingorderitemcsv};\n\n" +
			"# add dummy lines\n" +
			"# add dummy lines";
   	List<String> splitMAQL = rest.splitMAQL(input);
   	Assert.assertEquals(1, splitMAQL.size());
   	Assert.assertEquals(input, splitMAQL.get(0));
   }

    
    public void testSplitMaqlLarge() throws IOException{
	GdcRESTApiWrapper rest = new GdcRESTApiWrapper(config);
	InputStream inputFileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream( "large.maql" );
	String maqlString = IOUtils.toString(inputFileStream);
	List<String> splitMAQL = rest.splitMAQL(maqlString);
	l.error(splitMAQL);
	
	Assert.assertEquals(4, splitMAQL.size());
	InputStream outputFileStreamPart1 = Thread.currentThread().getContextClassLoader().getResourceAsStream( "large.maql.1" );
	InputStream outputFileStreamPart2 = Thread.currentThread().getContextClassLoader().getResourceAsStream( "large.maql.2" );
	InputStream outputFileStreamPart3 = Thread.currentThread().getContextClassLoader().getResourceAsStream( "large.maql.3" );
	InputStream outputFileStreamPart4 = Thread.currentThread().getContextClassLoader().getResourceAsStream( "large.maql.4" );
	
	Assert.assertEquals( IOUtils.toString(outputFileStreamPart1),splitMAQL.get(0));
	Assert.assertEquals( IOUtils.toString(outputFileStreamPart2),splitMAQL.get(1));
	Assert.assertEquals( IOUtils.toString(outputFileStreamPart3),splitMAQL.get(2));
	Assert.assertEquals( IOUtils.toString(outputFileStreamPart4),splitMAQL.get(3));
    }

}
