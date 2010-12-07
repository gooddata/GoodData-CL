# GoodData CL Commands

GoodData Cl supports following groups of commands:

* **[Project Management Commands](#project_management_commands)**: create/delete/use/remember a project
  `CreateProject`, `DeleteProject`, `OpenProject`, `RememberProject`. `UseProject`

* **[Connector Commands](#connector_commands)** that either generate the [XML configuration](http://developer.gooddata.com/gooddata-cl/xml-config.html) (`Generate<Source-Type>Config`) for a specific data source and load the  source data (`Load<Source-Type>`). Connector commands require a project to be activated via a project management command before they are invoked.

* **[Metadata Management Commands](#metadata_management_commands)**: work with project metadata (reports, dashboards, metrics, folders)  
  `RetrieveMetadataObject`, `StoreMetadataObject`, `DropMetadataObject`, `RetrieveAllObjects`, `StoreAllObjects`

* **[Logical Model Management Commands](#logical_model_management_commands)** generate & execute [MAQL DDL](http://developer.gooddata.com/api/maql-ddl.html) script for a connector that has been previously loaded via the `Load<Source-Type>` command.  
  `GenerateMaql`, `GenerateUpdateMaql`, `ExecuteMaql`

* **[Data Transfer Commands](#data_transfer_commands)** that transform, and transfer the data from a previously loaded (`Load<Source-Type>`) connector.  
  `TransferAllSnapshots`, `TransferSnapshots`, `TransferLastSnapshot`, `ListSnapshots`, `DropSnapshots`

## Project Initialization Workflow
Usually you want to initialize your project with following commands:

1. `CreateProject` or `OpenProject`. 
2. Optionally you generate [XML configuration](http://developer.gooddata.com/gooddata-cl/xml-config.html) for your data source using a `Generate<Source-Type>Config` command that yields an XML configuration file. This file describes your data structure and a way how the GoodData Logical Data Model is going to be generated. Sometimes you might want to review the XML config file and perform some changes. You'll most probably want to comment out the `Generate<Source-Type>Config` after the first run.
3. Initialize your data source Connector using a `Load<Source-Type>` command. The `Load<Source-Type>` command requires the XML config file and a specific parameters that define the data source data or query (e.g. a SQL query).
4. Generate and execute [MAQL DDL](http://developer.gooddata.com/api/maql-ddl.html) for your data source using the `GenerateMaql` and `ExecuteMaql` commands. The [MAQL DDL](http://developer.gooddata.com/api/maql-ddl.html) generates your project's Logical Data Model (LDM) and Data Loading Interface (DLI). The DLI is later used by the following Data Transfer commands. You need to generate your LDM and DLI only once per each project. That is why the scripts that transfer data on regular basis don't use the the `GenerateMaql` and `ExecuteMaql` commands.
5. `TransferAllSnapshots` or `TransferLastSnapshot` commands that transform, package, and transfer the data.

## Project Data Loading Workflow
The ongoing data loading scripts usually:

1. `OpenProject` or `UseProject` command.
2. Initialize your data source Connector using a `Load<Source-Type>` command. The `Load<Source-Type>` command requires the XML config file and a specific parameters that define the data source data or query (e.g. a SQL query).
3. `TransferAllSnapshots` or `TransferLastSnapshot` commands that transform, package, and transfer the data.

# Commands Reference

The following paragraphs describe the specific GoodData CL commands. 
 
## Project Management Commands

* `CreateProject`(name=&lt;project-name&gt;, desc=&lt;description&gt;, templateUri=&lt;templateUri&gt;) - create a new project on the &lt;hostname&gt; server
  - project-name - name of the new project
  - description  - (optional) project description
  - templateUri - (optional) project template to create the project from


* `DeleteProject`(id=&lt;project-id&gt;) - drop the project on the &lt;hostname&gt; server

  - project-id - optional project id, if not specified, the command tries to drop the current project

* `OpenProject`(id=&lt;identifier&gt;) - open an existing project for data modeling and data upload. Equivalent to providing the project identifier using the "-e" command line option.

  - identifier - id of an existing project (takes the form of an com.gooddata.MD5 hash)

* `RememberProject`(fileName=&lt;file&gt;) - saves the current project identifier into the specified file

  - fileName - file to save the project identifier

* `UseProject`(fileName=&lt;file&gt;) - loads the current project identifier from the specified file

  - fileName - file to load the project identifier from

* `InviteUser`(email=&lt;email&gt;, msg=&lt;msg&gt;\[, role=&lt;admin|editor|dashboard only&gt;\]) - invites a new user to the project

  - email - the invited user's e-mail
  - msg - optional invitation message
  - role - optional initial user's role

* `Lock`(path=&lt;file&gt;) - prevents concurrent run of multiple instances sharing the same lock file. Lock files older than 1 hour are discarded.

## Metadata Management Commands

* `RetrieveMetadataObject`(id=&lt;object-id&gt;, file=&lt;file-to-store-the-object&gt;) - retrieves a metadata object and stores it in a file, must call CreateProject or OpenProject before
  - object-id - valid object id (integer number)
  - file - file where the object content (JSON) is going to be stored

* `StoreMetadataObject`(\[id=&lt;object-id&gt;,\] file=&lt;file-with-the-object-content&gt;) - stores a metadata object with a content (JSON) in file to the metadata server, must call CreateProject or OpenProject before
  - object-id - valid object id (integer number), if the id is specified, the object is going to be modified, if not, a new object is created
  - file - file where the object content (JSON) is stored

* `DropMetadataObject`(id=&lt;object-id&gt;) - drops the object with specified id from the project's metadata, must call CreateProject or OpenProject before
  - object-id - valid object id (integer number)

* `RetrieveAllObjects`(dir=&lt;directory&gt;) - download all metadata objects (reports, dashboards, metrics, folders) and store them locally in the specified directory
  - directory - an existing directory which CL tool will use to write store object files

* `CopyObjects`(dir=&lt;directory&gt;, overwrite=&lt;true | false&gt;) - load a whole directory of objects (from `RetrieveAllObjects`) into an existing project
  - directory - a directory with object files
  - overwrite - overwrite existing objects (if conflicts are discovered)


## Logical Model Management Commands

* `GenerateMaql`(maqlFile=&lt;maql&gt;) - generate MAQL DDL script describing data model from the local config file, must call `CreateProject` or `OpenProject` and a `UseXXX` before

  - maqlFile - path to MAQL file (will be overwritten)

* `GenerateUpdateMaql`(maqlFile=&lt;maql&gt;) - generate MAQL DDL alter script that creates the columns available in the local configuration but missing in the remote GoodData project, must call `CreateProject` or OpenProject and `UseXXX` before

  - maqlFile - path to MAQL file (will be overwritten)

* `ExecuteMaql`(maqlFile=&lt;maql&gt; \[, ifExists=&lt;true | false&gt;\]) - run MAQL DDL script on server to generate data model, must call `CreateProject` or `OpenProject` and `UseXXX` before
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


# Connector Commands

## CSV Connector Commands

* `GenerateCsvConfig`(csvHeaderFile=&lt;file&gt;, configFile=&lt;config&gt; \[, defaultLdmType=&lt;mode&gt;\] \[, folder=&lt;folder&gt;\], \[separator = &lt;separator-char&gt;\]) - generate a sample XML config file based on the fields from your CSV file. If the config file exists already, only new columns are added. The config file must be edited as the LDM types (attribute | fact | label etc.) are assigned randomly.
  - csvHeaderFile - path to CSV file (only the first header row will be used)
  - configFile  - path to configuration file (will be overwritten)
  - defaultLdmType - LDM mode to be associated with new columns (only ATTRIBUTE mode is supported by the ProcessNewColumns task at this time)
  - folder - folder where to place new attributes
  - separator - optional field separator, the default is ','

* `UseCsv`(csvDataFile=&lt;data&gt;, configFile=&lt;config&gt;, header=&lt;true | false&gt;, \[separator = &lt;separator-char&gt;\]) - load CSV data file using config file describing the file structure, must call CreateProject or OpenProject before
  - csvDataFile    - path to CSV datafile
  - configFile  - path to XML configuration file (see the GenerateCsvConfig command that generates the config file template)
  - header - true if the CSV file has header in the first row, false otherwise
  - separator - optional field separator, the default is ','

## GoogleAnalytics Connector Commands

* `GenerateGoogleAnalyticsConfig`(name=&lt;name&gt;, configFile=&lt;config&gt;, dimensions=&lt;pipe-separated-ga-dimensions&gt;, metrics=&lt;pipe-separated-ga-metrics&gt;) - generate an XML config file based on the fields from your GA query.
  - name - the new dataset name
  - configFile  - path to configuration file (will be overwritten)
  - dimensions - pipe (|) separated list of Google Analytics dimensions (see [GData Reference](http://code.google.com/apis/analytics/docs/gdata/gdataReferenceDimensionsMetrics.html))
  - metrics - pipe (|) separated list of Google Analytics metrics (see [GData Reference](http://code.google.com/apis/analytics/docs/gdata/gdataReferenceDimensionsMetrics.html))

* `UseGoogleAnalytics`(configFile=&lt;config&gt;, username=&lt;ga-username&gt;, password=&lt;ga-password&gt;, profileId=&lt;ga-profile-id&gt;, dimensions=&lt;pipe-separated-ga-dimensions&gt;, metrics=&lt;pipe-separated-ga-metrics&gt;, startDate=&lt;date&gt;, endDate=&lt;date&gt;, filters=&lt;ga-filter-string&gt;)  - load GA data file using config file describing the file structure, must call CreateProject or OpenProject before
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
  
* `UseJdbc`(configFile=&lt;config&gt;, driver=&lt;jdbc-driver&gt;, url=&lt;jdbc-url&gt;, query=&lt;sql-query&gt; \[, username=&lt;jdbc-username&gt;\] \[, password=&lt;jdbc-password&gt;\])  - load JDBC data file using config file describing the file structure, must call CreateProject or OpenProject before
  - configFile  - path to configuration file (will be overwritten)  
  - driver - JDBC driver string (e.g. "org.apache.derby.jdbc.EmbeddedDriver"), you'll need to place the JAR with the JDBC driver to the lib subdirectory  
  - url - JDBC url (e.g. "jdbc:derby:mydb")  
  - query - SQL query (e.g. "SELECT employee,dept,salary FROM payroll")  
  - username - JDBC username  
  - password - JDBC password

* `ExportJdbcToCsv`(dir=&lt;dir&gt;, driver=&lt;jdbc-driver&gt;, url=&lt;jdbc-url&gt; \[, username=&lt;jdbc-username&gt;\] \[, password=&lt;jdbc-password&gt;\])  - exports all tables from the database to CSV file
  - dir - target directory
  - driver - JDBC driver string (e.g. "org.apache.derby.jdbc.EmbeddedDriver"), you'll need to place the JAR with the JDBC driver to the lib subdirectory
  - url - JDBC url (e.g. "jdbc:derby:mydb")
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
  
* `UseSfdc`(configFile=&lt;config&gt;, query=&lt;soql-query&gt;, username=&lt;sfdc-username&gt;, password=&lt;sfdc-password&gt;, token=&lt;sfdc-security-token&gt;)  - load SalesForce data file using config file describing the file structure, must call CreateProject or OpenProject before
  - configFile  - path to configuration file (will be overwritten)  
  - query - SOQL query (e.g. "SELECT Id, Name FROM Account"), see [Salesforce API](http://www.salesforce.com/us/developer/docs/api/Content/data_model.htm)
  - username - SFDC username  
  - password - SFDC password  
  - token - SFDC security token (you may append the security token to the password instead using this parameter)
  

## Time Dimension Connector Commands

* `UseDateDimension`(name=&lt;name&gt;)  - load new time dimension into the project, must call CreateProject or OpenProject before

  - name - the time dimension name differentiates the time dimension form others. This is typically something like "closed", "created" etc.
