package com.gooddata.util;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
import java.net.URL;
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
     * writes the data from the input stream to the provided output stream
     * @param is
     * @param os
     * @throws IOException
     */
    public static void copy(InputStream is, OutputStream os) throws IOException {
    	if (is == null || os == null) {
    		throw new IllegalArgumentException("both input and output streams must be non-null");
    	}
    	try {
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = is.read(buf)) != -1) {
                os.write(buf, 0, i);
            }
        } finally {
            is.close();
            os.close();
        }
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
     * Strips the CSV header from the existing file
     * Copies the CSV without headers to a new tmp file and returns it.
     * @param file  the CSV file
     * @throws IOException in case of IO issues
     */
    public static File stripCsvHeader(File file) throws IOException {
        File tmpFile = getTempFile();
        StringUtil.normalize(file,tmpFile,1);
        return tmpFile;
    }

    /**
         * Appends the CSV header to the file.
         * Returns new tmp file
         * @param header header without the trailing
         * @param file  the CSV file
         * @throws IOException in case of IO issues
         */
        public static File appendCsvHeader(String[] header, File file) throws IOException {
            File tmpFile = getTempFile();
            CSVReader csvIn  = FileUtil.createUtf8CsvReader(file);
            CSVWriter csvOut = FileUtil.createUtf8CsvWriter(tmpFile);
            csvOut.writeNext(header);
            StringUtil.normalize(csvIn,csvOut,0);
            csvOut.close();
            return tmpFile;
        }

        /**
         * Retrieves CSV headers from an URL
         *
         * @param url CSV url
         * @return the headers as String[]
         * @throws IOException in case of IO issues
         */
        public static String[] getCsvHeader(URL url) throws IOException {
            CSVReader csvIn = new CSVReader(createBufferedUtf8Reader(url));
            return csvIn.readNext();
        }



    /**
     * Constructs a new File and optionally checks if it exists 
     * @param fileName file name
     * @param ignoreMissingFile flag that ignores the fact that the file doesn't exists
     * @return the File
     * @throws IOException if the file doesn't exists and the ignoreMissingFile is false
     */
    public static  File getFile(String fileName, boolean ignoreMissingFile) throws IOException {
        File f = new File(fileName);
        if(!f.exists()) {
        	if (!ignoreMissingFile)
        		throw new IOException("File '" + fileName + "' doesn't exist.");
        	else
        		return null;
        }
        return f;
    }

    /**
     * Constructs a new File and checks if it exists
     * @param fileName file name
     * @return the File
     * @throws IOException if the file doesn't exists
     */
    public static File getFile(String fileName) throws IOException {
        return getFile(fileName, false);
    }

/**
	 * Opens a file given by a path and returns its {@link BufferedReader} using the
	 * UTF-8 encoding
	 *
	 * @param path path to a file to be read
	 * @return UTF8 BufferedReader of the file <tt>path</tt>
	 * @throws IOException
	 */
	public static BufferedReader createBufferedUtf8Reader(String path) throws IOException {
		return createBufferedUtf8Reader(new File(path));
	}

	/**
	 * Opens a file given by a path and returns its {@link BufferedWriter} using the
	 * UTF-8 encoding
	 *
	 * @param path path to a file to write to
	 * @return UTF8 BufferedWriter of the file <tt>path</tt>
	 * @throws IOException
	 */
	public static BufferedWriter createBufferedUtf8Writer(String path) throws IOException {
		return createBufferedUtf8Writer(new File(path));
	}

	/**
	 * Opens a file given by a path and returns its {@link BufferedReader} using the
	 * UTF-8 encoding
	 *
	 * @param file file to be read
	 * @return UTF8 BufferedReader of the <tt>file</tt>
	 * @throws IOException
	 */
	public static BufferedReader createBufferedUtf8Reader(File file) throws IOException {
		return createBufferedUtf8Reader(new FileInputStream(file));
	}

	/**
	 * Opens a file given by a path and returns its {@link BufferedWriter} using the
	 * UTF-8 encoding
	 *
	 * @param file file to write to
	 * @return UTF8 BufferedWriter of the <tt>file</tt>
	 * @throws IOException
	 */
	public static BufferedWriter createBufferedUtf8Writer(File file) throws IOException {
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf8"));
	}

	/**
	 * Opens a URL and returns its {@link BufferedReader} using the UTF-8 encoding
	 *
	 * @param url to be read
	 * @return UTF8 BufferedReader of the <tt>url</tt>
	 * @throws IOException
	 */
	public static BufferedReader createBufferedUtf8Reader(URL url) throws IOException {
		return createBufferedUtf8Reader(url.openStream());
	}

	/**
	 * Creates a {@link BufferedReader} on the top of the given {@link InputStream} using the
	 * UTF-8 encoding
	 *
	 * @param file file to be read
	 * @return UTF8 BufferedReader of the <tt>file</tt>
	 * @throws IOException
	 */
	public static BufferedReader createBufferedUtf8Reader(InputStream is) throws IOException {
		return new BufferedReader(new InputStreamReader(is, "utf8"));
	}

    /**
	 * Creates a UTF-8 {@link CSVReader} of the resource on classpath represented by
	 * given <tt>path</tt>. Calls {@link Class#getResourceAsStream(String)} internally to create
	 * the underlying {@link InputStream}.
	 *
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static CSVReader getResourceAsCsvReader(String path) throws IOException {
		InputStream is = FileUtil.class.getResource(path).openStream();
		return createUtf8CsvReader(is);
	}

	/**
	 * Creates a UTF-8 {@link CSVReader} of the given <tt>file</tt>.
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static CSVReader createUtf8CsvReader(File file) throws IOException {
		return createUtf8CsvReader(new FileInputStream(file));
	}

	/**
	 * Creates a UTF-8 {@link CSVReader} of the given <tt>inputStream</tt>.
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static CSVReader createUtf8CsvReader(InputStream inputStream) throws IOException {
		return new CSVReader(new InputStreamReader(inputStream, "utf8"));
	}

	/**
	 * Creates a UTF-8 {@link CSVWriter} of the given <tt>file</tt>.
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static CSVWriter createUtf8CsvWriter(File file) throws IOException {
		return createUtf8CsvWriter(new FileOutputStream(file));
	}

	/**
	 * Creates a UTF-8 {@link CSVWriter} of the given <tt>outputStream</tt>.
	 *
	 * @param outputStream
	 * @return
	 * @throws IOException
	 */
	public static CSVWriter createUtf8CsvWriter(OutputStream outputStream) throws IOException {
		return new CSVWriter(new OutputStreamWriter(outputStream, "utf8"));
	}

}
