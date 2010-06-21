# THIS EXAMPLE DEMONSTRATES HOW TO LOAD MORE COMPLEX STRUCTURES TO GOODDATA
# THREE DATASETS: DEPARTMENT, EMPLOYEE, AND SALARY ARE LOADED ARE CONNECTED TOGETHER
# CHECK THE CONFIGURATION FILES THAT CONTAINS THE CONNECTION POINTS AND REFERENCES

# CREATE A NEW PROJECT
CreateProject(name="HR");

# STORE THE PROJECT ID TO FILE FOR THE FOLLOWING TWO SCRIPTS
StoreProject(fileName="examples/hr/pid");

# LOAD THE DEPARTMENT FILE
LoadCsv(csvDataFile="examples/hr/department.csv",header="true",configFile="examples/hr/department.xml");

# GENERATE THE DEPARTMENT MAQL
GenerateMaql(maqlFile="examples/hr/department.maql");

# EXECUTE THE DEPARTMENT MAQL
ExecuteMaql(maqlFile="examples/hr/department.maql");

# TRANSFER THE DEPARTMENT DATA
TransferLastSnapshot(incremental="false");