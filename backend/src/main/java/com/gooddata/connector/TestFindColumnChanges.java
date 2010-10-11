package com.gooddata.connector;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.gooddata.integration.model.SLI;
import com.gooddata.integration.rest.GdcRESTApiWrapper;
import com.gooddata.integration.rest.configuration.NamePasswordConfiguration;
import com.gooddata.modeling.generator.MaqlGenerator;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.util.StringUtil;

public class TestFindColumnChanges {

	/**
	 * Testing class to generate an ALTER MAQL script and print it on stdout.
	 * Run without params to see the usage message.
	 * 
	 * @deprecated 
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
		
		DataSetDiffMaker diffMaker = new DataSetDiffMaker(gd, sli, schema);
		List<SourceColumn> newColumns = diffMaker.findNewColumns();
		List<SourceColumn> deletedColumns = diffMaker.findDeletedColumns();
		MaqlGenerator mg = new MaqlGenerator(schema);
		
		StringBuilder maql = new StringBuilder();
		maql.append(mg.generateMaqlAdd(newColumns, diffMaker.sourceColumns));
		maql.append(mg.generateMaqlDrop(deletedColumns));
		// the following model changes are not implemented yet even in the DLI branch:
		//    labels, references, connection points
		
		System.out.println(maql.toString());
	}

}
