CreateProject(name=HR);
LoadCsv(csvDataFile=data/hr/department.csv, configFile=data/hr/department.xml);
GenerateMaql(maqlFile=data/hr/department.maql);
ExecuteMaql(maqlFile=data/hr/department.maql);
TransferLastSnapshot(incremental=false);