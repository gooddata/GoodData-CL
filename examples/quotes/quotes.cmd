CreateProject(name=Quotes);
LoadCsv(csvDataFile=examples/quotes/quotes.csv,header=true,configFile=examples/quotes/quotes.config.xml);
GenerateMaql(maqlFile=examples/quotes/quotes.maql);
ExecuteMaql(maqlFile=examples/quotes/quotes.maql);
TransferLastSnapshot();
