#! /bin/bash

PRJ_BIN=`dirname $0`

. $PRJ_BIN/cfg.sh

java -Dderby.system.home=../db -cp ${CLASSPATH} com.gooddata.processor.GdcDI $*