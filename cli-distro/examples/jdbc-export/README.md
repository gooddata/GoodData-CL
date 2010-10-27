# Exporting Data from JDBC to CSV

When working with JDBC driver to download data from your SQL database, it is sometimes useful to be able to preview the data coming out from the JDBC driver before it's processed and uploaded to GoodData. The `ExportJdbcToCsv` allows just that - it retrieves data through the specified JDBC driver from the specified JDBC URL and dumps the data locally into CSV files for manual inspection.

### Usage

Configure the `cmd.txt` file with your own JDBC driver / query URL and then run:

_Windows:_

    c:> bin\gdi.bat -u <username> -p <password> examples\jdbc-export\cmd.txt

_Unix like OS (Linux, Mac OS X and others):_

    $ ./bin/gdi.sh -u <username> -p <password> examples/jdbc-export/cmd.txt
