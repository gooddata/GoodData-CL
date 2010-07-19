# GoodData CL Commands

GoodData Cl supports following groups of commands:

 * **Project Management Commands**: create (`CreateProject`), drop (`DropProject`) or open (`OpenProject`) a project identified by a project id. You can also save a project id to a file (`StoreProject`) in order to retrieve (`RetrieveProject`) it in another command script.
 * **Connector Commands** that either generate the [XML configuration](http://developer.gooddata.com/gooddata-cl/xml-config.html) (`Generate<Source-Type>Config`) for a specific data source and load the  source data (`Load<Source-Type>`). Connector commands require a project to be activated via a project management command before they are invoked.
 * **Logical Model Management Commands** generate (`GenerateMaql`) and execute (`ExecuteMaql`) the [MAQL DDL](http://developer.gooddata.com/api/maql-ddl.html) script for a connector that has been previously loaded via the `Load<Source-Type>` command.
 * **Data Transfer Commands** that transform, and transfer the data from a previously loaded (`Load<Source-Type>`) connector. All the data that are transferred are accumulated in a local database (Derby SQL or MySQL) as so called snapshots. You can decide to transfer all snapshots (`TransferAllSnapshots`), any snapshot (`TransferSnapshots`) or the last snapshot (`TransferLastSnapshot`).

## Project Initialization Workflow
Usually you want to initialize your project with following commands:

1. `CreateProject` or `OpenProject`. 
2. Optionally you generate [XML configuration](http://developer.gooddata.com/gooddata-cl/xml-config.html) for your data source using a `Generate<Source-Type>Config` command that yields an XML configuration file. This file describes your data structure and a way how the GoodData Logical Data Model is going to be generated. Sometimes you might want to review the XML config file and perform some changes. You'll most probably want to comment out the `Generate<Source-Type>Config` after the first run. 
3. Initialize your data source Connector using a `Load<Source-Type>` command. The `Load<Source-Type>` command requires the XML config file and a specific parameters that define the data source data or query (e.g. a SQL query).
4. Generate and execute [MAQL DDL](http://developer.gooddata.com/api/maql-ddl.html) for your data source using the `GenerateMaql` and `ExecuteMaql` commands. The [MAQL DDL](http://developer.gooddata.com/api/maql-ddl.html) generates your project's Logical Data Model (LDM) and Data Loading Interface (DLI). The DLI is later used by the following Data Transfer commands. You need to generate your LDM and DLI only once per each project. That is why the scripts that transfer data on regular basis don't use the the `GenerateMaql` and `ExecuteMaql` commands.
5. `TransferAllSnapshots` or `TransferLastSnapshot` commands that transform, package, and transfer the data.

## Project Data Loading Workflow
The ongoing data loading scripts usually:

1. `OpenProject` or `RetrieveProject` command.
2. Initialize your data source Connector using a `Load<Source-Type>` command. The `Load<Source-Type>` command requires the XML config file and a specific parameters that define the data source data or query (e.g. a SQL query).
3. `TransferAllSnapshots` or `TransferLastSnapshot` commands that transform, package, and transfer the data.

# Commands Reference

The following paragraphs describe the specific GoodData CL commands. 
 
## Project Management Commands

* `CreateProject`(name=&lt;project-name&gt;, desc=&lt;description&gt;) - create a new project on the &lt;hostname&gt; server
  - project-name - name of the new project
  - description  - (optional) project description

* `DropProject`(id=&lt;project-id&gt;) - drop the project on the &lt;hostname&gt; server

  - project-id - project id

* `OpenProject`(id=&lt;identifier&gt;) - open an existing project for data modeling and data upload. Equivalent to providing the project identifier using the "-e" command line option.

  - identifier - id of an existing project (takes the form of an MD5 hash)

* `StoreProject`(fileName=&lt;file&gt;) - saves the current project identifier into the specified file

  - fileName - file to save the project identifier

* `RetrieveProject`(fileName=&lt;file&gt;) - loads the current project identifier from the specified file

  - fileName - file to load the project identifier from

* `InviteUser`(email=&lt;email&gt;, msg=&lt;msg&gt;) - invites a new user to the project

  - email - the invited user's e-mail
  - msg - optional invitation message

* `Lock`(path=&lt;file&gt;) - prevents concurrent run of multiple instances sharing the same lock file. Lock files older than 1 hour are discarded.

## Logical Model Management Commands

* `GenerateMaql`(maqlFile=&lt;maql&gt;) - generate MAQL DDL script describing data model from the local config file, must call `CreateProject` or `OpenProject` and a `LoadXXX` before

  - maqlFile - path to MAQL file (will be overwritten)

* `GenerateUpdateMaql`(maqlFile=&lt;maql&gt;) - generate MAQL DDL alter script that creates the columns available in the local configuration but missing in the remote GoodData project, must call `CreateProject` or OpenProject and `LoadXXX` before

  - maqlFile - path to MAQL file (will be overwritten)

* `ExecuteMaql`(maqlFile=&lt;maql&gt; \[, ifExists=&lt;true | false&gt;\]) - run MAQL DDL script on server to generate data model, must call `CreateProject` or `OpenProject` and `LoadXXX` before
  - maqlFile - path to the MAQL file (relative to PWD)
  - ifExists - if set to true the command quits silently if the maqlFile does not exist, default is false

## Data Transfer Commands

* `TransferAllSnapshots`(\[incremental=&lt;true | false&gt;\] \[, waitForFinish=&lt;true | false&gt;\]) - upload data (all snapshots) to the server, must call CreateProject or OpenProject and Load&lt;Connector&gt; before. Not allowed for data set defining a connection point unless only one snapshot is present.
  - incremental - incremental transfer (true | false), default is false
  - waitForFinish - waits for the server-side processing (true | false), default is true

* `TransferSnapshots`(firstSnapshot=snapshot-id, lastSnapshot=snapshot-id \[,incremental=&lt;true | false&gt;\] \[, waitForFinish=&lt;true | false&gt;\]) - uploads all snapshots between the firstSnapshot and the lastSnapshot (inclusive). Only one snapshot is allowed for data set defining a connection point.
  - firstSnapshot - the first transferred snapshot id
  - lastSnapshot - the last transferred snapshot id
  - incremental - incremental transfer (true | false), default is false
  - waitForFinish - waits for the server-side processing (true | false), default is true

* `TransferLastSnapshot`(\[incremental=&lt;true | false&gt;\] \[, waitForFinish=&lt;true | false&gt;\]) - uploads the lastSnapshot 
  - incremental - incremental transfer (true | false), default is false
  - waitForFinish - waits for the server-side processing (true | false), default is true

* `ListSnapshots`() - list all data snapshots from the internal DB, must call CreateProject or OpenProject before

* `DropSnapshots`() - drops the internal DB, must call CreateProject or OpenProject before

## CSV Connector Commands

* `GenerateCsvConfig`(csvHeaderFile=&lt;file&gt;, configFile=&lt;config&gt; \[, defaultLdmType=&lt;type&gt;\] \[, folder=&lt;folder&gt;\]) - generate a sample XML config file based on the fields from your CSV file. If the config file exists already, only new columns are added. The config file must be edited as the LDM types (attribute | fact | label etc.) are assigned randomly.
  - csvHeaderFile - path to CSV file (only the first header row will be used)
  - configFile  - path to configuration file (will be overwritten)
  - defaultLdmType - LDM type to be associated with new columns (only ATTRIBUTE type is supported by the ProcessNewColumns task at this time)
  - folder - folder where to place new attributes

* `LoadCsv`(csvDataFile=&lt;data&gt;, configFile=&lt;config&gt;, header=&lt;true | false&gt;) - load CSV data file using config file describing the file structure, must call CreateProject or OpenProject before
  - csvDataFile    - path to CSV datafile
  - configFile  - path to XML configuration file (see the GenerateCsvConfig command that generates the config file template)
  - header - true if the CSV file has header in the first row, false otherwise 

## GoogleAnalytics Connector Commands

* `GenerateGoogleAnalyticsConfig`(name=&lt;name&gt;, configFile=&lt;config&gt;, dimensions=&lt;pipe-separated-ga-dimensions&gt;, metrics=&lt;pipe-separated-ga-metrics&gt;) - generate an XML config file based on the fields from your GA query.
  - name - the new dataset name
  - configFile  - path to configuration file (will be overwritten)
  - dimensions - pipe (|) separated list of Google Analytics dimensions (see [GData Reference](http://code.google.com/apis/analytics/docs/gdata/gdataReferenceDimensionsMetrics.html))
  - metrics - pipe (|) separated list of Google Analytics metrics (see [GData Reference](http://code.google.com/apis/analytics/docs/gdata/gdataReferenceDimensionsMetrics.html))

* `LoadGoogleAnalytics`(configFile=&lt;config&gt;, username=&lt;ga-username&gt;, password=&lt;ga-password&gt;, profileId=&lt;ga-profile-id&gt;, dimensions=&lt;pipe-separated-ga-dimensions&gt;, metrics=&lt;pipe-separated-ga-metrics&gt;, startDate=&lt;date&gt;, endDate=&lt;date&gt;, filters=&lt;ga-filter-string&gt;)  - load GA data file using config file describing the file structure, must call CreateProject or OpenProject before
  - configFile  - path to configuration file (will be overwritten)
  - token - Google Analytics AuthSub token (you must specify either the token or username/password)
  - username - Google Analytics username (you must specify either the token or username/password)
  - password - Google Analytics password (you must specify either the token or username/password)
  - profileId - Google Analytics profile ID (this is a value of the id query parameter in the GA url)
  - dimensions - pipe (|) separated list of Google Analytics dimensions (see [GData Reference](http://code.google.com/apis/analytics/docs/gdata/gdataReferenceDimensionsMetrics.html))
  - metrics - pipe (|) separated list of Google Analytics metrics (see [GData Reference](http://code.google.com/apis/analytics/docs/gdata/gdataReferenceDimensionsMetrics.html))
  - startDate - the GA start date in the yyyy-mm-dd format
  - endDate - the GA end date in the yyyy-mm-dd format
  - filters - the GA filters (see [GData Documentation](http://code.google.com/apis/analytics/docs/gdata/gdataReferenceDataFeed.html#filters))

## JDBC Connector Commands

* `GenerateJdbcConfig`(name=&lt;name&gt;, configFile=&lt;config&gt;, driver=&lt;jdbc-driver&gt;, url=&lt;jdbc-url&gt;, query=&lt;sql-query&gt; \[, username=&lt;jdbc-username&gt;\] \[, password=&lt;jdbc-password&gt;\])  - generate an XML config file based on the fields from your JDBC query.
  - name - the new dataset name  
  - configFile  - path to configuration file (will be overwritten)  
  - driver - JDBC driver string (e.g. "org.apache.derby.jdbc.EmbeddedDriver"), you'll need to place the JAR with the JDBC driver to the lib subdirectory  
  - url - JDBC url (e.g. "jdbc:derby:mydb")  
  - query - SQL query (e.g. "SELECT employee,dept,salary FROM payroll")  
  - username - JDBC username  
  - password - JDBC password
  
* `LoadJdbc`(configFile=&lt;config&gt;, driver=&lt;jdbc-driver&gt;, url=&lt;jdbc-url&gt;, query=&lt;sql-query&gt; \[, username=&lt;jdbc-username&gt;\] \[, password=&lt;jdbc-password&gt;\])  - load JDBC data file using config file describing the file structure, must call CreateProject or OpenProject before  
  - configFile  - path to configuration file (will be overwritten)  
  - driver - JDBC driver string (e.g. "org.apache.derby.jdbc.EmbeddedDriver"), you'll need to place the JAR with the JDBC driver to the lib subdirectory  
  - url - JDBC url (e.g. "jdbc:derby:mydb")  
  - query - SQL query (e.g. "SELECT employee,dept,salary FROM payroll")  
  - username - JDBC username  
  - password - JDBC password

## SalesForce Connector Commands

* `GenerateSfdcConfig`(name=&lt;name&gt;, configFile=&lt;config&gt;, query=&lt;soql-query&gt;, username=&lt;sfdc-username&gt;, password=&lt;sfdc-password&gt;, token=&lt;sfdc-security-token&gt;)  - generate an XML config file based on the fields from your SFDC query.  
  - name - the new dataset name  
  - configFile  - path to configuration file (will be overwritten)  
  - query - SOQL query (e.g. "SELECT Id, Name FROM Account"), see [Salesforce API](http://www.salesforce.com/us/developer/docs/api/Content/data_model.htm)  
  - username - SFDC username  
  - password - SFDC password  
  - token - SFDC security token (you may append the security token to the password instead using this parameter)
  
* `LoadSfdc`(configFile=&lt;config&gt;, query=&lt;soql-query&gt;, username=&lt;sfdc-username&gt;, password=&lt;sfdc-password&gt;, token=&lt;sfdc-security-token&gt;)  - load SalesForce data file using config file describing the file structure, must call CreateProject or OpenProject before  
  - configFile  - path to configuration file (will be overwritten)  
  - query - SOQL query (e.g. "SELECT Id, Name FROM Account"), see [Salesforce API](http://www.salesforce.com/us/developer/docs/api/Content/data_model.htm)
  - username - SFDC username  
  - password - SFDC password  
  - token - SFDC security token (you may append the security token to the password instead using this parameter)
  

## Time Dimension Connector Commands

* `LoadDateDimension`(name=&lt;name&gt;)  - load new time dimension into the project, must call CreateProject or OpenProject before

  - name - the time dimension name differentiates the time dimension form others. This is typically something like "closed", "created" etc.
