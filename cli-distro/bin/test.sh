#!/bin/sh
#
# Simple integration test - just exectures all examples from cli-distro module
 
set -e

tmp=`mktemp -t gdi-int-XXXXXX`

function drop() {
    pid="$1"
    echo "OpenProject(id = \"$pid\");" > "$tmp"
    echo "DeleteProject(id = \"$pid\");" >> "$tmp"
    bin/gdi.sh "$tmp"
}

echo 'Running quotes_alter demo'
bin/gdi.sh examples/quotes_alter/quotes.txt

echo 'Updating the data set in the quotes_alter demo'
echo "UseProject(fileName = \"examples/quotes_alter/pid\");" > "$tmp"
grep '^\(UseCsv\|Transfer\)' examples/quotes_alter/quotes.txt >> "$tmp"
bin/gdi.sh "$tmp"

echo 'Altering the server-side model (-2 attributes -1 fact +1 fact)'
bin/gdi.sh examples/quotes_alter/alter.txt

echo 'Dropping the quotes_alter project and snapshots'
drop "`cat examples/quotes_alter/pid`"
echo 'Running the HR demo'
bin/gdi.sh examples/hr/1-department.txt
bin/gdi.sh examples/hr/2-employee.txt
bin/gdi.sh examples/hr/test_generate_update_maql.txt
bin/gdi.sh examples/hr/3-salary.txt

echo 'Updating the employee dataset'
echo "UseProject(fileName = \"examples/hr/pid\");" > "$tmp"
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
bin/gdi.sh examples/forex/forex.txt

echo 'Dropping the forex project and snapshots'
drop "`cat examples/forex/pid`"

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

echo 'Running reconnect_date test'
bin/gdi.sh tests/reconnect_date/cmd.txt

echo 'Dropping the reconnect_date test project and snapshots'
drop "`cat tests/reconnect_date/pid`"

echo 'Running empty_dates test'
bin/gdi.sh tests/empty_dates/cmd.txt

echo 'Dropping the empty_dates test project and snapshots'
drop "`cat tests/empty_dates/pid`"

echo 'Running empty_lines test'
bin/gdi.sh tests/empty_lines/cmd.txt

echo 'Dropping the empty_lines test project and snapshots'
drop "`cat tests/empty_lines/pid`"

echo 'Running gum_cp_label test'
bin/gdi.sh tests/gum_cp_label/cmd.txt

echo 'Dropping the gum_cp_label test'
drop "`cat tests/gum_cp_label/pid`"

echo 'Running gum_label_nocp test'
bin/gdi.sh tests/gum_label_nocp/cmd.txt

echo 'Dropping the gum_label_nocp test'
drop "`cat tests/gum_label_nocp/pid`"
