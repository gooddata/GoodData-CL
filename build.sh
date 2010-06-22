#! /bin/bash

cd snaplogic
mvn install
cd ..
mvn install
cd cli-distro
mvn assembly:assembly
cd ..
cd snap-distro
mvn assembly:assembly