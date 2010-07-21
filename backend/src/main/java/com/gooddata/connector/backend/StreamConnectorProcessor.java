package com.gooddata.connector.backend;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import au.com.bytecode.opencsv.CSVReader;

import com.gooddata.connector.model.PdmColumn;
import com.gooddata.connector.model.PdmSchema;
import com.gooddata.connector.model.PdmTable;
import com.gooddata.naming.N;
import com.gooddata.util.CSVWriter;
import com.gooddata.util.FileUtil;

class StreamConnectorProcessor {
	
	private final PdmSchema schema;
	private final File srcFile;
	private final Map<String,Map<String, Integer>> lookups;
	private final File outDir;

	public StreamConnectorProcessor(PdmSchema schema, File srcFile,	Map<String, Map<String, Integer>> lookups, File outDir) {
		this.schema = schema;
		this.srcFile = srcFile;
		this.lookups = lookups;
		this.outDir = outDir;
	}

	public void process() throws IOException {
		CSVReader srcReader = FileUtil.createUtf8CsvReader(srcFile);
		String outPath = outDir + File.separator + schema.getFactTable().getName(); 
		CSVWriter writer = FileUtil.createUtf8CsvWriter(new File(outPath));
		Map<PdmColumn,String> rec;
		writeOutputHeader(writer, ...)
		while ((rec = toMap(srcReader.readNext())) != null) {
			Map<String, String> outRec = processRecord(rec);
			copyFacts(rec, outRec); // TODO how about primary key
		}
		writer.close();
	}

	private Map<String, String> processRecord(Map<PdmColumn, String> rec) {
		Map<String, String> processed = new HashMap<String, String>();
		for (PdmTable table : schema.getTables()) {
			Map<String,Integer> lookup = getOrCreate(lookups, table.getName());
			List<PdmColumn> srcCols = table.getAssociatedColumns();
			String hash = createHash(rec, srcCols);
			Integer key = lookup.get(key);
			if (key == null) {
				key = updateLookup(lookup, rec);
			}
			processed.put(table.getAssociatedSourceColumn() + "_" + N.ID, key.toString());
		}
		return processed;
	}

	private String createHash(Map<PdmColumn, String> rec, List<PdmColumn> srcCols) {
		boolean first = true;
		StringBuffer result = new StringBuffer();
		for (PdmColumn c : srcCols) {
			if (first) {
				first = false;
			} else {
				result.append(AbstractConnectorBackend.HASH_SEPARATOR);
			}
			if (rec.get(c) == null) {
				throw new IllegalStateException("Missing expected PdmColumn " + c + " in the source record");
			}
			result.append(rec.get(c));
		}
		return result.toString();
	}

	private Map<String, Integer> getOrCreate(Map<String, Map<String, Integer>> lookups, String name) {
		Map<String,Integer> result = lookups.get(name);
		if (result == null) {
			result = new HashMap<String, Integer>();
			lookups.put(name, result);
		}
		return result;
	}

	private Map<PdmColumn, String> toMap(String[] fields) {
		if (fields == null) {
			return null;
		}
		if (schema.getSourceTable().getColumns().size() != fields.length) {
			throw new IllegalStateException("Unexpected number of fields in source data: got #"
					+ fields.length + " but expected #"
					+ schema.getSourceTable().getColumns().size());
		}
		final Map<PdmColumn,String> result = new HashMap<PdmColumn, String>();
		for (int i = 0; i < fields.length; i++) {
			PdmColumn column = schema.getSourceTable().getColumns().get(i);
			result.put(column, fields[i]);
		}
		return result;
	}

}
