# Getting Started

For initial installation, please refer to the [README](http://developer.gooddata.com/gooddata-cl/install.html).

# Basic Usage

1. The utility is invoked using the gdi.sh shell script. In each run, you need to provide your GoodData username and password:

        $ ./bin/gdi.sh -u <username> -p <password>

2. You can either run a command through the -e parameter or specify path to a text file with commands:

        $ ./bin/gdi.sh -u <username> -p <password> -e 'CreateProject("my project");'
        $ ./bin/gdi.sh -u <username> -p <password> /path/to/commands.txt

3. A [list of commands](http://developer.gooddata.com/gooddata-cl/cli-commands.html) and descriptions is displayed when you run `gdi.sh` without parameter.

# Workflow

The utility can help you automate creating projects, created data models and loading data. Follow one of the examples in [section below](#examples) to see the workflow. A typical scenario has following steps:

1. tell the utility to read the CSV file and generate an example XML configuration file
2. configure the XML configuration file ([see XML documentation](http://developer.gooddata.com/gooddata-cl/xml-config.html))
3. let the utility to create a project, setup a data model according to the XML file and load data into this project
4. alternatively, repeat the last part (data loading) as desirable

# Examples

There are several examples available in your examples folder of GoodData CL. Each example contains it's own [README](http://developer.gooddata.com/gooddata-cl/examples/) file describing usage.
