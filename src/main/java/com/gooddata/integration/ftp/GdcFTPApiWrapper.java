package com.gooddata.integration.ftp;

import com.gooddata.exception.GdcUploadErrorException;
import com.gooddata.integration.rest.configuration.NamePasswordConfiguration;
import com.gooddata.util.FileUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

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
                                        throw new GdcUploadErrorException("Can't change the file's name: server="
                                                + config.getGdcHost() + ", file=" + file.getName() + ", " + clientReply(client));
                                    }
                                }
                                else
                                    throw new GdcUploadErrorException("Can't copy file to the FTP: server="
                                            + config.getGdcHost() + ", file=" + file.getName()  + ", " + clientReply(client));
                            }
                            else
                            throw new GdcUploadErrorException("Can't set the BINARY file transfer: server="
                                    + config.getGdcHost()  + ", " + clientReply(client));
                        }
                        else
                            throw new GdcUploadErrorException("Can't cd to the '"+dir+"' directory: server="
                                    + config.getGdcHost()  + ", " + clientReply(client));
                    } else
                        throw new GdcUploadErrorException("Can't create the '"+dir+"' directory: server="
                                + config.getGdcHost()  + ", " + clientReply(client));
                    client.logout();
                } else
                    throw new GdcUploadErrorException("Can't FTP login: server=" + config.getGdcHost()
                            + ", username=" + config.getUsername()  + ", " + clientReply(client));
            } else throw new GdcUploadErrorException("Can't FTP connect: server=" + config.getGdcHost()  + ", " + clientReply(client));
        } finally {
            if (client.isConnected()) {
                try {
                    client.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        }
    }

    /**
     * GET the transfer logs from the FTP server
     * @param remoteDir the primary transfer directory that contains the logs
     * @return Map with the log name and content
     * @throws IOException in case of IO issues
     */
    public Map<String, String> getTransferLogs(String remoteDir) throws IOException {
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
                                        throw new GdcUploadErrorException("Can't retrieve log file: server="
                                            + config.getGdcHost() + ", file=" + file + ", " + clientReply(client));
                                    }
                                    result.put(file,new String(logContent.toByteArray()));                                    
                                }
                            }
                        }
                        else
                        throw new GdcUploadErrorException("Can't set the ASCII file transfer: server="
                                + config.getGdcHost()  + ", " + clientReply(client));
                    }
                    else
                        throw new GdcUploadErrorException("Can't cd to the '"+remoteDir+"' directory: server="
                                + config.getGdcHost()  + ", " + clientReply(client));
                    client.logout();
                } else
                    throw new GdcUploadErrorException("Can't FTP login: server=" + config.getGdcHost()
                            + ", username=" + config.getUsername()  + ", " + clientReply(client));
            } else throw new GdcUploadErrorException("Can't FTP connect: server=" + config.getGdcHost()  + ", " + clientReply(client));
        } finally {
            if (client.isConnected()) {
                try {
                    client.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        }
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
