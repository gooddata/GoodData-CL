# Loading Quotes data into GoodData

This basic example loads a CSV data file containing stock quotes from the AMEX, NASDAQ and NYSE markets from January to August 2008.

Each line of the CSV file contain fields titled `Id`, `Company`, `Symbol`, `Sector`, `Industry`, `Market`, `Quote Date`, `Open Price`, `High Price`, `Low Price`, `Close Price`, `Volume` and `Adjusted Close Price`.

These fields are described in the pre-created `examples/quotes/quotes.config.xml` configuration file that consists of records such as:

    <column>
      <name>MARKET</name>
      <title>Market</title>
      <ldmType>ATTRIBUTE</ldmType>
    </column>

Most of fields is either a number to be aggregated, such as a stock price (its `ldmType` is `FACT`) or it can be used to break down an aggregated number, e.g Market or Industry - these fields have `ldmType` set to `ATTRIBUTE`.

Other `ldmType`s include `CONNECTION_POINT` (a unique identifier of a record within a dataset), `LABEL` (an alias of an `ATTRIBUTE` or a `CONNECTION_POINT`) and `DATE`. For example, the `Id` field is marked as `CONNECTION_POINT`, `Company` is an alias of `SYMBOL` and the `Quote Date` holds a date formatted as `yyyy-MM-dd` (e.g. 2008-02-28 for February 28th 2008).

The structure of the logical model built on the top of this data set is as follows:

![Quotes Logical Model Diagram](http://github.com/gooddata/GoodData-DI/raw/master/cli-distro/examples/quotes/quotes_ldm.png "Quotes Logical Model Diagram")

The light green boxes depict attributes and the `Id` connection points, the ovals represent facts, labels and most of the attributes of the `Date (Quotes)` dimension such as `Month (Quotes)`, `Year (Quotes)` etc are hidden for the sake of simplicity. 

Run `gdi.sh` with the `quotes.txt` script as follows:

_Windows:_

        c:> bin\gdi.sh -u <username> -p <password> examples\quotes\quotes.txt
        Project id = 'f5977852bfec20271d4c9bc453a263cb' created.

_Unix like OS (Linux, Mac OS X and others):_

        $ ./bin/gdi.sh -u <username> -p <password> examples/quotes/quotes.txt
        Project id = 'f5977852bfec20271d4c9bc453a263cb' created.

This creates a "Quotes" project in GoodData, sets up the data model and loads the data file into the project. Read further comments in the [script file](quotes.txt) for instructions how to setup a repeatable data load or how to create another project based your own CSV data set. 
