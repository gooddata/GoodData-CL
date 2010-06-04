#! /bin/bash

# java run wrapper

file=`readlink $0`;      # resolve symlinks if necessary
file=${file:-$0};        # if not a symlink, use the $0
PRJ_BIN=`dirname\`dirname $file\``  # base directory

. $PRJ_BIN/bin/cfg.sh

java -Dderby.system.home=$PRJ_BIN/db -cp ${CLASSPATH} com.gooddata.processor.GdcDI $*