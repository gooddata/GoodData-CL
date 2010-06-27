# Loading Google Analytics data into GoodData

This example demonstrates the built-in Google Analytics data connector that allows you to quickly load your Google Analytics data into GoodData. In order to run this example, you need a Google Account with an access to a Google Analytics Profile (i.e. access to a website statistics in Google Analytics).

Edit the `examples/ga/ga.txt` file and locate the line starting with the `LoadGoogleAnalytics` command. Change the values of parameters `username`, `password` and `profileId`; don't forget to keep the leading `ga:` string in the `profileId` parameter.

Make sure you provide `username` and `password` of an account that can log on to the Google Analytics user interface to access the statitics of the entered profile.

The value of the `profileId` parameter is the same as the value of the `id` field in the web address of your Google Analytics reports:
 ![Google Analytics profileId in the web address screenshot](http://github.com/gooddata/GoodData-CL/raw/master/cli-distro/examples/ga/ga_profileId.png "Google Analytics profileId in the web address screenshot")

For example, if you log into your Google Analytics user interface with username `jane@example.com` and password `jabberwocky78` and the web address of your profile's Google Analytics dashboard is `https://www.google.com/analytics/reporting/?reset=1&**id=7468896**&pdr=20100527-20100626` then your `LoadGoogleAnalytics` command will be as follows:

    LoadGoogleAnalytics(configFile="examples/ga/ga.config.xml",username="jane@example.com",password="jabberwocky78",profileId="ga:7468896",dimensions="ga:date|ga:browser|ga:browserVersion|ga:country|ga:isMobile",metrics="ga:bounces|ga:newVisits|ga:pageViews|ga:visits",startDate="2008-01-01",endDate="2010-06-15");

Run `gdi.sh` with the `ga.txt` script. The script uses the `ga.config.xml` configuration file to describe incoming data.

_Windows:_

        c:> bin\gdi.bat -u <username> -p <password> examples\ga\ga.txt
        Project id = 'f5977852bfec20271d4c9bc453a263cb' created.


_Unix like OS (Linux, Mac OS X and others):_

        $ ./bin/gdi.sh -u <username> -p <password> examples/ga/ga.txt
        Project id = 'f5977852bfec20271d4c9bc453a263cb' created.


## What Next?

Review the [XML file](ga.config.xml) and the [documentation](http://github.com/gooddata/GoodData-CL/blob/master/cli-distro/doc/DOCUMENTATION.md#config) if you want to include additional metrics and dimensions.

Note: data sets involving more than 7 dimensions are not supported by Google Analytics.
