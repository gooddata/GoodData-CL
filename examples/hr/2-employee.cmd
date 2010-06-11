OpenProject(id="fikge21btqnvhug9ojleox8enr3pekak");
LoadCsv(csvDataFile="examples/hr/employee.csv",header="true",configFile="examples/hr/employee.xml");
GenerateMaql(maqlFile="examples/hr/employee.maql");
ExecuteMaql(maqlFile="examples/hr/employee.maql");
TransferLastSnapshot(incremental="false");