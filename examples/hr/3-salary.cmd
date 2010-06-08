OpenProject(id=m88artk6gtloz1lbldux3g2usbfsz3tl);
LoadCsv(csvDataFile=examples/hr/salary.csv, configFile=examples/hr/salary.xml);
GenerateMaql(maqlFile=examples/hr/salary.maql);
ExecuteMaql(maqlFile=examples/hr/salary.maql);
TransferLastSnapshot(incremental=false);