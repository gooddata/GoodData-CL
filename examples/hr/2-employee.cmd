OpenProject(id=hxdgs0hzv1objfeyujzmfpvr0qmocewe);
LoadCsv(csvDataFile=examples/hr/employee.csv, configFile=examples/hr/employee.xml);
GenerateMaql(maqlFile=examples/hr/employee.maql);
ExecuteMaql(maqlFile=examples/hr/employee.maql);
TransferLastSnapshot(incremental=false);