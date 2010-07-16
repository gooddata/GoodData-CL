#!/bin/sh

set -e

dbdir="$1"
password="$2"

if [ ! "$dbdir" ] ; then
    echo "Usage: $0 <database directory> [<mysql root password>]" >&2
    exit 1
fi

count=0
tmp=`mktemp`

mysql -uroot --password="$password" \
    -e 'show databases where length(`Database`) = 32' \
    | grep -v ^Database > "$tmp"

while read project ; do
    count=$[$count + 1]
    mv "$dbdir/$project" "$dbdir/db_${project}_gdi"
done  < "$tmp"

echo "$count databases renamed"
rm -f "$tmp"
