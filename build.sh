#! /bin/bash

cd snaplogic
$M2_HOME/bin/mvn install
cd ..
$M2_HOME/bin/mvn install
cd cli-distro
$M2_HOME/bin/mvn assembly:assembly
cd ..
cd snap-distro
$M2_HOME/bin/mvn assembly:assembly