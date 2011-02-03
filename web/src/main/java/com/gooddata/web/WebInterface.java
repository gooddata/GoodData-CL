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

import com.gooddata.connector.Constants;
import com.gooddata.util.FileUtil;
import com.google.gdata.util.common.util.Base64DecoderException;
import net.sf.json.JSONObject;
import com.google.gdata.util.common.util.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;


/**
 * The GoodData Data Integration WWW processor.
 *
 * @author Zdenek Svoboda <zd@gooddata.com>
 * @version 1.0
 */
public class WebInterface extends HttpServlet {

    static private final String CREATE_TEMPLATE = "/mnt/projects/facebook/cmd/create.data.model.txt";
    static private final String LOAD_TEMPLATE = "/mnt/projects/facebook/cmd/load.data.txt";
    static private final String IN_QUEUE = "/mnt/projects/facebook/in_queue";
    static private String logPath = "/tmp/fblog.log";
    static private FileWriter logger;
    static final String form = "<!DOCTYPE HTML SYSTEM><html><head><title>GoodData Data Synchronization</title></head>" +
            "<body><form method='POST' action=''><table border='0'>" +
            "<tr><td><b>GoodData Username:</b></td><td><input type='text' name='EMAIL' value='john.doe@acme.com'/></td></tr>" +
            "<tr><td><b>Insight Graph API URL:</b></td><td><input type='text' size='80' name='BASE_URL' value='https://graph.facebook.com/23528966907/insights/page_views/day'/></td></tr>" +
            "<tr><td><b>Start Date (YYYY-MM-DD):</b></td><td><input type='text' name='START_DATE' value='%START_DATE_INITIAL_VALUE%'/></td></tr>" +
            "<tr><td><b>End Date (YYYY-MM-DD):</b></td><td><input type='text' name='END_DATE' value='%END_DATE_INITIAL_VALUE%'/></td></tr>" +
            "<tr><td><b>GoodData Project ID:</b></td><td><input type='text' name='GDC_PROJECT_HASH' value=''/></td></tr>" +
            "<tr><td><b>Create New GoodData Project:</b></td><td><input type='checkbox' name='CREATE_PROJECT_FLAG' checked='1'/></td></tr>" +
            "<tr><td colspan='2'>" +
            "<input type='hidden' name='TOKEN' value='%TOKEN%'/>" +
            "<input type='submit' name='SUBMIT_OK' value='OK'/></td></tr>" +
            "</table></form>" +
            "<br/><br/>" +
            "You need to grant this app the Facebook read_insights permission first. <a target='_blank' href='https://www.facebook.com/dialog/oauth?client_id=175593709144814&redirect_uri=https://zd.users.getgooddata.com/gdc/facebook/login/&scope=read_insights'>Click here to grant the permission</a>." +
            "<br/><br/>" +
            "<b>Facebook Graph API Token:</b> %TOKEN%" +
            "<br/><br/>" +
            "<b>Preview URL</b>: https://graph.facebook.com/23528966907/insights/page_views/day?since=%START_DATE_INITIAL_VALUE_UNIX%&until=%END_DATE_INITIAL_VALUE_UNIX%&access_token=%TOKEN%" +
            "<br/><br/>" +
            "<ul>" +
            "<li><b>GoodData Username</b> is your GoodData username (your e-mail). Go to the <a href='https://secure.gooddata.com'>GoodData platform main page</a> to create your account.</li>" +
            "<li><b>Insights Graph API URL</b> is the Facebook Graph API URL, that contains the data for a specific object in format 'object_id/metric/period'. " +
            "See the <a href='http://developers.facebook.com/docs/reference/api/insights/'>Facebook documentation</a> for more details.</li>" +
            "<li><b>Start Date</b> is the date when you want to retrieve the Facebook metrics from.</li>" +
            "<li><b>End Date</b> is the date when you want to retrieve the Facebook metrics till. Please note that Facebook supports at most 30 days periods.</li>" +
            "<li><b>GoodData Project</b> is the GoodData project hash (e.g. ca6a1r1lbfwpt2v05k36nbc0cjpu7lh9). If the 'Create New GoodData Project' is checked, this is ignored.</li>" +
            "<li><b>Create New GoodData Project</b> creates a new GoodData project. Once the project is ready, you'll receive the invitation e-mail.</li>" +
            "</ul>" +
            "</body></html>";
    static final String result = "<!DOCTYPE HTML SYSTEM><html><head><title>GoodData Data Synchronization Result</title></head>" +
            "<body>%MSG%</body></html>";

    private static final DateTimeFormatter baseFmt = DateTimeFormat.forPattern(Constants.DEFAULT_DATE_FMT_STRING);

    private static Set<String> acceptedParams = new HashSet<String>();


    static {
        String[] p = {"TOKEN", "BASE_URL", "START_DATE", "END_DATE", "EMAIL", "GDC_PROJECT_HASH"};
        acceptedParams.addAll(Arrays.asList(p));
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        setupLogger();
        dumpRequest(request);
        String token = extractToken(request);
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        String t = form;
        DateTime today = new DateTime();
        DateTime start = today.minusDays(30);
        t = t.replace("%START_DATE_INITIAL_VALUE%", baseFmt.print(start));
        t = t.replace("%END_DATE_INITIAL_VALUE%",baseFmt.print(today));
        t = t.replace("%START_DATE_INITIAL_VALUE_UNIX%", Long.toString(start.getMillis()/1000));
        t = t.replace("%END_DATE_INITIAL_VALUE_UNIX%",Long.toString(today.getMillis()/1000));
        if(token != null) {
            t = t.replaceAll("%TOKEN%",token);
            out.print(t);
        }
        else {
            t = t.replaceAll("%TOKEN%","");
            out.print(t);
        }
        out.close();
    }

    private String get(Map p, String n) {
        String[] a = (String[])p.get(n);
        if(a != null && a.length > 0) {
            return a[0];
        }
        return null;
    }

    private void process(Map parameters) throws IOException {
        String createProjectFlag = get(parameters,"CREATE_PROJECT_FLAG");
        String templateContent = "";
        String fileName = "";
        if(createProjectFlag != null && (createProjectFlag.equalsIgnoreCase("on") || createProjectFlag.equalsIgnoreCase("true") ||
            createProjectFlag.equalsIgnoreCase("1"))) {
            debug("CREATE template selected.");
            templateContent = FileUtil.readStringFromFile(CREATE_TEMPLATE);
            fileName = "create.project.%ID%.txt";
        }
        else {
            debug("LOAD template selected.");
            templateContent = FileUtil.readStringFromFile(LOAD_TEMPLATE);
            fileName = "load.data.%ID%.txt";
        }
        for(String key: acceptedParams) {
            String value = get(parameters,key);
            if(value != null && value.length()>0) {
                templateContent = templateContent.replace("%"+key+"%",value);
            }
            else {
                if(createProjectFlag != null && (createProjectFlag.equalsIgnoreCase("on") || createProjectFlag.equalsIgnoreCase("true") ||
            createProjectFlag.equalsIgnoreCase("1")) && "GDC_PROJECT_HASH".equalsIgnoreCase(key)) {
                    // ok
                }
                else {
                    if("TOKEN".equalsIgnoreCase(key)) {
                        debug("OAuth token not generated. Please make sure you have granted this app the read_insights Facebook permission.");
                        throw new IOException("OAuth token not generated. Please make sure you have granted this app the read_insights Facebook permission.");
                    }
                    else {
                        debug("Parameter "+key+" not supplied.");
                        throw new IOException("Parameter "+key+" not supplied.");
                    }
                }
            }
        }
        String value = get(parameters,"EMAIL");
        if(value != null && value.length()>0) {
            String id = DigestUtils.md5Hex(value);
            templateContent = templateContent.replace("%ID%",id);
            fileName = fileName.replace("%ID%",id);
            FileUtil.writeStringToFile(templateContent, IN_QUEUE+System.getProperty("file.separator")+fileName);
        }
        else {
            debug("Parameter EMAIL not supplied.");
            throw new IOException("Parameter EMAIL not supplied.");
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        setupLogger();
        dumpRequest(request);
        Map parameters = request.getParameterMap();
        if(parameters.containsKey("SUBMIT_OK")) {
            String token = extractToken(request);
            debug("POST: retrieving token="+token);
            PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            try {
                process(parameters);
                if(token != null && token.length()>0) {
                    out.print(result.replace("%MSG%","Synchronization task submitted."));
                }
                else {
                    out.print(result.replace("%MSG%","Authorization required."));
                }
            }
            catch (IOException e) {
                out.print(result.replace("%MSG%","Error encountered. Error message: " + e.getMessage()));
            }
            finally {
                out.close();
            }
        }
        else {
            doGet(request, response);
        }
    }

    private void setupLogger() throws IOException {
        if(logger == null) {
            logger = new FileWriter(logPath);
        }
    }

    private void debug(String msg) throws IOException {
        if(logger != null) {
            logger.write(msg);
            logger.write('\n');
        }
        logger.flush();
    }

    private void dumpRequest(HttpServletRequest request) throws IOException {
        String method = request.getMethod();
        debug("Method: "+method);
        Enumeration headerNames = request.getHeaderNames();
        debug("Headers");
        while(headerNames.hasMoreElements()) {
            String n = (String)headerNames.nextElement();
            String v = request.getHeader(n);
            debug(n+":"+v);
        }

        Enumeration parameterNames = request.getParameterNames();
        debug("Parameters");
        while(parameterNames.hasMoreElements()) {
            String n = (String)parameterNames.nextElement();
            String v = request.getParameter(n);
            debug(n+":"+v);
        }
    }

    private String extractToken(HttpServletRequest request) throws IOException {
        String token = null;
        token = request.getParameter("TOKEN");
        String base64 = request.getParameter("signed_request");
        if(base64 != null) {
            String content = base64.split("\\.")[1];
            try {
                String decodedContent = new String(Base64.decodeWebSafe(content));
                JSONObject json = JSONObject.fromObject(decodedContent);
                if(json.containsKey("oauth_token"))
                    token = json.getString("oauth_token");
                debug("Extracting token: token="+token);
            } catch (Base64DecoderException e) {
                throw new IOException(e.getMessage());
            }
        }
        return token;
    }

}
