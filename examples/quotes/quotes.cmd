# This is a simple example which loads a simple flat file with a stock quote data.
# Lines starting with "#" are comments and will not be executed

# Create a new project
CreateProject(name="Quotes");

# Load the stock quotes data file
# The XML config file describing data structure is already included in this example
LoadCsv(csvDataFile="examples/quotes/quotes.csv",header="true",configFile="examples/quotes/quotes.config.xml");

# Generate the stock quotes MAQL script
GenerateMaql(maqlFile="examples/quotes/quotes.maql");

# Execute the MAQL script generated above on the server to create data model in the project
ExecuteMaql(maqlFile="examples/quotes/quotes.maql");

# Transfer the stock quotes data
TransferLastSnapshot();

# If you wanted to repeatedly load data, you might want now to store the Project ID for later reuse by uncommenting the line below
# StoreProject(fileName="examples/quotes/quotes.pid");

# Subsequently, you'd run a following script:
# RetrieveProject(fileName="examples/quotes/quotes.pid");
# LoadCsv(csvDataFile="examples/quotes/quotes.csv", configFile="examples/quotes/quotes.config.xml");
# TransferData(incremental="false");
