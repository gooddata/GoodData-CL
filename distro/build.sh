#! /bin/bash

mvn package
mvn install
mvn assembly:assembly
