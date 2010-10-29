# Getting Started

For initial installation, please refer to the [README](http://developer.gooddata.com/gooddata-cl/install.html).

# Basic Usage

1. The utility is invoked using the gdn.sh shell script. In each run, you need to provide your GoodData username and password:

        $ ./bin/gdn.sh -u <username> -p <password>

2. You can either run a command through the -e parameter or specify path to a text file with commands:

        $ ./bin/gdn.sh -u <username> -p <password> -e 'CreateProject("my project");'
        $ ./bin/gdn.sh -u <username> -p <password> /path/to/commands.txt

3. A [list of commands](http://developer.gooddata.com/gooddata-cl/cli-commands.html) and descriptions is displayed when you run `gdn.sh` without parameter.

# Workflow


# Examples

There are several examples available in your examples folder.
