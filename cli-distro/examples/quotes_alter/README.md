# Loading time based data file into GoodData

This basic example loads a CSV data file containing stock quotes from the AMEX, NASDAQ and NYSE markets from January to August 2008.

Run `gdi.sh` with the `quotes.txt` script as follows:

_Windows:_

    c:> bin\gdi.bat -u <username> -p <password> examples\quotes\quotes.txt
    Project id = 'f5977852bfec20271d4c9bc453a263cb' created.

_Unix like OS (Linux, Mac OS X and others):_

    $ ./bin/gdi.sh -u <username> -p <password> examples/quotes/quotes.txt
    Project id = 'f5977852bfec20271d4c9bc453a263cb' created.

This creates a "Quotes" project in GoodData, sets up the data model and loads the data file into the project.

## What's Inside?

Each line of the CSV file contain fields titled `Id`, `Company`, `Symbol`, `Sector`, `Industry`, `Market`, `Quote Date`, `Open Price`, `High Price`, `Low Price`, `Close Price`, `Volume` and `Adjusted Close Price`.

These fields are described in the pre-created `examples/quotes/config.xml` configuration file that consists of records such as:

{% highlight xml %}

    <column>
      <name>MARKET</name>
      <title>Market</title>
      <ldmType>ATTRIBUTE</ldmType>
    </column>
    ...
    <column>
      <name>CLOSE_PRICE</name>
      <title>Close Price</title>
      <ldmType>FACT</ldmType>
    </column>

{% endhighlight %}

First `<column>` record describes the first column of the CSV file, second record the second column and so on. Most of fields is either a number to be aggregated, such as a Stock Price (its `ldmType` is `FACT`) or it can be used to break down an aggregated number, e.g Market or Industry - these fields have `ldmType` set to `ATTRIBUTE`.

Other `ldmType`s include `CONNECTION_POINT` (a unique identifier of a record within a dataset), `LABEL` (an alias of an `ATTRIBUTE` or a `CONNECTION_POINT`) and `DATE`. For example, the `Id` field is marked as `CONNECTION_POINT`, `Company` is an alias of `SYMBOL` and the `Quote Date` holds a date formatted as `yyyy-MM-dd` (e.g. 2008-02-28 for February 28th 2008).

Note the `schemaReference` property tells GoodData to connect this date field to the date dimension named _Quote_. This date dimension is created in the model by the following lines at the begining of the `quotes.txt` script:

{% highlight ruby %}

    UseDateDimension(name="Quote");
    GenerateMaql(maqlFile="examples/quotes/quote_date.maql");
    ExecuteMaql(maqlFile="examples/quotes/quote_date.maql");

{% endhighlight %}

The structure of the logical model built on the top of this data set is as follows:

![Quotes Logical Model Diagram](http://developer.gooddata.com/images/gdcl/examples/quotes/quotes_ldm.png "Quotes Logical Model Diagram")

The light green boxes depict attributes including the `Id` connection point, the ovals represent facts. Labels and most of the attributes of the `Date (Quotes)` dimension hidden for the sake of simplicity. The attributes belonging to the date dimension are shown using double border line. 

## What Next?

Now you can log into the [GoodData user interface](https://secure.gooddata.com/) and select the _Quotes_ project. When you switch to the _Data_ section and click _Model_ in the left menu bar you can see a data model visualization similar to what's outlined above. Then you can switch to the _Reports_ section and start building your first reports. 

Read further comments in the [script file](http://github.com/gooddata/GoodData-CL/blob/master/cli-distro/examples/quotes/quotes.txt) for instructions how to setup a repeatable data load or how to create another project based your own CSV data set.
