# Loading SalesForce data into GoodData

## About this example

The SFDC example shows how to create a GoodData project that models basic SFDC objects and populates the project directly from your SFDC account.

_Windows:_

    c:> bin\gdi.bat -u <username> -p <password> examples\sfdc\sfdc.txt
    Project id = 'wuw52aoc8z6rjvd1ufem23zxhdeuukm3' created.
    Data successfully loaded.
    Data successfully loaded.

_Unix like OS (Linux, Mac OS X and others):_

    $ ./bin/gdi.sh -u <username> -p <password> examples/sfdc/sfdc.txt
    Project id = 'wuw52aoc8z6rjvd1ufem23zxhdeuukm3' created.
    Data successfully loaded.
    Data successfully loaded.

Note: this example fetches sample data from GoodData's demo account at SalesForce. 

## What's Inside?

The `sfdc.txt` script brings the following entities into the project:

 - date dimension associated with the opportunity creation using the `UseDateDimension(name="Created");`
 - date dimension associated with closing an opportunity using the `UseDateDimension(name="Closed");`
 - SFDC `Account` object; only the `Id` and `Name` fields are loaded as specified by the `query` parameter of the first `UseSfdc` command
 - SFDC `Opportunity` object with `Id`, `AccountId`, `IsWon`, `IsClosed`, `CloseDate`, `StageName`, `CreatedDate`, `ExpectedRevenue` and  `Amount` fields as specified by the query parameter of the second `UseSfdc` command

The _Account_ and _Opportunity_ data sets loaded by the `UseSfdc` commands are described by the `account.xml` and `opportunity.xml` configuration files. The configuration files describe the fields of the data sets in the order of fields occurence within the data sets.

For example, the `opportunity.xml` file describes the first field of the _Opportunity_ data set as a `CONNECTION_POINT`, second as a `REFERENCE` to the _Account_ data set, fourth and sixth as date fields belonging to _closed_ and _created_ date dimension and seventh and eighth as _FACT_s, i.e. a numeric value to be aggregated in GoodData.

The structure of the logical data model built on the top of these three data sets is as follows:

![SFDC example logical data model diagram](http://developer.gooddata.com/images/gdcl/examples/sfdc/sfdc_ldm.png "SFDC example logical data model diagram")

See the [Quotes example](../quotes/#readme) for explanation what these boxes and ovals stand for.

For a more detailed description of joining multiple data sets refer to the [HR example](../hr/#readme). 


## What next?

In order to customize this example to import your own SalesForce data into GoodData, you will need to change the SFDC username, password and security token parameters that are passed to `UseSfdc` and optionally `GenerateSfdcConfig` commands in the `sfdc.txt` script file.

**Note:** you need the _Enterprise Editition_ of Salesforce to be able to access your Salesforce data using the API. Alternatively, the _Professional Edition_ users may enable API access as an extra option.

### Reset your SFDC security token

If you don't know your SFDC security token, you can either ask your SFDC administrator for help or generate a new one using the following procedure:

  1. Click the _Setup_ link in SFDC menu:
     ![SFDC setup link screenshot](http://developer.gooddata.com/images/gdcl/examples/sfdc/sfdc_token_01_menu.png "SFDC Setup link screenshot")

  1. Click the _Reset security token_:
     ![SFDC setup screen screenshot](http://developer.gooddata.com/images/gdcl/examples/sfdc/sfdc_token_02_setup.png "SFDC Setup screen screenshot")

  1. Click the _Reset security token_ button in the following confirmation screen:
     ![SFDC token reset confirmation screenshot](http://developer.gooddata.com/images/gdcl/examples/sfdc/sfdc_token_03_confirm.png "SFDC token reset screenshot")

An e-mail with the SFDC security token will arrive into your mailbox soon; please check your _spam_ folder if it seems to take too long.

### Add more objects and fields

In order to add more objects and/or fields you'll need to pay attention to the `UseSfdc` and `GenerateSfdcConfig` commands in the `sfdc.txt` script. Note the `GenerateSfdcConfig` command is commented out in the `sfdc.txt` script, don't forget to uncomment it first.

The `query` attribute of the `UseSfdc` command specifies a query writen in the [Salesforce.com Object Query language](http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_soql.htm) also known as SOQL. 

The most simple data set takes the form of `SELECT <field1>, <field2>, ... FROM <object>` such as the `SELECT Id, Name FROM Account` query used to defined the _Accounts_ data set in this example. The API names of standard objects and their fields are listed in the [Salesforce API documentation](http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_objects_list.htm).

When adding a data set featuring custom fields or even custom object you need to know the API names of the custom fields. These API names can be found under the _Setup_ menu of your Salesforce. For example, in order to get the API name of the `Contract Value` field of GoodData's SalesForce we need to:

 1. **Click _Setup_ in the main menu** and expand App Setup / Customize / Opportunities / Fields items in the left menu bar:
 ![Click Setup and expand App Setup etc](http://developer.gooddata.com/images/gdcl/examples/sfdc/sfdc_fields_01_setup.png)
 2. **Scroll down** to the _Opportunity Custom Fields & Relationships_ and click the _Contract Value_ link
 ![Opportunity Custom Fields & Relationships](http://developer.gooddata.com/images/gdcl/examples/sfdc/sfdc_fields_02_custom_fields.png)
 3. **Check the _API Name_ field** in the following screen:
 ![SFDC API Name](http://developer.gooddata.com/images/gdcl/examples/sfdc/sfdc_fields_03_api_name.png)

So the last thing I need to add the `Contract_Value__c` field API name to the `query` parameter of the opportunity related occurences of the `UseSfdc` and `GenerateSfdcConfig` commands in the `sfdc.txt` script.
