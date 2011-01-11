# GoodData CL Examples

The examples demonstrate how to use the GoodData CL tool to fetch one or more sets of data for analysis from various sources and load them into GoodData.

For our purposes, "data sets" are a collection of records of the some structure, something that can be presented as a simple spreadsheet, a CSV file or a de-normalized RDBM table.

Each of the examples performs some or all of the steps as follows:

1. Create a GoodData project or open a previously created project. A project in GoodData is a private workspace that includes the data you load, the metrics you define, the reports and dashboards you create and the users you invite.
1. If some of your data sets contain time-based data, create one or more date dimensions as necessary. A date dimension contains a set of date attributes to break down your numbers, such as date, month, quarter, year etc.
1. Describe your datasets and create corresponding models in your GoodData project. The description involves information about which fields can be aggregated (_facts_) and by which the aggregated numbers can be broken down (_attributes_), how to connect multiple data sets, and so on. 
1. Populate your datasets with data from various sources. The example covers loading data from CSV files, Google Analytics and Salesforce web services, and from a relational database accessible via a JDBC-4 interface.

**The examples are as follows:**

1. CSV connector examples:

    - [Quotes](./quotes/#readme) - loads a simple CSV file of daily stock quotes from AMEX, NYSE and NASDAQ in 2008
    - [Human Resources](./hr/#readme) - loads three separate CSV data sets of Deparments, Employees and Paid Salaries and connect them to enable reports such as _'Total paid amount by department'_ etc.

2. JDBC connector example: 

    - [Fundamentals](./jdbc/#readme) - shows how to connect an existing database accessible via JDBC and create GoodData data sets from the results of `SELECT` SQL statements

3. External data connectors:

    - [Google Analytics](./ga/#readme) - loads your Google Analytics data to GoodData. You need a valid Google Analytics account. Note: Google Analytics does not work with Google Apps accounts
    - [Salesforce.com](./sfdc/#readme) - loads some fields from Opportunity and Account within your Salesforce.com into GoodData

4. Other features:

    - [Forex](./forex/#readme) - shows built-in support for working with date and time data

Feel free to start building your own integration scenarios on the top of these examples. How about fetching more metrics from GoogleAnalytics or more Salesforce modules? How about mashing up your Salesforce or GoogleAnalytics data with your internal systems? Please share your experience at the [GoodData Developer Forum](http://support.gooddata.com/forums/176660-developer-forum).
