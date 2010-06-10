OpenProject(id=i36yydbvtint9ayt725n7got2dtia9kr);
LoadCsv(csvDataFile=examples/hr/salary.csv,header=true,configFile=examples/hr/salary.xml);
GenerateMaql(maqlFile=examples/hr/salary.maql);
ExecuteMaql(maqlFile=examples/hr/salary.maql);
TransferLastSnapshot(incremental=false);