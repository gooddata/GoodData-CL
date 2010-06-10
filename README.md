This is GoodData data integration toolkit. The toolkit contains data modeling, transformation and loading utilities that work on top of the GoodData REST APIs.

# Installation

See separate [Windows](#win) and [Unix](#unix) instructions.

<a name="unix">
## UNIX/Mac installation
</a>

1. [download](http://github.com/gooddata/Java-DI-Tool/archives/master) the code and unpack or clone the Git repository:

        $ git clone http://github.com/gooddata/Java-DI-Tool.git

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
        [...]

5. _(optional)_ symlink gdi.sh into your executable path, for example:

        $ sudo ln -s /<path-to-source-dir>/bin/gdi.sh /usr/local/bin/gdi

<a name="win">
## Windows installation
</a>

1. [download](http://github.com/gooddata/Java-DI-Tool/archives/master) the code and unzip

2. make sure you have [Apache Maven](http://maven.apache.org/) installed:

        C:\>mvn --version
        Apache Maven 2.2.1 (r801777; 2009-08-06 21:16:01 +0200)
        [...]

3. build the toolkit:

        C:\>build.bat
        [...]
        [INFO] BUILD SUCCESSFUL
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time: 9 seconds
        [INFO] Finished at: Tue Jun 01 20:07:19 CEST 2010
        [INFO] Final Memory: 31M/81M
        [INFO] ------------------------------------------------------------------------

4. change to the `bin` directory and run the bat file without argument to get help:

        C:\>cd %path-to-install-dir%\bin
        C:\path-to-install-dir\bin>gdi.bat
        usage: GdcDI
         -h,--host <arg>       GoodData host
         -p,--password <arg>   GoodData password
         -u,--username <arg>   GoodData username
        [...]