OpenProject(id=e9xznchz1s4cy0hcs0s2ahlptxdurpyw);
LoadCsv(csvDataFile=examples/hr/employee.csv, configFile=data/hr/employee.xml);
GenerateMaql(maqlFile=examples/hr/employee.maql);
ExecuteMaql(maqlFile=examples/hr/employee.maql);
TransferLastSnapshot(incremental=false);