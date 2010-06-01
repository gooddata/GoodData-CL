#!/bin/bash

file=`readlink $0`; # resolve symlinks if necessary
file=${file:-$0};

dir=`dirname \`dirname $file\`` # base directory
java -classpath "$dir/target/GoodDataDI-0.6-jar-with-dependencies.jar" com.gooddata.processor.GdcDI