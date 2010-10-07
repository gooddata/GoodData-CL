package com.gooddata.connector;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gooddata.integration.model.Column;
import com.gooddata.integration.model.SLI;
import com.gooddata.integration.rest.GdcRESTApiWrapper;
import com.gooddata.integration.rest.configuration.NamePasswordConfiguration;
import com.gooddata.modeling.generator.MaqlGenerator;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.naming.N;
import com.gooddata.util.StringUtil;

public class TestFindColumnChanges {

	/**
	 * Testing class to generate an ALTER MAQL script and print it on stdout.
	 * Run without params to see the usage message.
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if (args.length < 5 || args.length > 6) {
			throw new IllegalArgumentException("Usage: java TestFindColumnChanges <login> <password> <pid> <dataset> <schema file> [<host>]");
		}
		int i = 0;
		String username   = args[i++],
			   password   = args[i++],
			   projectId  = args[i++],
			   dataset    = args[i++],
			   schemaFile = args[i++];
		String host = (args.length >= i + 1) ? args[i++] : "secure.gooddata.com";
		
		GdcRESTApiWrapper gd = new GdcRESTApiWrapper(
				new NamePasswordConfiguration("https", host, username, password));
		gd.login();
		SourceSchema schema = SourceSchema.createSchema(new File(schemaFile));

		SLI sli = gd.getSLIById(dataset, projectId);
		if (!dataset.equals("dataset." + StringUtil.toIdentifier(schema.getName()))) {
			throw new IllegalArgumentException("Schema file " + schemaFile
					+ " describes a diferent dataset than " + sli.getId());
		}
		
		DataSetDiffMaker diffMaker = new DataSetDiffMaker(gd, sli);
		List<SourceColumn> newColumns = diffMaker.findNewColumns(schema);
		List<SourceColumn> deletedColumns = diffMaker.findDeletedColumns(schema);
		MaqlGenerator mg = new MaqlGenerator(schema);
		
		StringBuilder maql = new StringBuilder();
		maql.append(mg.generateMaqlAdd(newColumns));
		maql.append(mg.generateMaqlDrop(deletedColumns));
		// the following model changes are not implemented yet even in the DLI branch:
		//    labels, references, connection points
		
		System.out.println(maql.toString());
	}
	
	private static class DataSetDiffMaker {
		private Set<SourceColumn> remoteColumns = new HashSet<SourceColumn>();
		
		DataSetDiffMaker(GdcRESTApiWrapper gd, SLI sli) {
			String datasetId = sli.getId().replaceAll("^dataset\\.", "");
			String factPrefix = N.FCT_PFX + datasetId + ".";   // TODO use me!
			String lookupPrefix = N.LKP_PFX + datasetId + "_"; // TODO use me!
			List<Column> sliColumns = gd.getSLIColumns(sli.getUri());
			
			for (final Column c : sliColumns) {
				// final SourceColumn sc = new SourceColumn(name, ldmType, title)
			}
		}
		
		List<SourceColumn> findNewColumns(SourceSchema ss) {
			// TODO
			throw new UnsupportedOperationException("Not implemented yet");
		}
		
		List<SourceColumn> findDeletedColumns(SourceSchema ss) {
			// TODO
			throw new UnsupportedOperationException("Not implemented yet");
		}
		
	}

}
