#!/bin/sh
#
# Simple integration test - just exectures all examples from cli-distro module
 
set -e

tmp=`mktemp -t gdi-int-XXXXXX`

function drop() {
    load="$1"
    pid="$2"
    echo "OpenProject(id = \"$pid\");" > "$tmp"
    echo "$load" >> "$tmp"
    echo "DropSnapshots(id = \"$pid\");" >> "$tmp"
    bin/gdi.sh --backend "$backend" "$tmp"
}

for backend in MYSQL DERBY ; do
    echo 'Running quotes demo'
    bin/gdi.sh --backend "$backend" examples/quotes/quotes.cmd

    echo 'Updating the data set in the quotes demo'
    echo "RetrieveProject(fileName = \"examples/quotes/pid\");" > "$tmp"
    grep '^\(LoadCsv\|Transfer\)' examples/quotes/quotes.cmd >> "$tmp"   
    bin/gdi.sh --backend "$backend" "$tmp"

    echo 'Dropping the quotes project and snapshots'
    load=`grep ^LoadCsv "$tmp"`
    drop "$load" "`cat examples/quotes/pid`"

    echo 'Running the HR demo'
    bin/gdi.sh --backend "$backend" examples/hr/1-department.cmd
    bin/gdi.sh --backend "$backend" examples/hr/2-employee.cmd
    bin/gdi.sh --backend "$backend" examples/hr/3-salary.cmd

    echo 'Updating the employee dataset'
    echo "RetrieveProject(fileName = \"examples/hr/pid\");" > "$tmp"
    grep '^\(LoadCsv\|Transfer\)' examples/hr/2-employee.cmd >> "$tmp"
    bin/gdi.sh --backend "$backend" "$tmp"

    echo 'Dropping the HR project and snapshots'
    load=`grep ^LoadCsv "$tmp"`
    drop "$load" "`cat examples/hr/pid`"
done
