# This example demonstrates how to load more complex structures to GoodData
# Three datasets: department, employee, and salary are loaded are connected together
# Check the configuration files that contains the connection points and references

# Retrieve the project ID saved by the first script
RetrieveProject(fileName="examples/hr/pid");

# Load the salary data file, using the XML file describing the data
LoadCsv(csvDataFile="examples/hr/salary.csv",header="true",configFile="examples/hr/salary.xml");

# Generate the MAQL script describing data model for salary data
GenerateMaql(maqlFile="examples/hr/salary.maql");

# Execute the salary MAQL script on the server
ExecuteMaql(maqlFile="examples/hr/salary.maql");

# Transfer the salary data
TransferLastSnapshot(incremental="false");