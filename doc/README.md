# GoodData CL Architecture

The GoodData CL framework. The framework wraps various GoodData HTTP APIs (authentication, provisioning, modeling, and dat loading) in convenient Java methods. Here is a simple diagram of the GoodData CL Framework:
 
<p><img src="http://github.com/gooddata/GoodData-CL/raw/master/doc/architecture.png" alt="GoodData CL Framework" title="GoodData CL Framework Architecture" width="100%"> </p>

The framework contains following components:

1. *CLI* the command line interface that processes a simple commands like `CreateProject`, `ExecuteMAQL`, `TransferLastSnapshot` etc. by translating them to the *Connector* function calls. The CLI also implements few generic commands (e.g. `CreateProject`, `DeleteProject` etc.)
2. *Connector* represents a specific data source (e.g. [SalesForce](http://developer.gooddata.com/gooddata-cl/examples/sfdc/), [GoogleAnalytics](http://developer.gooddata.com/gooddata-cl/examples/ga/), [SQL database](http://developer.gooddata.com/gooddata-cl/examples/jdbc/), [data file](http://developer.gooddata.com/gooddata-cl/examples/quotes/) etc.) that can be loaded to a GoodData project. The *Connector* wraps functions that:

	1. extract data from a particular source.
	2. generate an appropriate [MAQL DDL](http://developer.gooddata.com/api/maql-ddl.html) script
	3. create a project's Logical Data Model (LDM) via executing the [MAQL DDL](http://developer.gooddata.com/api/maql-ddl.html) script
	4. transform (normalize) the input data to fit the Data Loading Interfaces that have been generated from the [MAQL DDL](http://developer.gooddata.com/api/maql-ddl.html)
	5. package and transfer the data to the GoodData project.
	
3. *GdcRESTApiWrapper* that is a communication stub that wraps the GoodData HTTP API in Java. This class de-facto translates the Java calls to the invocations of the GoodData HTTP API. The *GdcRESTApiWrapper* returns few info structures that describe the GoodData project, Data Loading Interface (DLI), DLI parts etc.
4. *GdcFTPApiWrapper* that is a communication stub that wraps the GoodData FTP API in Java. This class takes care of the transfer of the data package to a secure private space on GoodData servers.
5. *Connector Backend* performs the data transformation. The backend is implemented in the Derby SQL (embedded, low performance) and MySQL (needs installation, improves performance) databases. The connector backends transform the incoming data to the [3NF](http://en.wikipedia.org/wiki/Third_normal_form)
6. *MAQLGenerator* generates the [MAQL DDL](http://developer.gooddata.com/api/maql-ddl.html) script that creates the GoodData LDM
