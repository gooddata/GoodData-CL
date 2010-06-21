# Loading Google Analytics data into GoodData

This example shows the Google Analytics data connector in use.

Run `gdi.sh` with the `ga.cmd` script. The script uses the `ga.config.xml` configuration describing incoming data structure.

        $ ./bin/gdi.sh -u <username> -p <password> examples/quotes/ga.cmd
        Project id = 'f5977852bfec20271d4c9bc453a263cb' created.

This creates a `GA` project in GoodData, sets up the data model and loads the data file into the project. If you have a customized Google Analytics account, read the [script file](ga.config.xml) for further detail.