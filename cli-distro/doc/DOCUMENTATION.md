<a name="start">
# Getting Started
</a>

For initial installation, please refer to the [README](http://github.com/gooddata/GoodData-CL#readme).

<a name="basic">
# Basic Usage
</a>

1. The utility is invoked using the gdi.sh shell script. In each run, you need to provide your GoodData username and password:

        $ ./bin/gdi.sh -u <username> -p <password>

2. You can either run a command through the -e parameter or specify path to a text file with commands:

        $ ./bin/gdi.sh -u <username> -p <password> -e 'CreateProject("my project")'
        $ ./bin/gdi.sh -u <username> -p <password> /path/to/commands.txt

3. A list of commands and descriptions is displayed when you run `gdi.sh` without parameter. It can also be found [here](http://github.com/gooddata/GoodData-CL/blob/master/cli/src/main/resources/com/gooddata/processor/COMMANDS.txt#files)

<a name="workflow">
# Workflow
</a>

The utility can help you automate creating projects, created data models and loading data. Follow one of the examples in [section below](#examples) to see the workflow. A typical scenario has following steps:

1. tell the utility to read the CSV file and generate an example XML configuration file
2. configure the XML configuration file ([see XML documentation](XML.md)
3. let the utility to create a project, setup a data model according to the XML file and load data into this project
4. alternatively, repeat the last part (data loading) as desirable

<a name="examples">
# Examples
</a>

The examples folder contains typical some typical scenarios. Each example contains it's own [README](http://github.com/gooddata/GoodData-CL/blob/master/cli-distro/examples/README.md#readme) file describing usage.