OpenProject(id=e9xznchz1s4cy0hcs0s2ahlptxdurpyw);
LoadCsv(csvDataFile=data/hr/salary.csv, configFile=data/hr/salary.xml);
GenerateMaql(maqlFile=data/hr/salary.maql);
ExecuteMaql(maqlFile=data/hr/salary.maql);
TransferLastSnapshot(incremental=false);