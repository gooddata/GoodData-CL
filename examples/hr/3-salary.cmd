OpenProject(id="fikge21btqnvhug9ojleox8enr3pekak");
LoadCsv(csvDataFile="examples/hr/salary.csv",header="true",configFile="examples/hr/salary.xml");
GenerateMaql(maqlFile="examples/hr/salary.maql");
ExecuteMaql(maqlFile="examples/hr/salary.maql");
TransferLastSnapshot(incremental="false");