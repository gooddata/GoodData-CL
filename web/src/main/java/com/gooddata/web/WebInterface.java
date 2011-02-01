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

package com.gooddata.web;

import com.gooddata.util.FileUtil;
import com.google.gdata.util.common.util.Base64DecoderException;
import net.sf.json.JSONObject;
import com.google.gdata.util.common.util.Base64;

import java.io.*;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.*;
import javax.servlet.*;


/**
 * The GoodData Data Integration WWW processor.
 *
 * @author Zdenek Svoboda <zd@gooddata.com>
 * @version 1.0
 */
public class WebInterface extends HttpServlet {


    static private String path = "/tmp/fblog.log";
    static private FileWriter logger;
    static final String form = "<!DOCTYPE HTML SYSTEM><html><head><title>GoodData Data Synchronization</title></head><body><form method='POST' action=''><table border='0'><tr><td><b>GoodData Username:</b></td><td><input type='text' name='gdc-username' value='john.doe@acme.com'/></td></tr><tr><td><b>Insight Graph API URL:</b></td><td><input type='text' size='80' name='base-url' value='https://graph.facebook.com/175593709144814/insights/page_views/day'/></td></tr><tr><td><b>Create GoodData Project:</b></td><td><input type='checkbox' name='gdc-create-project-flag' checked='1'/></td></tr><tr><td colspan='2'><input type='submit' name='submit-ok' value='OK'/></td></tr></table></form></body></html>";
    static final String result = "<!DOCTYPE HTML SYSTEM><html><head><title>GoodData Data Synchronization Result</title></head><body>Synchronization task submitted. The authentication token is %TOKEN% .</body></html>";


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.print(form);
        out.close();
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        extractToken(request);
        Map parameters = request.getParameterMap();
        if(parameters.containsKey("base-url")) {
            HttpSession session = request.getSession(true);
            String token = (String)session.getAttribute("token");
            PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            out.print(result.replace("%TOKEN%", token));
            out.close();
        }
        else {
            doGet(request, response);
        }
    }

    private void extractToken(HttpServletRequest request) throws IOException {
        String base64 = request.getParameter("signed_request");
        if(base64 != null) {
            String content = base64.split("\\.")[1];
            try {
                String decodedContent = new String(Base64.decodeWebSafe(content));
                JSONObject json = JSONObject.fromObject(decodedContent);
                String token = json.getString("oauth_token");
                HttpSession session = request.getSession(true);
                session.setAttribute("token", token);
            } catch (Base64DecoderException e) {
                throw new IOException(e.getMessage());
            }
        }
    }

}
