OpenProject(id=02021eod4lv9qpyrzuufbgr8m25k3ths);
LoadCsv(csvDataFile=examples/hr/employee.csv, configFile=examples/hr/employee.xml);
GenerateMaql(maqlFile=examples/hr/employee.maql);
ExecuteMaql(maqlFile=examples/hr/employee.maql);
TransferLastSnapshot(incremental=false);