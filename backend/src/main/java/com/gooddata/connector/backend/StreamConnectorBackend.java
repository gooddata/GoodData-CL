package com.gooddata.connector.backend;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import au.com.bytecode.opencsv.CSVReader;

import com.gooddata.connector.model.PdmColumn;
import com.gooddata.connector.model.PdmSchema;
import com.gooddata.connector.model.PdmTable;
import com.gooddata.exception.ConnectorBackendException;
import com.gooddata.exception.InvalidParameterException;
import com.gooddata.integration.model.DLIPart;
import com.gooddata.util.CSVWriter;
import com.gooddata.util.FileUtil;
import com.gooddata.util.StringUtil;

/**
 * 
 * @author Pavel Kolesnikov <pavel@gooddata.com>
 */
public class StreamConnectorBackend extends AbstractConnectorBackend {
	
	private static final String LOOKUPS_DIR_NAME = "lookups";
	private static final String OUTPUT_DIR_NAME = "out";
	
	private final File rootDir;
	private final File lookupsDir;
	
	/**
	 * don't access directly, call {@link #getOrCreateSnapshotDir(PdmSchema)} instead!
	 */
	private File snapshotDir = null;
	
	public StreamConnectorBackend() {
		this(null);
	}
	
	public StreamConnectorBackend(String path) {
		if (path == null) {
			path = System.getProperty("user.home");
		}
		if (path == null) {
			throw new InvalidParameterException("Cannot locate user's home directory, "
					+ "please provide a backend folder explicitely using the 'directory' parameter");
		}

		File dir = new File(path);
		if (!dir.exists()) {
			dir = createDir(dir);
		} else {
			if (!dir.canWrite()) {
				throw new InvalidParameterException("Directory '" + path + "' is not writeable");
			}
		}
		this.rootDir = dir;
		File lookupsDir = new File(path + File.separator + LOOKUPS_DIR_NAME);
		if (!lookupsDir.exists()) {
			lookupsDir = createDir(lookupsDir);
		}
		this.lookupsDir = lookupsDir;
	}
	
	public static StreamConnectorBackend create(String dir) {
		return new StreamConnectorBackend(dir);
	}

	/**
     * {@inheritDoc}
     */
	protected void createSnowflake(PdmSchema schema)
			throws ConnectorBackendException {
		
		final Map<String, Map<String, Integer>> lookups = loadLookups(schema);
		File snapshotDir = getOrCreateSnapshotDir(schema);
		File srcFile = getExtractedSourceFile(schema);
		try {
			StreamConnectorProcessor processor = new StreamConnectorProcessor(schema, srcFile, lookups, snapshotDir);
			processor.process();
			// dumpLookups(lookups);
		} catch (IOException e) {
			throw new ConnectorBackendException(e);
		}
	}

	private void process(File src,	Map<String, Map<String, Integer>> lookups, File snapshotDir) {
		// TODO Auto-generated method stub
		
	}

	public void dropSnapshots() {
		// TODO Auto-generated method stub

	}

	protected void executeExtract(PdmSchema schema, String absolutePath, boolean hasHeader) {
		CSVReader srcReader = FileUtil.createUtf8CsvReader(new File(absolutePath));
		CSVWriter writer = FileUtil.createUtf8CsvWriter(getExtractedSourceFile(schema));
		
		PdmTable sourceTable = schema.getSourceTable();
        String cols = getNonAutoincrementColumns(sourceTable);

		String[] header = new String[schema.getSourceTable().getColumns().size()];
		int hi = 0;
		for (final PdmColumn c : schema.getSourceTable().getColumns()) {
			header[hi++] = c.getName();
		}
		writer.writeNext(header);
		
        String[] line;
		int lastMaxId = getLastMaxId();
		
		while ((line = srcReader.readNext()) != null) {
			String[] out = new String[line.length + 1];
			out[0] = ++lastMaxId;
			for (int i = 0; i < line.length; i++) {
				out[i + 1] = line[i];
			}
			writer.writeNext(out);
		}
		
		try {
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			throw new ConnectorBackendException(e);
		}
		
	}

	protected void executeLoad(PdmSchema pdm, DLIPart p, String dir,
			int[] snapshotIds) {
		// TODO Auto-generated method stub

	}

	protected boolean exists(String tbl) {
		// TODO Auto-generated method stub
		return false;
	}

	public int getLastSnapshotId() {
		// TODO Auto-generated method stub
		return 0;
	}

	protected void initializeLocalDataSet(PdmSchema schema)
			throws ConnectorBackendException {
		
		File dsDir = getDataSetDir(schema);
		if (!dsDir.mkdir()) {
			throw new ConnectorBackendException("Cannot create a dataset directory for the " + schema.getName() + " dataset");
		}
		File lookupDir = getLookupsDir(schema);
		if (!lookupDir.mkdir()) {
			throw new ConnectorBackendException("Cannot create a lookup directory for the " + schema.getName() + " dataset");
		}
	}

	protected void initializeLocalProject() throws ConnectorBackendException {
		File pDir = getProjectDir();
		if (!pDir.mkdir()) {
			throw new ConnectorBackendException("Cannot create a project dir for the " + getProjectId() + " project");
		}
	}
	
	public void close() { /* no resources to release */	}

	public String listSnapshots() {
		// TODO Auto-generated method stub
		return null;
	}

	private static File createDir(File dir) {
		final File parent = dir.getParentFile();
		if (!parent.exists()) {
			throw new InvalidParameterException("Cannot create directory '" + dir.getAbsolutePath()
					+ "': parent directory does not exist");
		}
		if (!parent.canWrite()) {
			throw new InvalidParameterException("Cannot create directory '" + dir.getAbsolutePath()
					+ "': parent directory is not writeable");
		}
		if (!dir.mkdir()) {
			throw new InvalidParameterException("Cannot create directory '" + dir.getAbsolutePath()
					+ "' due to an unknown reason");
		}
		return dir;
	}

	private Map<String, Map<String,Integer>> loadLookups(PdmSchema schema) {
		final Map<String, Map<String,Integer>> lookups = new HashMap<String, Map<String,Integer>>();
		final File lookupsDir = getLookupsDir(schema);
		for (final PdmTable table : schema.getTables()) {
			;
		}
		return lookups;
	}
	
	private File getProjectDir() {
		String path = new StringBuffer(rootDir.getAbsolutePath())
			.append(File.separator)
			.append(getProjectId()).toString();
		return new File(path);
	}

	private File getDataSetDir(PdmSchema schema) {
		String path = new StringBuffer(getProjectDir().getAbsolutePath())
			.append(File.separator)
			.append(StringUtil.formatShortName(schema.getName())).toString();
		return new File(path);
	}
	
	private File getLookupsDir(PdmSchema schema) {
		String path = new StringBuffer(getDataSetDir(schema).getAbsolutePath())
			.append(File.separator)
			.append(LOOKUPS_DIR_NAME).toString();
		return new File(path);
	}
	
	private File getOrCreateSnapshotDir(PdmSchema schema) {
		if (snapshotDir == null) {
			String path = new StringBuffer(getDataSetDir(schema).getAbsolutePath())
				.append(File.separator)
				.append(new Date().getTime()).toString();
			File snapshotDir = new File(path);
			if (!snapshotDir.mkdir()) {
				throw new ConnectorBackendException("Cannot create the snapshot directory '" + snapshotDir.getAbsolutePath() + "'");
			}
		}
		return snapshotDir;
	}

	private File getExtractedSourceFile(PdmSchema pdm) {
		File snapshotDir = getOrCreateSnapshotDir(pdm);
		return new File(snapshotDir + File.separator + pdm.getName());
	}
}
