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
import junit.framework.TestCase;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

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

    //NamePasswordConfiguration config = new NamePasswordConfiguration("https","secure.gooddata.com","zd@gooddata.com","xxx");

    String[] checkUrls = {
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/4170",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/12238",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/5479",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/13344",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/7261",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/12218",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/1788",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/13928",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/4148",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/8067",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/16843",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/3392",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/4192",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/4499",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/13772",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/18799",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/10730",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/6833",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/12467",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/7456",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/13785",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/4228",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/13876",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/3329",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/6826",
            "/gdc/md/bd410owmaf3lks1qxtecwjpls340ckg5/obj/5138"
    };

    public void testComputeMetric() throws Exception {
        try {
            if(config != null) {
                GdcRESTApiWrapper rest = new GdcRESTApiWrapper(config);
                rest.login();
                for(String url : checkUrls) {
                    List<JSONObject> deps =  rest.usedBy(url);
                    for(JSONObject d : deps) {
                        if("report".equalsIgnoreCase(d.getString("category"))) {
                            System.out.println(d.getString("link") + " : "+d.getString("title"));
                        }
                    }

                }
                rest.logout();
            }
        }
        catch(Exception e) {
           e.printStackTrace();
        }
    }


}
