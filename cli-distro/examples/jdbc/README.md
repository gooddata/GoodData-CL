# Loading Fundamentals data from an existing JDBC source into GoodData

This example shows how to connect an existing database accessible via JDBC, and create and populate a GoodData data set with results of a `SELECT` statement performed against an embedded database of companies fundamentals.

Run `gdi.sh` with the `fundamentals.txt` script. The script uses the `fundamentals.config.xml` configuration.

_Windows:_

        c:> bin\gdi.sh -u <username> -p <password> examples\jdbc\fundamentals.txt
        Project id = 'yz5uq4am9ip3vxiou6m8xntd81r8qdh6' created.

_Unix like OS (Linux, Mac OS X and others):_

        $ ./bin/gdi.sh -u <username> -p <password> examples/jdbc/fundamentals.txt
        Project id = 'yz5uq4am9ip3vxiou6m8xntd81r8qdh6' created.

This particular example is built using the embedded [Derby](http://db.apache.org/derby/) database.

When building an integration scenario connecting to your own database you can start with modifying this example.

In such case, the first steps you need to perform are as follows:

 - grab the appropriate **JDBC 4** driver (can be downloaded from your database vendor's website, ask you database administrator if unsure)
 - place it to the `lib/` folder of the installation directory
 - modify the `driver` and `url` parameters of the `LoadJdbc` command appropriately. If unsure about the `url` syntax please consult the documentation of your JDBC driver or your database administrator.

For your convenience, this distribution has already bundled a couple of freely redistributable JDBC 4 drivers in the `lib/` folder as follows:

 - MySQL
 - PostgreSQL
 - Apache Derby
    