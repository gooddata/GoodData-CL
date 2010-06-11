OpenProject(id=1mkcqkvka4aoph9hip33ea4rvu2g4p0z);
LoadCsv(csvDataFile=examples/hr/employee.csv,header=true,configFile=examples/hr/employee.xml);
GenerateMaql(maqlFile=examples/hr/employee.maql);
ExecuteMaql(maqlFile=examples/hr/employee.maql);
TransferLastSnapshot(incremental=false);