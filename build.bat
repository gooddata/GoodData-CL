@echo off
setlocal ENABLEDELAYEDEXPANSION

if not "%M2_HOME%" == "" goto m2homeok

echo.
echo ERROR: M2_HOME not found in your environment.
echo Please set the M2_HOME variable in your environment to match the
echo location of your Maven installation
echo.
goto error

:m2homeok
if exist "%M2_HOME%\bin\mvn.bat" goto execute

echo.
echo ERROR: M2_HOME is set to an invalid directory.
echo M2_HOME = "%M2_HOME%"
echo Please set the M2_HOME variable in your environment to match the
echo location of your Maven installation
echo.
goto error

:execute
cd snaplogic
call %M2_HOME%\bin\mvn.bat install
cd ..
call %M2_HOME%\bin\mvn.bat install
cd cli-distro
call %M2_HOME%\bin\mvn.bat assembly:assembly
cd ..
cd snap-distro
call %M2_HOME%\bin\mvn.bat assembly:assembly
goto end

:error
@REM error sink

:end
@REM end of the script
