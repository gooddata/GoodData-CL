package com.gooddata.util;

import java.io.*;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * File utils
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class FileUtil {

    private static final int BUF_SIZE = 2048;

    /**
     * Compresses local directory to the archiveName
     *
     * @param dirPath     path to the directory
     * @param archiveName the name of the ZIP archive that is going to be created
     * @throws IOException
     */
    public static void compressDir(String dirPath, String archiveName) throws IOException {
        File d = new File(dirPath);
        if (d.isDirectory()) {
            File[] files = d.listFiles();
            byte data[] = new byte[BUF_SIZE];
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(archiveName)));
            for (File file : files) {
                BufferedInputStream fi = new BufferedInputStream(new FileInputStream(file), BUF_SIZE);
                ZipEntry entry = new ZipEntry(file.getName());
                out.putNextEntry(entry);
                int count;
                while ((count = fi.read(data, 0, BUF_SIZE)) != -1) {
                    out.write(data, 0, count);
                }
                fi.close();
            }
            out.close();
            File file = new File(archiveName);
        } else
            throw new IOException("The referenced directory isn't directory!");

    }

    /**
     * Create a new temporary directory. Use something like
     * {@link #recursiveDelete(File)} to clean this directory up since it isn't
     * deleted automatically
     *
     * @return the new directory
     * @throws IOException if there is an error creating the temporary directory
     */
    public static File createTempDir() throws IOException {
        final File sysTempDir = new File(System.getProperty("java.io.tmpdir"));
        File newTempDir;
        final int maxAttempts = 9;
        int attemptCount = 0;
        do {
            attemptCount++;
            if (attemptCount > maxAttempts) {
                throw new IOException(
                        "The highly improbable has occurred! Failed to " +
                                "create a unique temporary directory after " +
                                maxAttempts + " attempts.");
            }
            String dirName = UUID.randomUUID().toString();
            newTempDir = new File(sysTempDir, dirName);
        } while (newTempDir.exists());

        if (newTempDir.mkdirs()) {
            return newTempDir;
        } else {
            throw new IOException(
                    "Failed to create temp dir named " +
                            newTempDir.getAbsolutePath());
        }
    }

    /**
     * Create a new temporary file. Use something like
     *
     * @return the new file
     * @throws IOException if there is an error creating the temporary file
     */
    public static File getTempFile() throws IOException {
        final File sysTempDir = new File(System.getProperty("java.io.tmpdir"));
        File newTempFile;
        final int maxAttempts = 9;
        int attemptCount = 0;
        do {
            attemptCount++;
            if (attemptCount > maxAttempts) {
                throw new IOException(
                        "The highly improbable has occurred! Failed to " +
                                "create a unique temporary directory after " +
                                maxAttempts + " attempts.");
            }
            String fileName = UUID.randomUUID().toString() + ".csv";
            newTempFile = new File(sysTempDir, fileName);
        } while (newTempFile.exists());
        return newTempFile;
    }

    /**
     * Recursively delete file or directory
     *
     * @param fileOrDir the file or dir to delete
     * @return true if all files are successfully deleted
     */
    public static boolean recursiveDelete(File fileOrDir) {
        if (fileOrDir.isDirectory()) {
            // recursively delete contents
            for (File innerFile : fileOrDir.listFiles()) {
                if (!recursiveDelete(innerFile)) {
                    return false;
                }
            }
        }

        return fileOrDir.delete();
    }

    /**
     * Writes a string to a file.
     *
     * @param content  the content
     * @param fileName the file
     * @throws IOException
     */
    public static void writeStringToFile(String content, String fileName) throws IOException {
        FileWriter fw = new FileWriter(fileName);
        fw.write(content);
        fw.flush();
        fw.close();
    }

    /**
     * Reads string from a file
     *
     * @param fileName the file
     * @return the file content as String
     * @throws IOException
     */
    public static String readStringFromFile(String fileName) throws IOException {
        return readStringFromReader(new FileReader(fileName));
    }
    
    public static String readStringFromStream(InputStream is) throws IOException {
    	return readStringFromReader(new InputStreamReader(is));
    }
    
    public static String readStringFromReader(Reader r) throws IOException {
        BufferedReader br = new BufferedReader(r);
        StringBuffer sbr = new StringBuffer();
        for(String ln = br.readLine(); ln != null; ln = br.readLine())
            sbr.append(ln+"\n");
        r.close();
        return sbr.toString();
    }

    /**
     * Appends the CSV header to the existing file
     * @param header header without the trailing \n
     * @param fileName  the CSV file
     * @throws IOException in case of IO issues
     */
    public static void appendCsvHeader(String header, String fileName) throws IOException {
        File tmpFile = getTempFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFile));
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        bw.write(header+"\n");
        for(String ln = br.readLine(); ln != null; ln = br.readLine()) {
            bw.write(ln+"\n");
        }
        br.close();
        bw.flush();
        bw.close();
        File oldFile = new File(fileName);
        oldFile.delete();
        tmpFile.renameTo(oldFile);
    }


}
