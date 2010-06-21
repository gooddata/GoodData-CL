# THIS EXAMPLE DEMONSTRATES HOW TO LOAD MORE COMPLEX STRUCTURES TO GOODDATA
# THREE DATASETS: DEPARTMENT, EMPLOYEE, AND SALARY ARE LOADED ARE CONNECTED TOGETHER
# CHECK THE CONFIGURATION FILES THAT CONTAINS THE CONNECTION POINTS AND REFERENCES

# RETRIEVE THE PROJECT THAT HAS BEEN SAVED BY THE FIRST SCRIPT
RetrieveProject(fileName="examples/hr/pid");

# LOAD THE SALARY CSV
LoadCsv(csvDataFile="examples/hr/salary.csv",header="true",configFile="examples/hr/salary.xml");

# GENERATE THE SALARY MAQL
GenerateMaql(maqlFile="examples/hr/salary.maql");

# EXECUTE THE SALARY MAQL
ExecuteMaql(maqlFile="examples/hr/salary.maql");

# TRANSFER THE SALARY DATA
TransferLastSnapshot(incremental="false");