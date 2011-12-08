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
package com.gooddata.filter;

import com.gooddata.exception.InternalErrorException;
import com.gooddata.exception.InvalidParameterException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filters the duplicate messages.
 *
 * @author zd@gooddata.com
 * @version 1.0
 */
public class DuplicateMessageFilter implements MessageFilter {

    private static Logger l = Logger.getLogger(DuplicateMessageFilter.class);

    private Map<String, Long> digestDB = new HashMap<String, Long>();
    private String dbLocation;
    private final static String DB_FILE_NAME = ".gdn.msg.db";

    /**
     * Constructor
     *
     * @param dbLoc - message db location
     */
    protected DuplicateMessageFilter(String dbLoc) {
        try {
            dbLocation = dbLoc;
            File db = new File(dbLocation);
            if (!db.exists()) {
                createDB();
            } else {
                load();
            }
        } catch (IOException e) {
            l.debug("Can't construct the duplicate message filter.", e);
            throw new InvalidParameterException("Can't construct the duplicate message filter.", e);
        } catch (ClassNotFoundException e) {
            l.debug("Can't construct the duplicate message filter.", e);
            throw new InvalidParameterException("Can't construct the duplicate message filter.", e);
        }
    }

    /**
     * Returns the period in seconds from strings like 12d, 3h, 30m etc.
     *
     * @param filteringCondition the duration string
     * @return the period length in seconds
     */
    private long getPeriod(String filteringCondition) {
        filteringCondition = filteringCondition.trim();
        Pattern rgxp = Pattern.compile("[0-9]+[w|d|h|m]");
        Matcher m = rgxp.matcher(filteringCondition);
        if (m.find()) {
            String c = m.group();
            char p = c.charAt(c.length() - 1);
            long d = 0;
            switch (p) {
                case 'w':
                    d = 7 * 24 * 60 * 60;
                    break;
                case 'd':
                    d = 24 * 60 * 60;
                    break;
                case 'h':
                    d = 60 * 60;
                    break;
                case 'm':
                    d = 60;
                    break;
                default:
                    l.error("Invalid filter condition " + filteringCondition);
                    throw new InvalidParameterException("Invalid filter condition " + filteringCondition);
            }
            String amt = c.substring(0, c.length() - 1);
            long n = Long.parseLong(amt);
            return n * d * 1000;
        } else {
            l.error("Invalid filter condition " + filteringCondition);
            throw new InvalidParameterException("Invalid filter condition " + filteringCondition);
        }
    }

    /**
     * Filter creator
     *
     * @return new MessageFilter
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static MessageFilter createFilter() {
        final String[] dirs = new String[]{"user.dir", "user.home"};
        for (final String d : dirs) {
            String path = System.getProperty(d) + File.separator + DB_FILE_NAME;
            return new DuplicateMessageFilter(path);
        }
        String path = System.getProperty("user.dir") + File.separator + DB_FILE_NAME;
        return new DuplicateMessageFilter(path);
    }

    /**
     * Returns the filter decision
     *
     * @param message - the message content
     * @return
     */
    public boolean filter(String message, String filterCondition) {
        Date dt = new Date();
        long currentTime = dt.getTime();
        String digest = DigestUtils.md5Hex(message);
        Long lastTime = digestDB.get(digest);
        if (lastTime == null)
            return true;
        long per = currentTime - lastTime.longValue();
        if (per > getPeriod(filterCondition))
            return true;
        else
            return false;
    }

    /**
     * Updates the DB with new message
     *
     * @param message - the message content
     * @return
     */
    public void update(String message) {
        Date dt = new Date();
        long currentTime = dt.getTime();
        String digest = DigestUtils.md5Hex(message);
        digestDB.put(digest, new Long(currentTime));
    }

    private void createDB() throws IOException {
        File db = new File(dbLocation);
        ObjectOutputStream w = new ObjectOutputStream(new FileOutputStream(dbLocation));
        w.writeObject(digestDB);
        w.flush();
        w.close();
    }

    public void save() throws IOException {
        File db = new File(dbLocation);
        if (db.exists() && db.canWrite()) {
            ObjectOutputStream w = new ObjectOutputStream(new FileOutputStream(dbLocation));
            w.writeObject(digestDB);
            w.flush();
            w.close();
        } else {
            l.debug("Can't write the notification DB at " + dbLocation);
            throw new InternalErrorException("Can't write the notification DB at " + dbLocation);
        }
        l.debug("Saved the notification DB at " + dbLocation);
    }

    private void load() throws IOException, ClassNotFoundException {
        File db = new File(dbLocation);
        if (db.exists() && db.canRead()) {
            ObjectInputStream r = new ObjectInputStream(new FileInputStream(db));
            digestDB = (Map<String, Long>) r.readObject();
        } else {
            l.debug("Can't read the notification DB at " + dbLocation);
            throw new InternalErrorException("Can't read the notification DB at " + dbLocation);
        }
        l.debug("Using the notification DB at " + dbLocation);
    }

}
