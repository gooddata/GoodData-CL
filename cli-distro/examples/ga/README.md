# Loading your Google Analytics data into GoodData

This example demonstrates the built-in Google Analytics data connector that allows you to quickly load your Google Analytics data into GoodData. In order to run this example, you need a Google Account with an access to a Google Analytics Profile (i.e. access to a website statistics in Google Analytics).

### Before you start

Edit the `examples/ga/cmd.create.txt` file and locate the lines starting with the `UseGoogleAnalytics` command. Change the values of parameters `username`, `password` and `profileId`; don't forget to keep the leading `ga:` string in the `profileId` parameter.

Make sure you provide `username` and `password` of an account that can log on to the Google Analytics user interface to access the statistics of the entered profile.

The value of the `profileId` parameter is the same as the value of the `id` field in the web address of your Google Analytics reports:
 ![Google Analytics profileId in the web address screenshot](http://developer.gooddata.com/images/gdcl/examples/ga/ga_profileId.png "Google Analytics profileId in the web address screenshot")

For example, if you log into your Google Analytics user interface with username `jane@example.com` and password `jabberwocky78` and the web address of your profile's Google Analytics dashboard is `https://www.google.com/analytics/reporting/?reset=1&id=7468896&pdr=20100527-20100626` then your `UseGoogleAnalytics` commands will be as follows:

{% highlight ruby %}

    UseGoogleAnalytics(configFile="examples/ga/ga.config.xml",
        username="jane@example.com",
        password="jabberwocky78",
        profileId="ga:7468896",
        dimensions="ga:date|ga:browser|ga:browserVersion|ga:country|ga:isMobile",
        metrics="ga:bounces|ga:newVisits|ga:pageViews|ga:visits",
        startDate="2008-01-01",endDate="2010-06-15");

{% endhighlight %}

I'm sure you have noticed the `startDate` and the `endDate` parameters and guessed what these are for.

### How to run it

After you have customized the `cmd.create.txt` script, you can try the Google Analytics example by running the following command:

_Windows:_

    c:> bin\gdi.bat -u <username> -p <password> examples\ga\ga.txt
    Project id = 'f5977852bfec20271d4c9bc453a263cb' created.


_Unix like OS (Linux, Mac OS X and others):_

    $ ./bin/gdi.sh -u <username> -p <password> examples/ga/ga.txt
    Project id = 'f5977852bfec20271d4c9bc453a263cb' created.

## What Next?

This example creates a project with pre-defined reports and dashboards. The project contains six data sets. However, you mean to include additional dimension or metrics into your production project, or you may want to reduce the complexity by dropping fields you are not interested in.

First, you will have to edit the list of dimensions and metrics to download. This can be done by editing the `dimensions` and `metrics` parameters of the `UseGoogleAnalytics` command in the `cmd.create.txt` script.

For a complete list of available metrics and dimensions refer to the [Google Analytics documentation](http://code.google.com/apis/analytics/docs/gdata/gdataReferenceDimensionsMetrics.html).

There are two Google Analytics limitation that must be taken into account. First, a Google Analytics data set cannot contain more than seven dimensions. Second, only certain combinations of metrics and dimensions are supported, more information is available at the [Valid Query Combinations](http://code.google.com/apis/analytics/docs/gdata/gdataReferenceValidCombos.html) page from the GA documentation.

When you have defined the source of your data, you need to describe the data set in the XML configuration file. You can either do it manually by editing the `ga.config.xml` file or you can generate it by using the `GenerateGaConfig` command.

The script `cmd.update.txt` refreshes the Google Analytics data in a GoodData project that has been previously created via the `cmd.create.txt` script. If you modify the dimensions and metrics in the `cmd.create.txt`, you need to replicate these changes to the `cmd.update.txt` script.

Don't worry about the overlapping dates (the `startDate` and the `endDate` parameters), the overlapping records are not going to be duplicated.

## Loading Multiple Google Analytics Profiles?

If you want to load multiple Google Analytics profiles into a single GoodData project, you must execute the `cmd.update.txt` multiple times with different Google Analytics `profileId` parameters. To set this scenario up, you must perform following steps:

1. Uncomment three lines in the `cmd.create.txt` that talk about multiple GA profiles. This will modify your project's existing GA data model by executing the `profile.maql`. Run the `cmd.create.txt` to create your project (as described above).
* List all your Google Analytics profiles in the `profile.csv`. The `profileId` column values should be your Google Analytics profile ID (including the `ga:` prefix) , the `profileName` column values contain your name for the profile (as it will appear in GoodData).
* Change the `IGNORE` ldmType in all XML configurations to the `REFERENCE`.
* Uncomment two lines in the `cmd.update.txt` that talk about multiple GA profiles. These run the script to load the `profile.csv` data to the project.

Now you can load multiple Google Analytics profiles to your project. By default, your reports and dashboards will show data for all profiles together. If you want to report on data from a specific profile only, you need to [add a filter](https://secure.gooddata.com/docs/html/reference.guide.createreports.filters.html) to your report for a specific Profile (or even add an [interactive variable](https://secure.gooddata.com/docs/html/reference.guide.dashboard.filters.html) to your dashboard).
