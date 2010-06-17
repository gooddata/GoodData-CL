# Loading your Zendesk data into GoodData

1. **download your Zendesk data** in CSV format. Go to your Zendesk page, log in as admin, go to Manage > Reports, in Exports section generate export of all your tickets. Unzip and save this file into this zendesk folder with the name `data.csv`

        $ cat Downloads/data/<filename>-csv.zip | gunzip > examples/zendesk/data.csv

2. run `gdi.sh` with the GenerateCsvConfigTemplate command. This generates a `config.xml` file:

        $ ./bin/gdi.sh -u <username> -p <password> -e 'GenerateCsvConfigTemplate(csvHeaderFile=examples/zendesk/data.csv,configFile=examples/zendesk/config.xml)'

3. now you need configure your `config.xml` file to describe your data. For detailed instructions, see [documentation](../../doc/DOCUMENTATION.md#config). For your Zendesk data, you need to remove all &lt;folder&gt; and &lt;pk&gt; entries, change all &lt;ldmType&gt; entries to ATTRIBUTE, except entries for "Summation column" and "Resolution time", which should be set to FACT:

        $ vi ./examples/zendesk/config.xml

4. to **create your project** and set up the **data model**, run `gdi.sh` with the `1-create.txt` script. This uses your `config.xml` file to create a MAQL DDL script with the data model, runs this script in your new project:

        $ ./bin/gdi.sh -u <username> -p <password> ./examples/zendesk/1-create.txt
        Project id = 'f5977852bfec20271d4c9bc453a263cb' created.

5. to **load your data**, run `gdi.sh` with the `2-load.txt` script. Note that you'll have to replace the project ID on the first line of the load script with the ID of your new project. The ID was returned to you on stdout during the run of the create script (see previous step):

        $ vi ./examples/zendesk/2-load.txt    # add the project ID from previous step
        $ ./bin/gdi.sh -u <username> -p <password> ./examples/zendesk/2-load.txt

In the future, you can reuse the load script (with the saved project ID) to automate repeated data loads into your project.