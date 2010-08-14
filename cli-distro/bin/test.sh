#!/bin/sh
#
# Simple integration test - just exectures all examples from cli-distro module
 
set -e

tmp=`mktemp -t gdi-int-XXXXXX`

function drop() {
    pid="$1"
    echo "OpenProject(id = \"$pid\");" > "$tmp"
    echo "DropIntegrationDatabase(); DropProject(id = \"$pid\");" >> "$tmp"
    bin/gdi.sh --backend "$backend" "$tmp"
}

for backend in MYSQL DERBY ; do
    echo 'Running quotes demo'
    bin/gdi.sh --backend "$backend" examples/quotes/quotes.txt

    echo 'Updating the data set in the quotes demo'
    echo "RetrieveProject(fileName = \"examples/quotes/pid\");" > "$tmp"
    grep '^\(LoadCsv\|Transfer\)' examples/quotes/quotes.txt >> "$tmp"
    bin/gdi.sh --backend "$backend" "$tmp"

    echo 'Dropping the quotes project and snapshots'
    drop "`cat examples/quotes/pid`"

    echo 'Running the HR demo'
    bin/gdi.sh --backend "$backend" examples/hr/1-department.txt
    bin/gdi.sh --backend "$backend" examples/hr/2-employee.txt
    bin/gdi.sh --backend "$backend" examples/hr/3-salary.txt

    echo 'Updating the employee dataset'
    echo "RetrieveProject(fileName = \"examples/hr/pid\");" > "$tmp"
    grep '^\(LoadCsv\|Transfer\)' examples/hr/2-employee.txt >> "$tmp"
    bin/gdi.sh --backend "$backend" "$tmp"

    echo 'Dropping the HR project and snapshots'
    drop "`cat examples/hr/pid`"

    echo 'Running sfdc demo'
    bin/gdi.sh --backend "$backend" examples/sfdc/sfdc.txt

    echo 'Dropping the sfdc project and snapshots'
    drop "`cat examples/sfdc/pid`"

    echo 'Running jdbc demo'
    bin/gdi.sh --backend "$backend" examples/jdbc/fundamentals.txt

    echo 'Dropping the jdbc project and snapshots'
    drop "`cat examples/jdbc/pid`"

    echo 'Running ga demo'
    bin/gdi.sh --backend "$backend" examples/ga/ga.txt

    echo 'Dropping the ga project and snapshots'
    drop "`cat examples/ga/pid`"

    echo 'Running naming test'
    bin/gdi.sh --backend "$backend" tests/naming/cmd.txt

    echo 'Dropping the naming test project and snapshots'
    drop "`cat tests/naming/pid`"

    echo 'Running empty_lines test'
    bin/gdi.sh --backend "$backend" tests/empty_lines/cmd.txt

    echo 'Dropping the empty_lines test project and snapshots'
    drop "`cat tests/empty_lines/pid`"

    echo 'Running drop_snapshots test'
    bin/gdi.sh --backend "$backend" tests/drop_snapshots/cmd.txt

    echo 'Dropping the drop_snapshots test project and snapshots'
    drop "`cat tests/drop_snapshots/pid`"

done