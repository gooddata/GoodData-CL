OpenProject(id=e9xznchz1s4cy0hcs0s2ahlptxdurpyw);
LoadCsv(csvDataFile=examples/hr/salary.csv, configFile=examples/hr/salary.xml);
GenerateMaql(maqlFile=examples/hr/salary.maql);
ExecuteMaql(maqlFile=examples/hr/salary.maql);
TransferLastSnapshot(incremental=false);