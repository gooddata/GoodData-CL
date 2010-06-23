# GoodData DI Examples

The examples demonstrate how to use the GoodData DI tool to fetch one or more sets of data for analysis from various sources and load them into GoodData.

When speaking of a data set, we understand a collection of records of the some structure, something that can be presented as a simple spreadsheet, a CSV file or a de-normalized RDBM table.

The examples are as follows:

1. CSV connector examples:

    - [Quotes CSV](quotes/) - loads a simple CSV file with stock quote data. 
    - [HR CSV](hr/) - loads three separate CSV datasets of Deparments, Employees and Paid Salaries and connect them to enable reports such as _'Total paid amount by department'_ etc.

1. JDBC connector example: 

    - [Fundamentals JDBC](jdbc/) - shows how to connect an existing database accessible via JDBC and create GoodData data sets from the results of `SELECT` SQL statements

1. External data connectors:

    - [Google Analytics](ga/) - loads your Google Analytics data to GoodData. You need a valid Google Analytics account. Note Google Analytics does not work with Google Apps accounts
    - [SalesForce](sfdc/) - loads some fields of Opportunity and Account from your SalesForce into GoodData

