# Loading Fundamentals data from an existing JDBC source into GoodData

This example shows how to connect an existing database accessible via JDBC, and create and populate a GoodData data set with results of a `SELECT` statement performed against an embedded database of companies fundamentals.

Run `gdi.sh` with the `fundamentals.txt` script. The script uses the `fundamentals.config.xml` configuration.

_Windows:_

        c:> bin\gdi.sh -u <username> -p <password> examples\quotes\quotes.txt
        Project id = 'yz5uq4am9ip3vxiou6m8xntd81r8qdh6' created.

_Unix like OS (Linux, Mac OS X and others):_

        $ ./bin/gdi.sh -u <username> -p <password> examples/quotes/quotes.txt
        Project id = 'yz5uq4am9ip3vxiou6m8xntd81r8qdh6' created.


This particular example is built using the embedded [Derby](http://db.apache.org/derby/) database.

You can start by modifying this example when building an integration scenario connecting to your own database. In such case, the first steps you need to perform are as follows:
- grab the appropriate JDBC driver (ask you database administrator if unsure)
- place it to the `lib/` folder of the installation directory
- modify the `driver` and `url` parameters of the `LoadJdbc` command appropriately 
