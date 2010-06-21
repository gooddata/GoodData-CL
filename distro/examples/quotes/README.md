# Loading Quotes data into GoodData

This basic example loads a data file containing stock quotes data.

Run `gdi.sh` with the `quotes.cmd` script. The script uses the `quotes.config.xml` configuration.

        $ ./bin/gdi.sh -u <username> -p <password> examples/quotes/quotes.cmd
        Project id = 'f5977852bfec20271d4c9bc453a263cb' created.

This creates a "Quotes" project in GoodData, sets up the data model and loads the data file into the project. Read further comments in the script file for instructions how to setup a repeatable data load.