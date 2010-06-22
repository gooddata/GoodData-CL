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

package com.gooddata.integration.ftp;

import com.gooddata.exception.GdcUploadErrorException;
import com.gooddata.integration.rest.configuration.NamePasswordConfiguration;
import com.gooddata.util.FileUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

/**
 * GoodData FTP API Java wrapper
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class GdcFTPApiWrapper {

    private static Logger l = Logger.getLogger(GdcFTPApiWrapper.class);

    protected static final String DEFAULT_ARCHIVE_NAME = "upload.zip";

    protected FTPClient client;
    protected NamePasswordConfiguration config;

    /**
     * Constructs the GoodData FTP API Java wrapper
     *
     * @param config NamePasswordConfiguration object with the GDC name and password configuration
     */
    public GdcFTPApiWrapper(NamePasswordConfiguration config) {
        this.config = config;
        client = new FTPClient();
    }


    /**
     * FTP transfers a local directory to the remote GDC FTP server
     * @param archiveName the name of the ZIP archive that is going to be transferred
     * @throws IOException in case of IO issues
     */
    public void transferDir(String archiveName) throws IOException {
        l.debug("Transfering archive "+archiveName);
        try {
            File file = new File(archiveName);
            String dir = file.getName().split("\\.")[0];
            client.connect(config.getGdcHost());
            if (FTPReply.isPositiveCompletion(client.getReplyCode())) {
                client.login(config.getUsername(), config.getPassword());
                if (FTPReply.isPositiveCompletion(client.getReplyCode())) {
                    client.makeDirectory(dir);
                    if (FTPReply.isPositiveCompletion(client.getReplyCode())) {
                        client.changeWorkingDirectory(dir);
                        if (FTPReply.isPositiveCompletion(client.getReplyCode())) {
                            client.setFileType(FTPClient.BINARY_FILE_TYPE);
                            if (FTPReply.isPositiveCompletion(client.getReplyCode())) {
                                client.enterLocalPassiveMode();
                                client.storeFile(file.getName(), new FileInputStream(file));
                                if (FTPReply.isPositiveCompletion(client.getReplyCode())) {
                                    client.rename(file.getName(),DEFAULT_ARCHIVE_NAME);
                                    if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
                                        l.debug("Can't change the file's name: server="
                                                + config.getGdcHost() + ", file=" + file.getName() + ", " + clientReply(client));
                                        throw new GdcUploadErrorException("Can't change the file's name: server="
                                                + config.getGdcHost() + ", file=" + file.getName() + ", " + clientReply(client));
                                    }
                                }
                                else {
                                    l.debug("Can't copy file to the FTP: server="
                                            + config.getGdcHost() + ", file=" + file.getName()  + ", " + clientReply(client));
                                    throw new GdcUploadErrorException("Can't copy file to the FTP: server="
                                            + config.getGdcHost() + ", file=" + file.getName()  + ", " + clientReply(client));
                                }
                            }
                            else {
                                l.debug("Can't set the BINARY file transfer: server="
                                    + config.getGdcHost()  + ", " + clientReply(client));
                                throw new GdcUploadErrorException("Can't set the BINARY file transfer: server="
                                    + config.getGdcHost()  + ", " + clientReply(client));
                            }
                        }
                        else {
                            l.debug("Can't cd to the '"+dir+"' directory: server="
                                    + config.getGdcHost()  + ", " + clientReply(client));
                            throw new GdcUploadErrorException("Can't cd to the '"+dir+"' directory: server="
                                    + config.getGdcHost()  + ", " + clientReply(client));
                        }
                    } else {
                        l.debug("Can't create the '"+dir+"' directory: server="
                                + config.getGdcHost()  + ", " + clientReply(client));
                        throw new GdcUploadErrorException("Can't create the '"+dir+"' directory: server="
                                + config.getGdcHost()  + ", " + clientReply(client));
                    }
                    client.logout();
                } else {
                    l.debug("Can't FTP login: server=" + config.getGdcHost()
                            + ", username=" + config.getUsername()  + ", " + clientReply(client));
                    throw new GdcUploadErrorException("Can't FTP login: server=" + config.getGdcHost()
                            + ", username=" + config.getUsername()  + ", " + clientReply(client));
                }
            } else {
                l.debug("Can't FTP connect: server=" + config.getGdcHost()  + ", " + clientReply(client));
                throw new GdcUploadErrorException("Can't FTP connect: server=" + config.getGdcHost()  + ", " + clientReply(client));
            }
        } finally {
            if (client.isConnected()) {
                try {
                    client.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        }
        l.debug("Transferred archive "+archiveName);
    }

    /**
     * GET the transfer logs from the FTP server
     * @param remoteDir the primary transfer directory that contains the logs
     * @return Map with the log name and content
     * @throws IOException in case of IO issues
     */
    public Map<String, String> getTransferLogs(String remoteDir) throws IOException {
        l.debug("Retrieveing transfer logs.");
        Map<String,String> result = new HashMap<String,String>();
        try {
            client.connect(config.getGdcHost());
            if (FTPReply.isPositiveCompletion(client.getReplyCode())) {
                client.login(config.getUsername(), config.getPassword());
                if (FTPReply.isPositiveCompletion(client.getReplyCode())) {
                    client.changeWorkingDirectory(remoteDir);
                    if (FTPReply.isPositiveCompletion(client.getReplyCode())) {
                        client.setFileType(FTPClient.ASCII_FILE_TYPE);
                        if (FTPReply.isPositiveCompletion(client.getReplyCode())) {
                            client.enterLocalPassiveMode();
                            String[] files = client.listNames();
                            for(String file : files) {
                                if(file.endsWith(".log")) {
                                    ByteArrayOutputStream logContent = new ByteArrayOutputStream();
                                    InputStream in = client.retrieveFileStream(file);
                                    FileUtil.copy(in, logContent);
                                    boolean st = client.completePendingCommand();
                                    if (!st || !FTPReply.isPositiveCompletion(client.getReplyCode())) {
                                        l.debug("Can't retrieve log file: server="
                                            + config.getGdcHost() + ", file=" + file + ", " + clientReply(client));
                                        throw new GdcUploadErrorException("Can't retrieve log file: server="
                                            + config.getGdcHost() + ", file=" + file + ", " + clientReply(client));
                                    }
                                    result.put(file,new String(logContent.toByteArray()));                                    
                                }
                            }
                        }
                        else {
                            l.debug("Can't set the ASCII file transfer: server="
                                + config.getGdcHost()  + ", " + clientReply(client));
                            throw new GdcUploadErrorException("Can't set the ASCII file transfer: server="
                                + config.getGdcHost()  + ", " + clientReply(client));
                        }
                    }
                    else {
                        l.debug("Can't cd to the '"+remoteDir+"' directory: server="
                                + config.getGdcHost()  + ", " + clientReply(client));
                        throw new GdcUploadErrorException("Can't cd to the '"+remoteDir+"' directory: server="
                                + config.getGdcHost()  + ", " + clientReply(client));
                    }
                    client.logout();
                } else {
                    l.debug("Can't FTP login: server=" + config.getGdcHost()
                            + ", username=" + config.getUsername()  + ", " + clientReply(client));
                    throw new GdcUploadErrorException("Can't FTP login: server=" + config.getGdcHost()
                            + ", username=" + config.getUsername()  + ", " + clientReply(client));
                }
            } else {
                l.debug("Can't FTP connect: server=" + config.getGdcHost()  + ", " + clientReply(client));
                throw new GdcUploadErrorException("Can't FTP connect: server=" + config.getGdcHost()  + ", " + clientReply(client));
            }
        } finally {
            if (client.isConnected()) {
                try {
                    client.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        }
        l.debug("Transfer logs retrieved.");
        return result;
    }

    /**
     * gets client reply
     * @param client ftp client
     * @return client reply
     */
    private String clientReply(FTPClient client) {
    	return client.getReplyString() + " (code: " + client.getReplyCode() + ")";
    }
}
