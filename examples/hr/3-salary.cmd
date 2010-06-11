OpenProject(id=1mkcqkvka4aoph9hip33ea4rvu2g4p0z);
LoadCsv(csvDataFile=examples/hr/salary.csv,header=true,configFile=examples/hr/salary.xml);
GenerateMaql(maqlFile=examples/hr/salary.maql);
ExecuteMaql(maqlFile=examples/hr/salary.maql);
TransferLastSnapshot(incremental=false);