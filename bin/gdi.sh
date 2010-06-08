#! /bin/bash

# java run wrapper

A=`readlink $0` # resolve symlinks
A=${A:-$0}      # if original wasn't a symlink, BSD returns empty string
SCRIPT_REAL_PATH=$(cd ${A%/*} && echo $PWD/${A##*/}) # path to original shell script, resolved from relative paths
PROJECT_DIR=`dirname \`dirname $SCRIPT_REAL_PATH\``  # base directory

. $PROJECT_DIR/bin/cfg.sh

java -Dderby.system.home=$PROJECT_DIR/db -Djava.io.tmpdir=$PROJECT_DIR/tmp -cp ${CLASSPATH} com.gooddata.processor.GdcDI $*