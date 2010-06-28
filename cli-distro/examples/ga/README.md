# Loading your Google Analytics data into GoodData

This example demonstrates the built-in Google Analytics data connector that allows you to quickly load your Google Analytics data into GoodData. In order to run this example, you need a Google Account with an access to a Google Analytics Profile (i.e. access to a website statistics in Google Analytics).

### Before you start

Edit the `examples/ga/ga.txt` file and locate the line starting with the `LoadGoogleAnalytics` command. Change the values of parameters `username`, `password` and `profileId`; don't forget to keep the leading `ga:` string in the `profileId` parameter.

Make sure you provide `username` and `password` of an account that can log on to the Google Analytics user interface to access the statitics of the entered profile.

The value of the `profileId` parameter is the same as the value of the `id` field in the web address of your Google Analytics reports:
 ![Google Analytics profileId in the web address screenshot](http://github.com/gooddata/GoodData-CL/raw/master/cli-distro/examples/ga/ga_profileId.png "Google Analytics profileId in the web address screenshot")

For example, if you log into your Google Analytics user interface with username `jane@example.com` and password `jabberwocky78` and the web address of your profile's Google Analytics dashboard is `https://www.google.com/analytics/reporting/?reset=1&id=7468896&pdr=20100527-20100626` then your `LoadGoogleAnalytics` command will be as follows:

    LoadGoogleAnalytics(configFile="examples/ga/ga.config.xml",
                        username="jane@example.com",
                        password="jabberwocky78",
                        profileId="ga:7468896",
                        dimensions="ga:date|ga:browser|ga:browserVersion|ga:country|ga:isMobile",
                        metrics="ga:bounces|ga:newVisits|ga:pageViews|ga:visits",
                        startDate="2008-01-01",endDate="2010-06-15");

### How to run it

After you have customized the `ga.txt` script, you can try the Google Analytics example by running the following command:

_Windows:_

        c:> bin\gdi.bat -u <username> -p <password> examples\ga\ga.txt
        Project id = 'f5977852bfec20271d4c9bc453a263cb' created.


_Unix like OS (Linux, Mac OS X and others):_

        $ ./bin/gdi.sh -u <username> -p <password> examples/ga/ga.txt
        Project id = 'f5977852bfec20271d4c9bc453a263cb' created.


## What Next?

This example creates a data set that contains five dimensions (_ga:date_, _ga:browser_, _ga:browserVersion_, _ga:country_ and _ga:isMobile_) and four metrics (_ga:bounces_, _ga:newVisits_, _ga:pageViews_ and _ga:visits_). However, you mean to include additional dimension or metrics into your production project, or you may want to reduce the complexity by dropping fields you are not insterested in.

First, you will have to edit the list of dimensions and metrics to download. This can be done by editing the `dimensions` and `metrics` parameters of the `LoadGoogleAnalytics` command in the `ga.txt` script. 

For a complete list of available metrics and dimensions refer to the [Google Analytics documentation](http://code.google.com/apis/analytics/docs/gdata/gdataReferenceDimensionsMetrics.html).

There are two Google Analytics limitation that must be taken into account: first, a Google Analytics data set cannot contain more than seven dimensions. Second, only certain combinations of metrics and dimensions are supported, more information is available at the [Valid Query Combinations](http://code.google.com/apis/analytics/docs/gdata/gdataReferenceValidCombos.html) page.

When you have defined the source of your data, you need to describe the data set in the XML configuration file. You can either do it manually by editing the [ga.config.xml] file or you can generate it by uncommenting the `GenerateGaConfig` command from the `ga.txt` script and setting its `dimensions` and `metrics` parameters as you have done with the `LoadGoogleAnalytics` command.
