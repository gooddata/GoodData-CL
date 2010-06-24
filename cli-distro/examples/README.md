# GoodData DI Examples

The examples demonstrate how to use the GoodData DI tool to fetch one or more sets of data for analysis from various sources and load them into GoodData.

When speaking of a data set, we understand a collection of records of the some structure, something that can be presented as a simple spreadsheet, a CSV file or a de-normalized RDBM table.

The examples are as follows:

1. CSV connector examples:

    - [Quotes CSV](quotes#readme) - loads a simple CSV file with stock quote data. 
    - [HR CSV](hr#readme) - loads three separate CSV datasets of Deparments, Employees and Paid Salaries and connect them to enable reports such as _'Total paid amount by department'_ etc.

1. JDBC connector example: 

    - [Fundamentals JDBC](jdbc#readme) - shows how to connect an existing database accessible via JDBC and create GoodData data sets from the results of `SELECT` SQL statements

1. External data connectors:

    - [Google Analytics](ga#readme) - loads your Google Analytics data to GoodData. You need a valid Google Analytics account. Note Google Analytics does not work with Google Apps accounts
    - [SalesForce](sfdc#readme) - loads some fields of Opportunity and Account from your SalesForce into GoodData

Feel free to start building your own integration scenarios on the top of these examples. How about fetching more metrics from GoogleAnalytics or more SalesForce modules? How about mashing up your SalesForce or GoogleAnalytics data with your internal systems? Please share your experience at the [GoodData Developer Forum](http://support.gooddata.com/forums/176660-developer-forum).
