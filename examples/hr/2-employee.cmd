OpenProject(id=i36yydbvtint9ayt725n7got2dtia9kr);
LoadCsv(csvDataFile=examples/hr/employee.csv,header=true,configFile=examples/hr/employee.xml);
GenerateMaql(maqlFile=examples/hr/employee.maql);
ExecuteMaql(maqlFile=examples/hr/employee.maql);
TransferLastSnapshot(incremental=false);