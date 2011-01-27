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

echo 'Running generate_csv_config test'
bin/gdi.sh tests/generate_csv_config/quotes.txt
facts=`grep FACT tests/generate_csv_config/quotes.config.xml | wc -l | sed 's,^ *,,'`
if [ "x$facts" != "x5" ] ; then
    echo "generate_csv_config test produced '$facts' FACT fields, 5 expected"
    exit 1
fi

