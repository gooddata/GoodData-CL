OpenProject(id=e9xznchz1s4cy0hcs0s2ahlptxdurpyw);
LoadCsv(csvDataFile=data/hr/employee.csv, configFile=data/hr/employee.xml);
GenerateMaql(maqlFile=data/hr/employee.maql);
ExecuteMaql(maqlFile=data/hr/employee.maql);
TransferLastSnapshot(incremental=false);