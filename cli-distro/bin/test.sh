#!/bin/sh
#
# Simple integration test - just exectures all examples from cli-distro module
 
set -e

tmp=`mktemp -t gdi-int-XXXXXX`

function drop() {
    pid="$1"
    echo "OpenProject(id = \"$pid\");" > "$tmp"
    echo "DropProject(id = \"$pid\");" >> "$tmp"
    bin/gdi.sh "$tmp"
}

echo 'Running quotes demo'
bin/gdi.sh examples/quotes/quotes.txt

echo 'Updating the data set in the quotes demo'
echo "RetrieveProject(fileName = \"examples/quotes/pid\");" > "$tmp"
grep '^\(UseCsv\|Transfer\)' examples/quotes/quotes.txt >> "$tmp"
bin/gdi.sh "$tmp"

echo 'Altering the server-side model (-2 attributes -1 fact +1 fact)'
bin/gdi.sh examples/quotes/alter.txt

echo 'Dropping the quotes project and snapshots'
drop "`cat examples/quotes/pid`"
echo 'Running the HR demo'
bin/gdi.sh examples/hr/1-department.txt
bin/gdi.sh examples/hr/2-employee.txt
bin/gdi.sh examples/hr/test_generate_update_maql.txt
bin/gdi.sh examples/hr/3-salary.txt

echo 'Updating the employee dataset'
echo "RetrieveProject(fileName = \"examples/hr/pid\");" > "$tmp"
grep '^\(UseCsv\|Transfer\)' examples/hr/2-employee.txt >> "$tmp"
bin/gdi.sh "$tmp"

echo 'Dropping the HR project and snapshots'
drop "`cat examples/hr/pid`"

echo 'Running sfdc demo'
bin/gdi.sh examples/sfdc/sfdc.txt

echo 'Dropping the sfdc project and snapshots'
drop "`cat examples/sfdc/pid`"                                   c

echo 'Running jdbc demo'
bin/gdi.sh examples/jdbc/fundamentals.txt

echo 'Dropping the jdbc project and snapshots'
drop "`cat examples/jdbc/pid`"

echo 'Running ga demo'
bin/gdi.sh examples/ga/cmd.create.txt

echo 'Dropping the ga project and snapshots'
drop "`cat examples/ga/pid`"

echo 'Running pt demo'
bin/gdi.sh examples/pt/cmd.txt

echo 'Dropping the pt project and snapshots'
drop "`cat examples/pt/pid`"

echo 'Running forex demo'
bin/gdi.sh examples/forex/cmd.txt

echo 'Dropping the forex project and snapshots'
drop "`cat examples/forex/pid`"

echo 'Running salesforce sales demo'
bin/gdi.sh examples/sales/cmd.txt

echo 'Dropping the salesforce sales project and snapshots'
drop "`cat examples/sales/pid`"

echo 'Running naming test'
bin/gdi.sh tests/naming/cmd.txt

echo 'Dropping the naming test project and snapshots'
drop "`cat tests/naming/pid`"

echo 'Running separators test'
bin/gdi.sh tests/separators/cmd.txt

echo 'Dropping the separators test project and snapshots'
drop "`cat tests/separators/pid`"

echo 'Running escapes test'
bin/gdi.sh tests/escapes/cmd.txt

echo 'Dropping the escapes test project and snapshots'
drop "`cat tests/escapes/pid`"

echo 'Running drop_snapshots test'
bin/gdi.sh tests/drop_snapshots/cmd.txt

echo 'Dropping the drop_snapshots test project and snapshots'
drop "`cat tests/drop_snapshots/pid`"

echo 'Running empty_lines test'
bin/gdi.sh tests/empty_lines/cmd.txt

echo 'Dropping the empty_lines test project and snapshots'
drop "`cat tests/empty_lines/pid`"

echo 'Running empty_dates test'
bin/gdi.sh tests/empty_dates/cmd.txt

echo 'Dropping the empty_dates test project and snapshots'
drop "`cat tests/empty_dates/pid`"

