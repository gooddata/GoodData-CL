GoodData CL Commands
=====================

GoodData Cl supports following groups of commands:

* **[Project Management Commands](#project_management_commands)**: create/delete/use/remember a project
  `CreateProject`, `DeleteProject`, `OpenProject`, `RememberProject`. `UseProject`

* **[Connector Commands](#data_connectors)** that either generate the [XML configuration](http://developer.gooddata.com/gooddata-cl/xml-config.html) (`Generate<Source-Type>Config`) for a specific data source and load the  source data (`Use<Source-Type>`). Connector commands require a project to be activated via a project management command before they are invoked.

* **[Metadata Management Commands](#metadata_management_commands)**: work with project metadata (reports, dashboards, metrics, folders)  
  `RetrieveMetadataObject`, `StoreMetadataObject`, `DropMetadataObject`, `RetrieveAllObjects`, `StoreAllObjects`

* **[Logical Model Management Commands](#logical_model_management_commands)** generate & execute [MAQL DDL](http://developer.gooddata.com/api/maql-ddl.html) script for a connector that has been previously loaded via the `Use<Source-Type>` command.
  `GenerateMaql`, `GenerateUpdateMaql`, `ExecuteMaql`

* **[Data Transfer Commands](#data_transfer_commands)** that transform, and transfer the data from a previously loaded (`Use<Source-Type>`) connector.  
  `TransferData`

Project Initialization Workflow
-------------------------------
Usually you want to initialize your project with following commands:

1. `CreateProject` or `OpenProject`. 
2. Optionally you generate [XML configuration](http://developer.gooddata.com/gooddata-cl/xml-config.html) for your data source using a `Generate<Source-Type>Config` command that yields an XML configuration file. This file describes your data structure and a way how the GoodData Logical Data Model is going to be generated. Sometimes you might want to review the XML config file and perform some changes. You'll most probably want to comment out the `Generate<Source-Type>Config` after the first run.
3. Initialize your data source Connector using a `Use<Source-Type>` command. The `Use<Source-Type>` command requires the XML config file and a specific parameters that define the data source data or query (e.g. a SQL query).
4. Generate and execute [MAQL DDL](http://developer.gooddata.com/api/maql-ddl.html) for your data source using the `GenerateMaql` and `ExecuteMaql` commands. The [MAQL DDL](http://developer.gooddata.com/api/maql-ddl.html) generates your project's Logical Data Model (LDM) and Data Loading Interface (DLI). The DLI is later used by the following `TransferData` command. You need to generate your LDM and DLI only once per each project. That is why the scripts that transfer data on regular basis don't use the the `GenerateMaql` and `ExecuteMaql` commands.
5. `TransferData` command that transforms, packages, and transfers the data.

Project Data Loading Workflow
-----------------------------
The ongoing data loading scripts usually:

1. `OpenProject` or `UseProject` command.
2. Initialize your data source Connector using a `Use<Source-Type>` command. The `Use<Source-Type>` command requires the XML config file and a specific parameters that define the data source data or query (e.g. a SQL query).
3. `TransferData` command that transforms, packages, and transfers the data.


---------------------------------------------------------------------------


Commands Reference
==================

The following paragraphs describe the specific GoodData CL commands. 


Project Management Commands:
----------------------------

`CreateProject(name="...", desc="...", templateUri="...");` - create a new project on the server
- name        - name of the new project
- desc        - *(optional)* project description
- templateUri - *(optional)* project template to create the project from

`DeleteProject(id="...");` - drop the project on the server
- id - *(optional)* project id, if not specified, the command tries to drop the current project
-

`OpenProject(id="...");` - open an existing project for data modeling and data upload.
- id - identifier of an existing project (takes the form of an MD5 hash)
-

`RememberProject(fileName="...");` - saves the current project identifier into the specified file
- fileName - file to save the project identifier
-

`UseProject(fileName="...");` - loads the current project identifier from the specified file
- fileName - file to load the project identifier from
-

`InviteUser(email="...", msg="...", role="...");` - invites a new user to the project (must call `CreateProject` or `OpenProject` before)
- email - the invited user's e-mail
- msg   - *(optional)* invitation message
- role  - *(optional)* initial user's role: `admin`|`editor`|`dashboard only`


Metadata Management Commands:
-----------------------------

> **Important:** All the commands in this section expect
> to know what project to work in already. You must call
> `CreateProject`, `OpenProject` or `RetrieveProject`
> in your script at some place before these commands.

`RetrieveAllObjects(dir="...");` - retrieves all metadata objects (reports, dashboards, metrics) from the current project and stores it in a directory
- dir - directory where the objects content (JSON) are going to be stored
-

`StoreAllObjects(dir="...", overwrite="...");` - copies all metadata (reports, dashboards, metrics) objects from the directory to the current project
- dir       - directory where the copied objects content (JSON) are stored
- overwrite - if true overwrite the existing objects in the project (true | false)

`RetrieveMetadataObject(id="...", file="...");` - retrieves a metadata object and stores it in a file
- id   - valid object id (integer number)
- file - file where the object content (JSON) is going to be stored

`StoreMetadataObject(file="...", id="...");` - stores a metadata object with a content (JSON) in file to the metadata server
- file - file where the object content (JSON) is stored
- id   - *(optional)* valid object id (integer number), if the id is specified, the object is going to be modified, if not, a new object is created

`DropMetadataObject(id="...");` - drops the object with specified id from the project's metadata
- id - valid object id (integer number)
-

`Lock(path="...");` - prevents concurrent run of multiple instances sharing the same lock file. Lock files older than 1 hour are discarded.
- path - path to a lock file
-

`MigrateDatasets(configFiles="...");` - migrates the project's datasets from CL 1.1.x to CL 1.2.x
- configFiles - the comma separated list of ALL project's dataset's XML configuration files
-


Logical Model Management Commands:
----------------------------------

> **Important:** All the commands in this section expect
> to know what project to work in and how your data model
> looks. You must call one of (`CreateProject`, `OpenProject`
> or `RetrieveProject`) commands and a `Use<Connector>`
> command in your script at some place before these commands.

`GenerateMaql(maqlFile="...");` - generate MAQL DDL script describing data model from the local config file
- maqlFile - path to MAQL file (will be overwritten)
-

`GenerateUpdateMaql(maqlFile="...");` - generate MAQL DDL alter script that creates the columns available in the local configuration but missing in the remote GoodData project
- maqlFile - path to MAQL file (will be overwritten)
-

`ExecuteMaql(maqlFile="...", ifExists="...");` - run MAQL DDL script on server to generate data model
- maqlFile - path to the MAQL file (relative to PWD)
- ifExists - *(optional)* if set to true the command quits silently if the maqlFile does not exist (true | false, default is false)


Data Transfer Commands:
-----------------------

> **Important:** All the commands in this section expect
> to know what project to work in and know the data model
> and data. You must call one of (`CreateProject`, `OpenProject`
> or `RetrieveProject`) commands and a `Use<Connector>`
> command in your script at some place before these commands.

`TransferData(incremental="...", waitForFinish="...");` - upload data to the GoodData server
- incremental   - *(optional)* when true, will try to append (or merge/replace via matching CONNECTION_POINT) the data. (true | false, default is false)
- waitForFinish - *(optional)* the process waits for the server-side processing (true | false, default is true)

`Dump(csvFile="...");` - dumps the connector data to a local CSV file
- csvFile   - path to the CSV file


Data Connectors:
==================

The following commands provide data connectors for consuming data from flat files ([CSV Connector](#csv_connector_commands)), databases ([JDBC Connector](#jdbc_connector_commands)) and even SaaS apps ([Salesforce](#salesforce_connector_commands), [Google Analytics](#googleanalytics_connector_commands), [PivotalTracker](#pivotaltracker_connector_commands) Connectors). All connectors provide commands like `Generate<Type>Config`, `Use<Type>`, `Export<Type>ToCsv`. Data input should be encoded in UTF-8.

CSV Connector Commands:
-----------------------

`GenerateCsvConfig(csvHeaderFile="...", configFile="...", defaultLdmType="...", facts="...", folder="...", separator="...");` - generate a sample XML config file based on the fields from your CSV file. If the config file exists already, only new columns are added. The config file must be edited as the LDM types (attribute | fact | label etc.) are assigned randomly.
- csvHeaderFile  - path to CSV file (only the first header row will be used)
- configFile     - path to configuration file (will be **overwritten**)
- defaultLdmType - *(optional)* LDM mode to be associated with new columns (only ATTRIBUTE mode is supported by the ProcessNewColumns task at this time)
- facts          - *(optional)* comma separated list of fields known to be facts
- folder         - *(optional)* folder where to place new attributes
- separator      - *(optional)* field separator, the default is ','  

`UseCsv(csvDataFile="...", configFile="...", hasHeader="...", separator = "...");` - load CSV data file using config file describing the file structure, must call `CreateProject` or `OpenProject` before
- csvDataFile - path to CSV datafile
- configFile  - path to XML configuration file (see the GenerateCsvConfig command that generates the config file template)
- hasHeader   - *(optional)* true if the CSV file has a header row (true | false, default is true)
- separator   - *(optional)* field separator, the default is ','. Use '\t' or type the tab char for tabulator.


GoogleAnalytics Connector Commands:
-----------------------------------

`GenerateGoogleAnalyticsConfig(name="...", configFile="...", dimensions="...", metrics="...");` - generate an XML config file based on the fields from your GA query.
- name - the new dataset name
- configFile  - path to configuration file (will be overwritten)
- dimensions  - pipe (|) separated list of Google Analytics dimensions (see http://code.google.com/apis/analytics/docs/gdata/gdataReferenceDimensionsMetrics.html)
- metrics     - pipe (|) separated list of Google Analytics metrics (see http://code.google.com/apis/analytics/docs/gdata/gdataReferenceDimensionsMetrics.html)


`UseGoogleAnalytics(configFile="...", username="...", password="...", profileId="...", dimensions="...", metrics="...", startDate="...", endDate="...", filters="...");` - load GA data file using config file describing the file structure, must call `CreateProject` or `OpenProject` before
- configFile  - path to configuration file (will be overwritten)
- token       - Google Analytics AuthSub token (you must specify either the token or username/password)
- username    - Google Analytics username (you must specify either the token or username/password)
- password    - Google Analytics password (you must specify either the token or username/password)
- profileId   - Google Analytics profile ID (this is a value of the id query parameter in the GA url)
- dimensions  - pipe (|) separated list of Google Analytics dimensions (see http://code.google.com/apis/analytics/docs/gdata/gdataReferenceDimensionsMetrics.html)
- metrics     - pipe (|) separated list of Google Analytics metrics (see http://code.google.com/apis/analytics/docs/gdata/gdataReferenceDimensionsMetrics.html)
- startDate   - the GA start date in the yyyy-mm-dd format  
- endDate     - the GA end date in the yyyy-mm-dd format  
- filters     - the GA filters (see http://code.google.com/apis/analytics/docs/gdata/gdataReferenceDataFeed.html#filters)


JDBC Connector Commands:
------------------------

`GenerateJdbcConfig(name="...", configFile="...", driver="...", url="...", query="...", username="...", password="...");`  - generate an XML config file based on the fields from your JDBC query.
- name       - the new dataset name
- configFile - path to configuration file (will be overwritten)
- driver     - JDBC driver string (e.g. "org.apache.derby.jdbc.EmbeddedDriver"), you'll need to place the JAR with the JDBC driver to the lib subdirectory
- url        - JDBC url (e.g. `jdbc:derby:mydb`)
- query      - SQL query (e.g. `SELECT employee,dept,salary FROM payroll`)
- username   - *(optional)* JDBC username
- password   - *(optional)* JDBC password
  
`UseJdbc(configFile="...", driver="...", url="...", query="...", username="...", password="...");` - load JDBC data file using config file describing the file structure, must call `CreateProject` or `OpenProject` before
- configFile - path to configuration file (will be overwritten)
- driver     - JDBC driver string (e.g. "org.apache.derby.jdbc.EmbeddedDriver"), you'll need to place the JAR with the JDBC driver to the lib subdirectory
- url        - JDBC url (e.g. "jdbc:derby:mydb")
- query      - SQL query (e.g. "SELECT employee,dept,salary FROM payroll")
- username   - *(optional)* JDBC username
- password   - *(optional)* JDBC password

`ExportJdbcToCsv(dir="...", driver="...", url="...", username="...", password="...");` - exports all tables from the database to CSV file
- dir      - target directory
- driver   - JDBC driver string (e.g. "org.apache.derby.jdbc.EmbeddedDriver"), you'll need to place the JAR with the JDBC driver to the lib subdirectory
- url      - JDBC url (e.g. "jdbc:derby:mydb")
- username - *(optional)* JDBC username
- password - *(optional)* JDBC password


SalesForce Connector Commands:
------------------------------

`GenerateSfdcConfig(name="...", configFile="...", query="...", username="...", password="...", token="...");` - generate an XML config file based on the fields from your SFDC query.
- name       - the new dataset name
- configFile - path to configuration file
- query      - SOQL query (e.g. "SELECT Id, Name FROM Account"), see http://www.salesforce.com/us/developer/docs/api/Content/data_model.htm
- username   - SFDC username
- password   - SFDC password
- token      - SFDC security token (you may append the security token to the password instead using this parameter)
- partnerId  - SFDC client ID (partner token) that allows extended access to the SalesForce API
  
`UseSfdc(configFile="...", query="...", username="...", password="...", token="...");` - load SalesForce data file using config file describing the file structure, must call `CreateProject` or `OpenProject` before
- configFile - path to configuration file (will be overwritten)
- query      - SOQL query (e.g. "SELECT Id, Name FROM Account"), see http://www.salesforce.com/us/developer/docs/api/Content/data_model.htm
- username   - SFDC username
- password   - SFDC password
- token      - SFDC security token (you may append the security token to the password instead using this parameter)
- partnerId  - SFDC client ID (partner token) that allows extended access to the SalesForce API


MS CRM 2011 Online Connector Commands:
--------------------------------------

`UseMsCrm(configFile="...", username="...", password="...", host="...", org="...", entity="...", fields="...");` - load MS CRM 2011 data file using config file describing the file structure, must call `CreateProject` or `OpenProject` before
- configFile - path to configuration file (will be overwritten)
- username   - MS CRM username
- password   - MS CRM password
- host     - MS CRM server hostname
- org        - MS CRM organization name
- entity     - MS CRM entity name (e.g. account, opportunity etc.)
- fields     - MS CRM entity fields (e.g. accountid, name etc.)

Sugar CRM Connector Commands:
-----------------------------

`UseSugarCrm(configFile="...", username="...", password="...", host="...", entity="...", fields="...");` - load Sugar CRM data file using config file describing the file structure, must call `CreateProject` or `OpenProject` before
- configFile - path to configuration file (will be overwritten)
- username   - Sugar CRM username
- password   - Sugar CRM password
- host       - Sugar CRM server hostname
- entity     - Sugar CRM entity name (e.g. account, opportunity etc.)
- fields     - Sugar CRM entity fields (e.g. id, name etc.)

Chargify Commands:
------------------

`UseChargify(configFile="...", apiKey="...", domain="...", entity="...", fields="...");` - load Chargify data file using config file describing the file structure, must call `CreateProject` or `OpenProject` before
- configFile - path to configuration file (will be overwritten)
- apiKey     - Chargify API key
- domain     - Chargify domain
- entity     - Chargify entity name (e.g. products, subscriptions etc.)
- fields     - Chargify entity fields (e.g. id, name etc.)

Pivotal Tracker Connector Commands:
------------------------------

`UsePivotalTracker(username="...", password="...", pivotalProjectId="...", storyConfigFile="...", labelConfigFile="...", labelToStoryConfigFile="...", snapshotConfigFile="...");` - downloads and transforms the PT data.
- username               - PT username
- password               - PT password
- pivotalProjectId       - PT project ID (integer)
- storyConfigFile        - PT stories XML schema config
- labelConfigFile        - PT labels XML schema config
- labelToStoryConfigFile - PT labels to stories XML schema config
- snapshotConfigFile     - PT snapshots XML schema config 

Time Dimension Connector Commands:
----------------------------------

`UseDateDimension(name="...", includeTime = "...", type = "...");`  - load new time dimension into the project, must call `CreateProject` or `OpenProject` before
- name        - the time dimension name differentiates the time dimension form others. This is typically something like "closed", "created" etc.
- includeTime - generate the time dimension (true | false)
- type - specifies the name of a particular fiscal date dimension
