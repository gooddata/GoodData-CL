This is GoodData data integration toolkit. The toolkit contains data modeling, transformation and loading utilities that
work on top of the GoodData REST APIs.

Unless you wish to participate in development of the tool, you should choose the easier [binary](https://github.com/gooddata/GoodData-CL/blob/master/cli-distro/README.md)
install.

<a name="source"></a>
# Building from sources

See separate [Windows](#swin) and [Unix](#sunix) instructions.

<a name="sunix"></a>
## UNIX/Mac installation

1. [download](http://github.com/gooddata/GoodData-CL/archives/master) the code and unpack *(or git clone this repository)*:

2. make sure you have [Apache Maven](http://maven.apache.org/) installed:

        $ which mvn
        /usr/bin/mvn

3. create the M2_HOME environment variable that points to the Maven root directory

        $ export M2_HOME=path-to-your-maven-installation-dir

4. Make sure that your JAVA_HOME environment variable points to your Java installation directory.
   Please note that only the Java versions 1.5 and 1.6 are currently supported.

5. build the toolkit:

        $ mvn install

6. build the client distribution

        $ cd cli-distro
        $ mvn assembly:assembly

7. unpack one of the distributions that have been built in the cli-distro/target subdirectory:

    - gooddata-cl-1.1.0-SNAPSHOT.tar.gz
    - gooddata-cl-1.1.0-SNAPSHOT.zip

8. run the gdi.sh without arguments to get help from the unpacked distribution:

        $ ./bin/gdi.sh
        usage: GdcDI
         -h,--host <arg>       GoodData host
         -p,--password <arg>   GoodData password
         -u,--username <arg>   GoodData username
        [...]

<a name="swin"></a>
## Windows installation

1. [download](http://github.com/gooddata/GoodData-CL/archives/master) the code and unzip

2. make sure you have [Apache Maven](http://maven.apache.org/) installed:

        C:\>mvn --version
        Apache Maven 2.2.1 (r801777; 2009-08-06 21:16:01 +0200)
        [...]

3. create the M2_HOME environment variable that points to the Maven root directory

        C:\> SET M2_HOME=path-to-your-maven-installation-dir

4. Make sure that your JAVA_HOME environment variable points to your Java installation directory.
   Please note that only the Java versions 1.5 and 1.6 are currently supported.

5. build the toolkit:

        C:\>mvn install

6. build the client distribution

        C:\>cd cli-distro
        C:\>mvn assembly:assembly

7. unpack one of the distributions that have been built in the cli-distro/target subdirectory:

    - gooddata-cl-1.1.0-SNAPSHOT.tar.gz
    - gooddata-cl-1.1.0-SNAPSHOT.zip

8. run the gdi.bat without arguments to get help from the unpacked distribution:

        bin\gdi.bat
        usage: GdcDI
         -h,--host <arg>       GoodData host
         -p,--password <arg>   GoodData password
         -u,--username <arg>   GoodData username
        [...]