#! /bin/bash

# java run wrapper

A=`readlink $0` # resolve symlinks
A=${A:-$0}      # if original wasn't a symlink, BSD returns empty string
SCRIPT_REAL_PATH=$(cd ${A%/*} && echo $PWD/${A##*/}) # path to original shell script, resolved from relative paths
PROJECT_DIR=`dirname \`dirname $SCRIPT_REAL_PATH\``  # base directory

CLSPTH=$PROJECT_DIR
for i in `ls $PROJECT_DIR/lib/*.jar`
do
  CLSPTH=${CLSPTH}:${i}
done

TMPDIR=/tmp

java -Xmx512M -Dlog4j.configuration=$PROJECT_DIR/log4j.configuration -Dderby.system.home=$PROJECT_DIR/db -Djava.io.tmpdir=$TMPDIR -cp ${CLSPTH} com.gooddata.processor.GdcDI $*
