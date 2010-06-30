# Executing All Reports in a GoodData Project

This example enumerates and executes all reports in a selected project. The execution creates caches in the project.
Once the caches are created the subsequent execution of the reports is significantly faster. You have to re-execute this
after every data load.

Please make sure that the project ID in the `report_exec.txt` script references the project where you want to perform
the reports execution.

You can find out your project ID by logging to GoodData, visiting
`https://secure.gooddata.com/gdc/md`, and clicking on the project link. The long hash on the top of the page is your
project ID.

Run `gdi.sh` with the `report_exec.txt` script as follows:

_Windows:_

        c:> bin\gdi.bat -u <username> -p <password> examples\report_exec\report_exec.txt

_Unix like OS (Linux, Mac OS X and others):_

        $ ./bin/gdi.sh -u <username> -p <password> examples/report_exec/report_exec.txt
