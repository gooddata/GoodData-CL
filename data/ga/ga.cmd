CreateProject(name=GA);
GenerateGoogleAnalyticsConfigTemplate(name=GA,configFile=data/ga/ga.config.xml,dimensions=ga:date|ga:browser,metrics=ga:visits|ga:pageviews);
LoadGoogleAnalytics(configFile=data/ga/ga.config.xml,username=user@gmail.com,password=password,profileId=ga:7467765,dimensions=ga:date|ga:browser,metrics=ga:visits|ga:pageviews,startDate=2010-01-01,endDate=2010-05-31);
GenerateMaql(maqlFile=data/ga/ga.maql);
ExecuteMaql(maqlFile=data/ga/ga.maql);
TransferLastSnapshot(incremental=true);