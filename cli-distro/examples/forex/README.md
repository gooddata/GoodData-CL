# Using GoodData With Dates And Times

This example demonstrates support for time data built into GoodData CL.

Run `gdi.sh` with the `forex.txt` script as follows:

_Windows:_

    c:> bin\gdi.bat -u <username> -p <password> examples\forex\forex.txt
    Project id = 'f5977852bfec20271d4c9bc453a263cb' created.

_Unix like OS (Linux, Mac OS X and others):_

    $ ./bin/gdi.sh -u <username> -p <password> examples/forex/forex.txt
    Project id = 'f5977852bfec20271d4c9bc453a263cb' created.

## What's Inside?

There are two parts shown in this example - a GoodData provided date dimension (providing a hierarchy of days, weeks, months, years etc.) and a separate Time dimension (providing attributes such as AM/PM, hours, minutes, seconds). Both make use of the `DATETIME` column in the `forex.csv` data file.

First, we need to tell GoodData CL to set up the Date and Time dimensions in our project:

Here are the lines from `forex.txt` that take care of setting up the date & time dimensions:

{% highlight ruby %}
Create/Use/OpenProject(...);
UseDateDimension(name="Forex", includeTime="true"); # with includeTime=false sets up just the date dimension
GenerateMaql(maqlFile="date.maql");
ExecuteMaql(maqlFile="date.maql");
TransferData();                                     # this line is necessary only when includeTime=true
{% endhighlight %}

Now that we have the date & time dimensions present in our project, we can define a mapping between the DATETIME column in `forex.csv` file and these dimensions. In the definition of your data model XML schema, the column entry for the `DATETIME` column has 4 important entries:

{% highlight xml %}
<column>
  ...
  <ldmType>DATE</ldmType>
  <datetime>true</datetime>
  <schemaReference>Forex</schemaReference>
  <format>dd-MM-yyyy HH:mm:ss</format>          
  ...
</column>
{% endhighlight %}

* `<ldmType>DATE</ldmType>` - this tells GoodData CL to treat this field as a date field (by default without time support). GoodData CL parses this field according to the `<format>` and then connects it automatically to a Date dimension (identified by the `<schemaReference>`)

* `<datetime>true</datetime>` - this extents the LDM type DATE to include time support. GoodData CL will now be able to parse the field as DATETIME

* `<schemaReference>Forex</schemaReference>` - this specifies the name of the Date dimension to be used. Needs to correspond with the UseDateDimension() name parameter used in your script (here `forext.txt`)

* `<format>dd-MM-yyyy HH:mm:ss</format>` - this field helps GoodData CL understand how to parse the datetime string

![Forex Logical Model Diagram](http://developer.gooddata.com/images/gdcl/examples/forex/forex_ldm.png "Forex Logical Model Diagram")