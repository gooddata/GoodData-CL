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

# Using GoodData CL with the MySQL Database

GoodData CL uses the embedded Derby SQL database engine by default. This database engine provides sufficient
performance for datasets up to 300k records. We recommend you to use the MySQL database in case you wish to
transfer larger amounts of data. You must install the MySQL on the local machine and make sure that it listens
on the standard 3306 TCP port. Once you have the MySQL engine up and running, you can switch to it using the
 `-b`, `-d`, and `-c` flags.

 e.g. `bin/gdi.sh -u username -p password -b mysql -d mysql-user -c mysql-psw <script-file>`

# Next Steps

Please see the [GoodData CL examples and documentation](/gooddata-cl/).