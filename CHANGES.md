# GoodData CL Change Log

## To be released

* Fixed invalid "odd quote character" error if the 4096th character is an escaped double-quote + test added 
* Non-closed quotes in a CSV file were silently accepted, the tool returns an error now

## 1.1.5-beta

* Fixed CSV parsing bugs with the <comma><tree-double-quotes> sequence and with missing end lines. 
* Respect the TMPDIR environment variable instead of blindly using /tmp (Unix only)
* Google Analytics Connector: Handle "(others)" value of ga:date gracefully
* Documentation improvements 

## 1.1.4-beta

* Several CSV escaping issues fixed by replacing the CSV parser
* Fixed "table deleted_ids exists" error when the tool was re-run after a certain type of interruption
* CSV connector accepts tabulator as a separator character (use "\t" separator in the LoadCsv statement)
* Fixed ExecuteReports command
* HTTP proxy can be specified by -Dhttp.proxyHost and -Dhttp.proxyPort command line options (originally ignored by the underlying Jakarta HTTP client)
