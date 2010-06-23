This is GoodData data integration toolkit. The toolkit contains data modeling, transformation and loading utilities that
work on top of the GoodData REST APIs.

<a name="binary">
# Binary installation
</a>

See separate [Windows](#iwin) and [Unix](#iunix) instructions.

<a name="iunix">
## UNIX/Mac installation
</a>

1. [download](http://github.com/gooddata/GoodData-DI/downloads) and unpack the binary distribution:

2. Make sure that your JAVA_HOME environment variable points to your Java installation directory.
   Please note that only the Java version 1.6 is currently supported.

3. run the gdi.sh without arguments to get help from the unpacked distribution:

        $ ./bin/gdi.sh
        usage: GdcDI
         -h,--host <arg>       GoodData host
         -p,--password <arg>   GoodData password
         -u,--username <arg>   GoodData username
        [...]

<a name="iwin">
## Windows installation
</a>

1. [download](http://github.com/gooddata/GoodData-DI/downloads) and unpack the binary distribution:

2. Make sure that your JAVA_HOME environment variable points to your Java installation directory.
   Please note that only the Java version 1.6 is currently supported.

3. run the gdi.bat without arguments to get help from the unpacked distribution:

        bin\gdi.bat
        usage: GdcDI
         -h,--host <arg>       GoodData host
         -p,--password <arg>   GoodData password
         -u,--username <arg>   GoodData username
        [...]

