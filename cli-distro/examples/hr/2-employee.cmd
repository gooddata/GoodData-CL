# THIS EXAMPLE DEMONSTRATES HOW TO LOAD MORE COMPLEX STRUCTURES TO GOODDATA
# THREE DATASETS: DEPARTMENT, EMPLOYEE, AND SALARY ARE LOADED ARE CONNECTED TOGETHER
# CHECK THE CONFIGURATION FILES THAT CONTAINS THE CONNECTION POINTS AND REFERENCES

# RETRIEVE THE PROJECT THAT HAS BEEN SAVED BY THE FIRST SCRIPT
RetrieveProject(fileName="examples/hr/pid");

# LOAD THE EMPLOYEE CSV
LoadCsv(csvDataFile="examples/hr/employee.csv",header="true",configFile="examples/hr/employee.xml");

# GENERATE THE EMPLOYEE MAQL
GenerateMaql(maqlFile="examples/hr/employee.maql");

# EXECUTE THE EMPLOYEE MAQL
ExecuteMaql(maqlFile="examples/hr/employee.maql");

# TRANSFER THE EMPLOYEE DATA
TransferLastSnapshot(incremental="false");