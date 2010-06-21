#! /bin/bash

A=`readlink $0`
A=${A:-$0}
SCRIPT_REAL_PATH=$(cd ${A%/*} && echo $PWD/${A##*/}) # path to original shell script
PROJECT_DIR=`dirname \`dirname $SCRIPT_REAL_PATH\``  # base directory

. $PROJECT_DIR/bin/cfg.sh

java -cp ${CLASSPATH} org.apache.derby.tools.ij