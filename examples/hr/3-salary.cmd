OpenProject(id=7r8y6aoapuvis6ln2vtfen69075670q9);
LoadCsv(csvDataFile=examples/hr/salary.csv, configFile=examples/hr/salary.xml);
GenerateMaql(maqlFile=examples/hr/salary.maql);
ExecuteMaql(maqlFile=examples/hr/salary.maql);
TransferLastSnapshot(incremental=false);