# THIS EXAMPLE SHOWS HOW TO LOAD THE GOOGLE ANALYTICS DATA TO THE GOODDATA PROJECT

# CREATE A NEW PROJECT
CreateProject(name="GA");

# GENERATE CONFIG FILE. THIS COMMAND IS COMMENTED OUT AS WE HAVE DONE THAT ALREADY.
# IF YOU CHANGE THE GA METRICS AND DIMENSIONS YOU NEED TO RE-RUN THE CONFIG FILE GENERATION
#GenerateGaConfig(name="GA",configFile="examples/ga/ga.config.xml",dimensions="ga:date|ga:browser|ga:browserVersion|ga:country|ga:isMobile",metrics="ga:bounces|ga:newVisits|ga:pageViews|ga:visits");

# LOADS A NEW TIME DIMENSION TO THE PROJECT
LoadTimeDimension(context="ga");

# GENERATE THE TIME DIMENSION MAQL
GenerateMaql(maqlFile="examples/ga/time.maql");

# EXECUTE THE TIME DIMENSION MAQL
# DATA FOR THE TIME DIMENSION ARE GENERATED AUTOMATICALLY BY GOOD DATA
# NO DATA TRANSFER IS NECESSARY FOR TIME DIMENSIONS
ExecuteMaql(maqlFile="examples/ga/time.maql");

# LOAD THE GOOGLE ANALYTICS DATA
LoadGoogleAnalytics(configFile="examples/ga/ga.config.xml",username="gdc.bot@gmail.com",password="zd",profileId="ga:7468896",dimensions="ga:date|ga:browser|ga:browserVersion|ga:country|ga:isMobile",metrics="ga:bounces|ga:newVisits|ga:pageViews|ga:visits",startDate="2009-01-01",endDate="2010-05-31");

# GENERATE THE GA MAQL
# CHECK OUT THE GA CONFIG THAT CONNECTS THE DATA COLUMN TO THE TIME DIMENSION THAT WE HAVE GENERATED ABOVE
GenerateMaql(maqlFile="examples/ga/ga.maql");

# EXECUTE THE GA MAQL
ExecuteMaql(maqlFile="examples/ga/ga.maql");

# TRANSFER THE GA DATA
TransferLastSnapshot(incremental="true");