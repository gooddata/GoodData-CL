# Salesforce Chatter Notifications

This example sends notifications to Salesforce Chatter based on report conditions in GoodData.

1. ### Edit the config file
Review the `config.xml` file in this folder, most importantly the `<condition>` statement, which can contain a metric name. What each metric name means is defined below in the `<metrics>` section. A URL to the metric in GoodData must be specified.


2. ### Run the tool (regurarly)
Run `gdn.sh` with the `config.xml` like this:  
_Windows:_

        c:> bin\gdn.bat -u <username> -p <password> -c <salesforce-username> -d <salesforce-password> examples\sfdc-chatter\config.xml
        INFO [main] (GdcNotification.java:314) - Notification sent.
_Unix like OS (Linux, Mac OS X and others):_

        $ ./bin/gdn.sh -u <username> -p <password> -c <salesforce-username> -d <salesforce-password> examples/sfdc-chatter/config.xml
        INFO [main] (GdcNotification.java:314) - Notification sent.
