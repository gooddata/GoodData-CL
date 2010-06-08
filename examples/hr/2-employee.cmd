OpenProject(id=m88artk6gtloz1lbldux3g2usbfsz3tl);
LoadCsv(csvDataFile=examples/hr/employee.csv, configFile=examples/hr/employee.xml);
GenerateMaql(maqlFile=examples/hr/employee.maql);
ExecuteMaql(maqlFile=examples/hr/employee.maql);
TransferLastSnapshot(incremental=false);