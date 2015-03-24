# GoodData CL Change Log

## 1.3.0

* Removed deprecated command line arguments for the FTP host and port. The WebDAV endpoint is discovered at runtime via the REST API.

## 1.1.9-beta

* Authentication handling updated to be forward compatible with API change in the upcoming
  GoodData platform release 41 (~ end of October)

Note:  By accident, the released build of 1.1.9-beta errorneously reports version 1.1.8, sorry 
  about that

## 1.1.8-beta

* Fixed error when using MySQL backend database running with binary logging turned on
* Fixed error loading data containing double-quote character from an external source

## 1.1.7-beta

* Fixed error handling CRLF sequences in CSV files. IMPORTANT: If you run GoodData CL 1.1.4 to 1.1.6 on Windows, you really need to upgrade!

## 1.1.6-beta

* Google Analytics Connector: support for incremental data loading with overlapped dates
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
