CreateProject(name=HR);
LoadCsv(csvDataFile=examples/hr/department.csv,header=true,configFile=examples/hr/department.xml);
GenerateMaql(maqlFile=examples/hr/department.maql);
ExecuteMaql(maqlFile=examples/hr/department.maql);
TransferLastSnapshot(incremental=false);