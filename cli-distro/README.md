This is GoodData command-line utility. The utility contains data modeling, transformation and loading functionality that work on top of the [GoodData REST APIs](http://developer.gooddata.com/api/).

See separate [Windows](#iwin) and [Unix](#iunix) instructions.

<a name="iunix"></a>
## UNIX/Mac installation


1. [download](http://github.com/gooddata/GoodData-CL/downloads) and unpack the binary distribution:

2. Make sure that your JAVA_HOME environment variable points to your Java installation directory.
   Please note that only the Java versions 1.5 and 1.6 are currently supported.

3. run the gdi.sh without arguments to get help from the unpacked distribution:

        $ ./bin/gdi.sh
        usage: GdcDI
         -h,--host <arg>       GoodData host
         -p,--password <arg>   GoodData password
         -u,--username <arg>   GoodData username
        [...]

<a name="iwin"></a>
## Windows installation

1. [download](http://github.com/gooddata/GoodData-CL/downloads) and unpack the binary distribution:

2. Make sure that your JAVA_HOME environment variable points to your Java installation directory.
   Please note that only the Java versions 1.5 and 1.6 are currently supported.

3. run the gdi.bat without arguments to get help from the unpacked distribution:

        bin\gdi.bat
        usage: GdcDI
         -h,--host <arg>       GoodData host
         -p,--password <arg>   GoodData password
         -u,--username <arg>   GoodData username
        [...]

# Next Steps

Please see the [GoodData CL examples and documentation](http://developer.gooddata.com/gooddata-cl/).