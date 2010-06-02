#! /bin/bash

file=`readlink $0`;      # resolve symlinks if necessary
file=${file:-$0};        # if not a symlink, use the $0
PRJ_BIN=`dirname $file`  # base directory

. $PRJ_BIN/cfg.sh

java -cp ${CLASSPATH} org.apache.derby.tools.ij