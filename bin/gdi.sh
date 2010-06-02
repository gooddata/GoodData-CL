#! /bin/bash

# java run wrapper

file=`readlink $0`;      # resolve symlinks if necessary
file=${file:-$0};        # if not a symlink, use the $0
PRJ_BIN=`dirname $file`  # base directory

. $PRJ_BIN/cfg.sh

java -Dderby.system.home=../db -cp ${CLASSPATH} com.gooddata.processor.GdcDI $*