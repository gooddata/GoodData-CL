# XML Configuration File

The XML configuration file is generated using the GenerateCsvConfig() command. It uses your CSV file to find the names of columns in the first row of the file. The generated XML file is an example and should be modified before being used.

### Basic Structure
The file contains a name of the schema and entries for all your data columns:

{% highlight xml %}

        <schema>
          <name>[SCHEMA NAME]</name>
          <columns>
            <column>
              [COLUMN OPTIONS]
            </column>
            [...]
          </columns>
        </schema>

{% endhighlight %}

The schema name will ultimately be represented as a name of the dataset in the product.

### Column Options
In each `<column>` entry, following tags can be used:

1. `<name>` (**required**) - unique identifier for this column (will be used internally to generate MAQL DDL identifier)
2. `<title>` (**required**) - the name of this column on UI level
3. `<ldmType>` (**required**) - data mode of this column, one of:

    * ATTRIBUTE -- a generic column containing data that is used as labels - either strings or numbers that cannot be added (ex. Name, social security number, id)
    * FACT -- a column containing numeric values that can be added 
    * LABEL -- a secondary value of an attribute column (ex. column containing 01, 02, 03 and column containing Jan, Feb, Mar or full name column "John Doe" vs. "J. Doe" etc.) Requires `<reference>`, see below
    * DATE -- a column containing a date (`<format>` field required, see below). We will add a date dimension to this column, allowing you to aggregate by weeks, months, dates of week etc.
    * CONNECTION_POINT -- this identifies a primary key of the whole schema that can be used for connecting to another schema
    * REFERENCE -- a counterpart of CONNECTION_POINT in the other schema
    * IGNORE -- this column will be skipped and not uploaded

4. `<format>` only allowed (and required) for columns with ldmType DATE, this field specifies what the date format looks like (`yyyy-MM-dd` by default). We currently support following formatting characters:
    * yyyy -- year (e.g. 2010), _currently no support for the short format (yy)_
    * MM -- month (01 - 12)
    * dd -- day (01 - 31)
    * hh -- hour (01 - 12)
    * HH -- hour 24 format (00 - 23)
    * mm -- minutes (00 - 59)
    * ss -- seconds (00 - 59)
    * kk/kkkk -- microseconds or fractions of seconds (00-99, 000-999, 0000-9999)
    There are few special formats:
    * UNIXTIME - this is the epoch format (number of seconds since 1970-01-01). Please note that the epoch is in the UTC by default and will be converted to the CL tool's timezone automatically.
    * GOODDATA - this is the GoodData format (number of days since 1900-01-01).
5. `<reference>` - used with LABEL columns for pointing to the primary column; used with CONNECTION_POINT column and `<schemaReference>`
6. `<schemaReference>` - used with CONNECTION_POINT to identify the schema name of the counterpart REFERENCE column. When connecting to a date dimension, use the same value as in the `name` parameter of the `LoadDateDimension` command (see also [the CLI commands documentation](https://github.com/gooddata/GoodData-CL/blob/master/cli-distro/doc/CLI.md#time_dimension_connector_commands)).
7. `<folder>` allows to specify what folder this column is organized into (on UI level). 
8. `<transformation>` - apply transformation on this column. Currently supports:
    * `IDENTITY` -- automatically fills this fields with an MD5 hash of all non-fact fields (attributes, labels etc.) in the current row. This column does not have any representation in the datasource (ie. it's purely generated).
9. `<datetime>` - used in a DATE column to indicate that the date contains time (in the form of `<datetime>true</datetime>`)

10. `<dataType>` specified type of the column and size allocation. Possible values:

    * VARCHAR(N) -- N (1..255)
    * DECIMAL(M,D) -- M min(-1e+15) max(1e+15), D max = 6
    * INT -- min(-2147483648) max(2147483647)
    * BIGINT -- min(-1e+15) max(1e+15)
    * DATE -- 'YYYY-MM-DD'

11. `<sortLabel>` - used with ATTRIBUTE columns for pointing to the LABEL by which the ATTRIBUTE is sorted

12. `<sortOrder>` - specifies the <sortLabel> sorting order (ASC | DESC). Default is ASC.
