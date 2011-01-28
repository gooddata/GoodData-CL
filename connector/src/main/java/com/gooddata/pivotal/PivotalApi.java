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

package com.gooddata.pivotal;

import com.gooddata.connector.Constants;
import com.gooddata.exception.GdcLoginException;
import com.gooddata.exception.GdcRestApiException;
import com.gooddata.exception.HttpMethodException;
import com.gooddata.exception.InvalidParameterException;
import com.gooddata.util.CSVReader;
import com.gooddata.util.CSVWriter;
import com.gooddata.util.FileUtil;
import com.gooddata.util.XPathReader;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.HttpCookie;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * GoodData Pivotal API wrapper
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class PivotalApi {

    private static Logger l = Logger.getLogger(PivotalApi.class);

    /**
     * PT username
     */
    private String userName;
    /**
     * PT password
     */
    private String password;

    /**
     * PT project ID (integer)
     */
    private String projectId;

    /**
     * List of column headers that will be included in the STORY dataset
     */
    private Set RECORD_STORIES = new HashSet();


    /**
     * List of DATE column headers (we need to convert dates to the ISO format)
     */
    private Set DATE_COLUMNS = new HashSet();
    /**
     * Labels column header
     */
    private String HEADER_LABEL = "Labels";

    private static String PIVOTAL_URL = "https://www.pivotaltracker.com";

    /**
     * Id column header
     */
    private String HEADER_STORY_ID = "Id";
    /**
     * Shared HTTP client
     */
    private HttpClient client = new HttpClient();

    /**
     * The Pivotal API wrapper constructor
     * @param usr - PT username
     * @param psw - PT password
     * @param prjId  - PT project ID (integer)
     */
    public PivotalApi(String usr, String psw, String prjId) {
        this.setUserName(usr);
        this.setPassword(psw);
        this.setProjectId(prjId);
        client.getHostConfiguration().setHost(PIVOTAL_URL);
        // populate the STORY dataset columns
        RECORD_STORIES.addAll(Arrays.asList(new String[]{"Id", "Labels", "Story", "Iteration", "Iteration Start",
                "Iteration End", "Story Type", "Estimate", "Current State", "Created at", "Accepted at", "Deadline",
                "Requested By", "Owned By", "URL"}));
        DATE_COLUMNS.addAll(Arrays.asList(new String[] {"Iteration Start", "Iteration End", "Created at", "Accepted at",
                "Deadline"}));
    }


    /**
     * Get token
     * @throws Exception in case of an IO error
     */
    public void getToken() throws IOException {
        PostMethod m = new PostMethod("/services/tokens/active");
        m.getParams().setCookiePolicy(CookiePolicy.NETSCAPE);
        m.setParameter("username",getUserName());
        m.setParameter("password",getPassword());
        try {
            client.executeMethod(m);
            System.err.println(m.getResponseBodyAsString());
            if (m.getStatusCode() != HttpStatus.SC_OK && m.getStatusCode() != HttpStatus.SC_MOVED_TEMPORARILY) {
                throw new InvalidParameterException("Invalid PT credentials. HTTP reply code "+m.getStatusCode());
            }
        }
        finally {
            m.releaseConnection();
        }
    }

    private String authCookie = "";

    /**
     * Sign into the PT
     * @throws Exception in case of an IO error
     */
    public void signin() throws IOException {
        PostMethod m = new PostMethod(PIVOTAL_URL+"/signin");
        m.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        m.addParameter("credentials[username]",getUserName());
        m.addParameter("credentials[password]",getPassword());
        try {
            client.executeMethod(m);
            if (m.getStatusCode() != HttpStatus.SC_OK && m.getStatusCode() != HttpStatus.SC_MOVED_TEMPORARILY) {
                throw new InvalidParameterException("Invalid PT credentials. HTTP reply code "+m.getStatusCode());
            }
            Header[] cookies = m.getResponseHeaders("Set-Cookie");
            for(int i=0; i < cookies.length; i++) {
                String value = cookies[i].getValue();
                if(i==0)
                    authCookie += value.split(";")[0];
                else
                    authCookie += "; "+value.split(";")[0];
            }
            Header l = m.getResponseHeader("Location");
            String location = l.getValue();
            GetMethod gm = new GetMethod(location);
            gm.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
            gm.setRequestHeader("Cookie",authCookie);
            client.executeMethod(gm);
            if (gm.getStatusCode() != HttpStatus.SC_OK && gm.getStatusCode() != HttpStatus.SC_MOVED_TEMPORARILY) {
                throw new InvalidParameterException("Invalid PT credentials. HTTP reply code "+m.getStatusCode());
            }
        }
        finally {
            m.releaseConnection();
        }
    }

    /**
     * Retrieves the PT data in the CSV format
     * @param ptCsv - the filename to store the PT CSV data
     * @throws Exception in case of an IO error
     */
    public void getCsvData(String ptCsv) throws IOException {
        String url = PIVOTAL_URL+"/projects/"+getProjectId()+"/export/";
        PostMethod m = new PostMethod(url);
      
        m.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        m.setRequestHeader("Cookie",authCookie);
        m.addParameter("options[include_current_backlog_stories]","1");
        m.addParameter("options[include_icebox_stories]","1");
        m.addParameter("options[include_done_stories]","1");

        try {
            client.executeMethod(m);
            if (m.getStatusCode() == HttpStatus.SC_OK) {
                final int BUFLEN = 2048;
                byte[] buf = new byte[BUFLEN];
                BufferedInputStream is = new BufferedInputStream(m.getResponseBodyAsStream());
                BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(ptCsv));
                int cnt = is.read(buf,0,BUFLEN);
                while(cnt >0) {
                    os.write(buf, 0, cnt);
                    cnt = is.read(buf,0,BUFLEN);
                }
                is.close();
                os.flush();
                os.close();
            }
            else {
                throw new InvalidParameterException("Error retrieving the PT data. HTTP reply code "+m.getStatusCode());
            }
        }
        finally {
            m.releaseConnection();
        }
    }

    /**
     * Writes a record to CSV writer
     * @param cw CSV writer
     * @param rec record as a list
     */
    private void writeRecord(CSVWriter cw, List<String> rec) {
        cw.writeNext(rec.toArray(new String[] {}));
    }

    private DateTimeFormatter reader = DateTimeFormat.forPattern("MMM dd, yyyy");
    private DateTimeFormatter writer = DateTimeFormat.forPattern("yyyy-MM-dd");

    /**
     * Converts the date format (if needed)
     * @param header the CSV column header
     * @param value the value
     * @return the converted date
     */
    private String convertDate(String header, String value) {
        if(DATE_COLUMNS.contains(header)) {
            if(value != null && value.length()>0) {
                DateTime dt = null;
                try {
                    dt = reader.parseDateTime(value);
                } catch (IllegalArgumentException e) {
                    l.debug("Error parsing PT date value '"+value+"'");
                }
                return writer.print(dt);
            }
            else {
                return "";
            }
        }
        else
            return value;
    }


    private final DateTimeFormatter baseFmt = DateTimeFormat.forPattern(Constants.DEFAULT_DATE_FMT_STRING);
    private final DateTime base = baseFmt.parseDateTime("1900-01-03");


    /**
     * Computes the iteration velocity
     * @param csvFile the incoming PT CSV file
     * @param velocityIterationCount the number of iterations that the velocity is computed from
     * @param velocities returned velocities
     * @param releaseInfo releases
     * @throws Exception in case of an IO issue
     */
    public void computeIterationVelocity(String csvFile, int velocityIterationCount,
                                         Map<Integer,Double> velocities, Map<String,String> releaseInfo) throws IOException {
        Map<Integer,Integer> estimates = new HashMap<Integer,Integer>();
        CSVReader cr = FileUtil.createUtf8CsvReader(new File(csvFile));
        String[] header = cr.readNext();
        if(header != null && header.length>0) {
            List<String> headerList = Arrays.asList(header);

            int iterIdx = headerList.indexOf("Iteration");
            int estIdx = headerList.indexOf("Estimate");
            int idIdx = headerList.indexOf("Id");
            int typeIdx = headerList.indexOf("Story Type");
            int nameIdx = headerList.indexOf("Story");
            if(iterIdx >=0 && estIdx>=0 && idIdx>=0 && typeIdx>=0 && nameIdx>=0) {
                List<String> releaseBuffer = new ArrayList<String>();
                String[] row = cr.readNext();
                while(row != null && row.length > 0) {
                    try {
                        String id = row[idIdx];
                        String type = row[typeIdx];
                        String name = row[nameIdx];
                        if(id != null && id.length()>0) {
                            if(type != null && type.length()>0) {
                                if("release".equalsIgnoreCase(type)) {
                                    if(name != null && name.length()>0) {
                                        for(String story : releaseBuffer) {
                                            releaseInfo.put(story,name);
                                        }
                                        releaseInfo.put(id,name);
                                        releaseBuffer.clear();
                                    }
                                    else {
                                        cr.close();
                                        throw new IOException("Release with no name.");
                                    }
                                }
                                else {
                                    releaseBuffer.add(id);
                                }
                            }
                        }
                        else {
                            cr.close();
                            throw new IOException("Story with empty Id.");
                        }
                        String iterTxt = row[iterIdx];
                        String estTxt = row[estIdx];
                        if(iterTxt != null && iterTxt.length()>0) {
                            Integer iter = Integer.parseInt(iterTxt);
                            Integer est = new Integer(0);
                            if(estTxt!=null && estTxt.length()>0)
                                est = Integer.parseInt(estTxt);
                            if(estimates.containsKey(iter))  {
                                Integer estimate = estimates.get(iter);
                                estimate = new Integer(estimate.intValue() + est.intValue());
                                estimates.put(iter,estimate);
                            }
                            else {
                                estimates.put(iter,new Integer(est));
                            }
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException e) {
                        cr.close();
                        throw new IOException("Iteration velocity computation failed: data row doesn't contain Id or Type or Iteration or Estimate fields.");
                    }
                    row = cr.readNext();
                }
                cr.close();
                for (Integer iteration : estimates.keySet()) {
                    int cnt = 0;
                    int estimate = 0;
                    for(int i = 1; i <= velocityIterationCount; i++) {
                        if(estimates.containsKey(new Integer(iteration.intValue() - i))) {
                            cnt++;
                            estimate += estimates.get(new Integer(iteration.intValue() - i));
                        }
                    }
                    double velocity = 0;
                    if(cnt>0)
                        velocity = (double)estimate / (double)cnt;
                    velocities.put(iteration, new Double(velocity));

                }
            }
            else {
                cr.close();
                throw new IOException("Iteration velocity computation failed: no Iteration or Estimate or Id or Type fields in header.");
            }
        }
        else {
            cr.close();
            throw new IOException("Iteration velocity computation failed: empty PT input.");
        }

    }


    /**
     * Parses the PT CSV file into the STORY, LABEL, and LABEL_TO_STORY CSV files
     * @param csvFile the incoming PT CSV file
     * @param storiesCsv the output STORY CSV file
     * @param labelsCsv the output LABEL CSV file
     * @param labelsToStoriesCsv  the output LABEL_TO_STORY CSV file
     * @param velocityIterationCount the number of iterations that the velocity is computed from
     * @throws Exception in case of an IO issue
     */
    public void parse(String csvFile, String storiesCsv, String labelsCsv, String labelsToStoriesCsv, DateTime t, int velocityIterationCount) throws IOException {
        String today = writer.print(t);
        Map<Integer,Double> velocities = new HashMap<Integer,Double>();
        Map<String,String> releases = new HashMap<String,String>();
        computeIterationVelocity(csvFile, velocityIterationCount, velocities, releases);
        CSVReader cr = FileUtil.createUtf8CsvReader(new File(csvFile));
        String[] row = cr.readNext();
        if(row != null && row.length > 0) {
            List<String> headers = Arrays.asList(row);
            List<String> storiesRecord = new ArrayList<String>();
            List<String> labelsRecord = new ArrayList<String>();
            List<String> labelsToStoriesRecord = new ArrayList<String>();

            CSVWriter storiesWriter = new CSVWriter(new FileWriter(storiesCsv));
            CSVWriter labelsWriter = new CSVWriter(new FileWriter(labelsCsv));
            CSVWriter labelsToStoriesWriter = new CSVWriter(new FileWriter(labelsToStoriesCsv));

            labelsRecord.add("cpId");
            labelsRecord.add("Label Id");
            labelsRecord.add("Label");
            labelsToStoriesRecord.add("cpId");
            labelsToStoriesRecord.add("Story Id");
            labelsToStoriesRecord.add("Label Id");


            for(String header : headers) {
                if(RECORD_STORIES.contains(header)) {
                    storiesRecord.add(header);
                    if(header.equalsIgnoreCase("Iteration")) {
                        storiesRecord.add("IterationFact");
                        storiesRecord.add("IterationVelocity");
                    }
                    if(header.equalsIgnoreCase("Id")) {
                        storiesRecord.add("Release");
                    }
                }
            }
            storiesRecord.add(0, "SnapshotDate");
            storiesRecord.add(0, "cpId");
            writeRecord(storiesWriter, storiesRecord);
            writeRecord(labelsWriter, labelsRecord);
            writeRecord(labelsToStoriesWriter, labelsToStoriesRecord);

            Map<String,String> labels = new HashMap<String, String>();
            row = cr.readNext();
            while(row != null && row.length > 1) {
                storiesRecord.clear();
                labelsRecord.clear();
                labelsToStoriesRecord.clear();
                String storyId = "";
                String label = "";
                String key = today+"|";
                for(int i=0; i < headers.size(); i++) {
                    String header = headers.get(i);
                    if(RECORD_STORIES.contains(header)) {
                        key += row[i] + "|";
                        storiesRecord.add(convertDate(header, row[i]));
                        if(header.equalsIgnoreCase("Id")) {
                            String id = row[i];
                            String release = "";
                            if(releases.containsKey(id)) {
                                release = releases.get(id);
                            }
                            storiesRecord.add(release);
                        }
                        if(header.equalsIgnoreCase("Iteration")) {
                            storiesRecord.add(convertDate(header, row[i]));
                            if(row[i] != null && row[i].length()>0) {
                                Integer iteration = Integer.parseInt(row[i]);
                                storiesRecord.add(convertDate(header, velocities.get(iteration).toString()));
                            }
                            else {
                                storiesRecord.add(convertDate(header, "0"));
                            }
                        }
                    }
                    if(HEADER_LABEL.equals(header)) {
                        label = row[i];
                    }
                }
                storyId = DigestUtils.md5Hex(key);
                storiesRecord.add(0, today);
                storiesRecord.add(0, storyId);
                String[] lbls = label.split(",");
                for(String lbl : lbls) {
                    lbl = lbl.trim();
                    if(lbl.length() > 0) {
                        if(labels.containsKey(lbl)) {
                            String lblId = labels.get(lbl);
                            labelsToStoriesRecord.add(storyId);
                            labelsToStoriesRecord.add(lblId);
                            labelsToStoriesRecord.add(0, DigestUtils.md5Hex(storyId + "|" + lblId));
                            writeRecord(labelsToStoriesWriter, labelsToStoriesRecord);
                        }
                        else {
                            String id = DigestUtils.md5Hex(lbl);
                            labels.put(lbl, id);
                            labelsRecord.add(lbl);
                            labelsRecord.add(0, id);
                            labelsToStoriesRecord.add(storyId);
                            labelsToStoriesRecord.add(id);
                            writeRecord(labelsWriter, labelsRecord);
                            labelsToStoriesRecord.add(0, DigestUtils.md5Hex(storyId + "|" + id));
                            writeRecord(labelsToStoriesWriter, labelsToStoriesRecord);
                        }
                    }
                    labelsRecord.clear();
                    labelsToStoriesRecord.clear();                   
                }
                writeRecord(storiesWriter, storiesRecord);
                row = cr.readNext();
            }
            storiesWriter.flush();
            storiesWriter.close();
            labelsWriter.flush();
            labelsWriter.close();
            labelsToStoriesWriter.flush();
            labelsToStoriesWriter.close();
        }
        else {
            throw new InvalidParameterException("The Pivotal extract doesn't contain any row.");
        }



    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

}
