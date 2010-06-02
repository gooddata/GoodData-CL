This is GoodData data integration toolkit. The toolkit contains data modeling, transformation and loading utilities that work on top of the GoodData REST APIs.

# Getting Started

1. download [the code](http://github.com/gooddata/Java-DI-Tool/archives/master) and unpack
2. make sure you have [Apache Maven](http://maven.apache.org/) installed:

        $ which mvn
        /usr/bin/mvn

3. build the toolkit:

        $ ./build.sh
        [...]
        [INFO] BUILD SUCCESSFUL
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time: 9 seconds
        [INFO] Finished at: Tue Jun 01 20:07:19 CEST 2010
        [INFO] Final Memory: 31M/81M
        [INFO] ------------------------------------------------------------------------

4. run without arguments to get help:

        $ ./bin/gdi.sh
        usage: GdcDI
         -h,--host <arg>       GoodData host
         -p,--password <arg>   GoodData password
         -u,--username <arg>   GoodData username

5. if you want, place gdi.sh into your executable path

        $ sudo ln -s /.../bin/gdi.sh /usr/local/bin/gdi
