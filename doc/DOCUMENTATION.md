<a name="start">
# Getting Started
</a>

For initial installation, please refer to the [README](http://github.com/gooddata/Java-DI-Tool#readme).

<a name="basic">
# Basic Usage
</a>

1. The utility is invoked using the gdi.sh shell script. In each run, you need to provide your GoodData username and password:

        $ ./bin/gdi.sh -u <username> -p <password>

2. You can either run a command through the -e parameter or specify path to a text file with commands:

        $ ./bin/gdi.sh -u <username> -p <password> -e 'CreateProject("my project")'
        $ ./bin/gdi.sh -u <username> -p <password> /path/to/commands.txt

3. A list of commands and descriptions is displayed when you run `gdi.sh` without parameter. It can also be found [here](http://github.com/gooddata/Java-DI-Tool/blob/master/src/main/resources/com/gooddata/processor/COMMANDS.txt#files)

<a name="workflow">
# Workflow
</a>

The utility can help you automate creating projects, created data models and loading data. Follow one of the examples in [section below](#examples) to see the workflow. A typical scenario has following steps:

1. tell the utility to read the CSV file and generate an example XML configuration file
2. configure the XML configuration file ([see documentation below](#config))
3. let the utility to create a project, setup a data model according to the XML file and load data into this project
4. alternatively, repeat the last part (data loading) as desirable

<a name="config">
# XML Configuration File
</a>

The XML configuration file is generated using the GenerateCsvConfig() command. It uses your CSV file to find the names of columns in the first row of the file. The generated XML file is an example and should be modified before being used.

### Basic Structure
The file contains a name of the schema and entries for all your data columns:

        <schema>
          <name>[SCHEMA NAME]</name>
          <columns>
            <column>
              [COLUMN OPTIONS]
            </column>
            [...]
          </columns>
        </schema>

The schema name will ultimately be represented as a name of the dataset in the product.

### Column Options
In each &lt;column&gt; entry, following tags can be used:

1. &lt;name&gt; (**required**) - unique identifier for this column (will be used internally to generate MAQL DDL identifier)
2. &lt;title&gt; (**required**) - the name of this column on UI level
3. &lt;ldmType&gt; (**required**) - data type of this column, one of:

    * ATTRIBUTE - a generic column containing data that is used as labels - either strings or numbers that cannot be added (ex. Name, social security number, id)
    * FACT - a column containing numeric values that can be added 
    * LABEL - a secondary value of an attribute column (ex. column containing 01, 02, 03 and column containing Jan, Feb, Mar or full name column "John Doe" vs. "J. Doe" etc.) Requires &lt;reference&gt;, see below
    * DATE - a column containing a date (&lt;format&gt; field required, see below). We will add a date dimension to this column, allowing you to aggregate by weeks, months, dates of week etc.
    * CONNECTION_POINT - this identifies a primary key of the whole schema that can be used for connecting to another schema
    * REFERENCE - a counterpart of CONNECTION_POINT in the other schema

4. &lt;folder&gt; _not yet supported_ (optional) allows to specify what folder this column is organized into (on UI level)
5. &lt;format&gt; only allowed (and required) for columns with ldmType DATE, this field specifies what the date format looks like
6. &lt;reference&gt; - used with LABEL columns for pointing to the primary column; used with CONNECTION_POINT column and &lt;schemaReference&gt;
7. &lt;schemaReference&gt; - used with CONNECTION_POINT to identify the schema name of the counterpart REFERENCE column

<a name="examples">
# Examples
</a>

The examples folder contains typical some typical scenarios. Each example contains it's own [README](../examples/zendesk/README.md#readme) file describing usage.