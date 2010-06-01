#! /bin/bash

PRJ_BIN=`dirname $0`

. $PRJ_BIN/cfg.sh

java -cp ${CLASSPATH} org.apache.derby.tools.ij