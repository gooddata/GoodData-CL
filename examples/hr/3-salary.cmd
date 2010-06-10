OpenProject(id=02021eod4lv9qpyrzuufbgr8m25k3ths);
LoadCsv(csvDataFile=examples/hr/salary.csv, configFile=examples/hr/salary.xml);
GenerateMaql(maqlFile=examples/hr/salary.maql);
ExecuteMaql(maqlFile=examples/hr/salary.maql);
TransferLastSnapshot(incremental=false);