cd snaplogic
call mvn install
cd ..
call mvn install
cd cli-distro
call mvn assembly:assembly
cd ..
cd snap-distro
call mvn assembly:assembly