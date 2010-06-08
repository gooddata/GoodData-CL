OpenProject(id=7r8y6aoapuvis6ln2vtfen69075670q9);
LoadCsv(csvDataFile=examples/hr/employee.csv, configFile=examples/hr/employee.xml);
GenerateMaql(maqlFile=examples/hr/employee.maql);
ExecuteMaql(maqlFile=examples/hr/employee.maql);
TransferLastSnapshot(incremental=false);