This is GoodData data integration toolkit. The toolkit contains data modeling, transformation and loading utilities that
work on top of the GoodData REST APIs. You can either follow instructions for [binary installation](#binary) or you can [compile](#source) from source code. Unless you wish to participate in development of the tool, you should choose the easier [binary](#binary) install.

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

<a name="source">
# Building from sources
</a>

See separate [Windows](#swin) and [Unix](#sunix) instructions.

<a name="sunix">
## UNIX/Mac installation
</a>

1. [download](http://github.com/gooddata/GoodData-DI/archives/master) the code and unpack *(or git clone this repository)*:

2. make sure you have [Apache Maven](http://maven.apache.org/) installed:

        $ which mvn
        /usr/bin/mvn

3. create the M2_HOME environment variable that points to the Maven root directory

        $ export M2_HOME=path-to-your-maven-installation-dir

4. Make sure that your JAVA_HOME environment variable points to your Java installation directory.
   Please note that only the Java version 1.6 is currently supported.

5. build the toolkit:

        $ ./build.sh
        [...]
        [INFO] BUILD SUCCESSFUL
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time: 9 seconds
        [INFO] Finished at: Tue Jun 01 20:07:19 CEST 2010
        [INFO] Final Memory: 31M/81M
        [INFO] ------------------------------------------------------------------------

6. unpack one of the distributions that have been built in the cli-distro/target subdirectory:

    - gooddata-di-cli-1.0-SNAPSHOT-final.tar.bz2
    - gooddata-di-cli-1.0-SNAPSHOT-final.tar.gz
    - gooddata-di-cli-1.0-SNAPSHOT-final.zip




7. run the gdi.sh without arguments to get help from the unpacked distribution:

        $ ./bin/gdi.sh
        usage: GdcDI
         -h,--host <arg>       GoodData host
         -p,--password <arg>   GoodData password
         -u,--username <arg>   GoodData username
        [...]

<a name="swin">
## Windows installation
</a>

1. [download](http://github.com/gooddata/GoodData-DI/archives/master) the code and unzip

2. make sure you have [Apache Maven](http://maven.apache.org/) installed:

        C:\>mvn --version
        Apache Maven 2.2.1 (r801777; 2009-08-06 21:16:01 +0200)
        [...]

3. create the M2_HOME environment variable that points to the Maven root directory

        C:\> SET M2_HOME=path-to-your-maven-installation-dir

4. Make sure that your JAVA_HOME environment variable points to your Java installation directory.
   Please note that only the Java version 1.6 is currently supported.

5. build the toolkit:

        C:\>build.bat
        [...]
        [INFO] BUILD SUCCESSFUL
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time: 9 seconds
        [INFO] Finished at: Tue Jun 01 20:07:19 CEST 2010
        [INFO] Final Memory: 31M/81M
        [INFO] ------------------------------------------------------------------------

6. unpack one of the distributions that have been built in the cli-distro/target subdirectory:

    - gooddata-di-cli-1.0-SNAPSHOT-final.tar.bz2
    - gooddata-di-cli-1.0-SNAPSHOT-final.tar.gz
    - gooddata-di-cli-1.0-SNAPSHOT-final.zip

7. run the gdi.bat without arguments to get help from the unpacked distribution:

        bin\gdi.bat
        usage: GdcDI
         -h,--host <arg>       GoodData host
         -p,--password <arg>   GoodData password
         -u,--username <arg>   GoodData username
        [...]