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
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.util.Enumeration;
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


      public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        if(logger == null) {
            logger = new FileWriter(path);
        }
        logger.write("START REQUEST\n\n");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("<head>");
        out.println("<title>Request Information Example</title>");
        logger.write("Request Information Example\n\n");
        out.println("</head>");
        out.println("<body>");
        out.println("<br/><h1>Request Information</h1>");
        logger.write("Request Information\n");
        out.println("<br/>Method: " + request.getMethod());
        out.println("<br/>Request URI: " + request.getRequestURI());
        out.println("<br/>Protocol: " + request.getProtocol());
        out.println("<br/>PathInfo: " + request.getPathInfo());
        out.println("<br/>Remote Address: " + request.getRemoteAddr());
        logger.write("Method: " + request.getMethod() + "\n");
        logger.write("Request URI: " + request.getRequestURI()+"\n");
        logger.write("Protocol: " + request.getProtocol()+"\n");
        logger.write("PathInfo: " + request.getPathInfo()+"\n");
        logger.write("Remote Address: " + request.getRemoteAddr()+"\n\n");

        out.println("<br/><h1>Headers</h1>");
        logger.write("Headers+\n");
        Enumeration e = request.getHeaderNames();
        while (e.hasMoreElements()) {
            String name = (String)e.nextElement();
            String value = request.getHeader(name);
            out.println("<br/>"+name + " = " + value);
            logger.write(name + " = " + value+"\n");
        }
        out.println("<br/><h1>Parameters</h1>");
        Enumeration p = request.getParameterNames();
        while (p.hasMoreElements()) {
            String name = (String)p.nextElement();
            String value = request.getParameter(name);
            out.println("<br/>"+name + " = " + value);
            logger.write(name + " = " + value+"\n");
        }

        String base64 = request.getParameter("signed_request");
        if(base64 != null) {
            String json = new String(Base64.decodeBase64(base64.getBytes()));
            logger.write("JSON: " + json+"\n");
        }


        logger.write("END REQUEST\n\n");
        out.println("</body>");
        out.println("</html>");
        logger.flush();
    }

    /**
     * We are going to perform the same operations for POST requests
     * as for GET methods, so this method just sends the request to
     * the doGet method.
     */

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        doGet(request, response);
    }

}
