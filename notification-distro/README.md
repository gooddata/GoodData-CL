This is GoodData Notification command-line utility.

See separate [Windows](#iwin) and [Unix](#iunix) instructions.

<a name="iunix"></a>
## UNIX/Mac installation


1. [download](http://support.gooddata.com/entries/317382-gooddata-notification) and unpack the binary distribution:

2. Make sure that your JAVA_HOME environment variable points to your Java installation directory.
   Please note that only the Java versions 1.5 and 1.6 are currently supported.

3. run the gdn.sh without arguments to get help from the unpacked distribution:

        $ ./bin/gdn.sh 
        Usage: gdn.sh -u username -p password -c ext-username -d ext-password <config-file>
         -h,--host <arg>       GoodData host (secure.gooddata.com by default)
         -p,--password <arg>   GoodData password
         -u,--username <arg>   GoodData username
         -c,--transportpassword <arg>   The transport (e.g. SFDC) password
         -d,--transportusername <arg>   The transport (e.g. SFDC) username
         -V, --version         Prints out the tool version
         file                  path to config file with the message profiles

<a name="iwin"></a>
## Windows installation

1. [download](http://support.gooddata.com/entries/317382-gooddata-notification) and unpack the binary distribution:

2. Make sure that your JAVA_HOME environment variable points to your Java installation directory.
   Please note that only the Java versions 1.5 and 1.6 are currently supported.

3. run the gdn.bat without arguments to get help from the unpacked distribution:

        bin\gdn.bat
        Usage: gdn.sh -u username -p password -c ext-username -d ext-password <config-file>
         -h,--host <arg>       GoodData host (secure.gooddata.com by default)
         -p,--password <arg>   GoodData password
         -u,--username <arg>   GoodData username
         -c,--transportpassword <arg>   The transport (e.g. SFDC) password
         -d,--transportusername <arg>   The transport (e.g. SFDC) username
         -V, --version         Prints out the tool version
         file                  path to config file with the message profiles