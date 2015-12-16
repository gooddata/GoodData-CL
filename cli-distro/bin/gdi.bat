@echo off
setlocal ENABLEDELAYEDEXPANSION

SET PRJ_BIN=%~dp0
SET PRJ=%PRJ_BIN%..

if not "%JAVA_HOME%" == "" goto jhomeok

if not "%JAVA_HOME%" == "" goto javaHomeAlreadySet
for %%P in (java.exe) do set JAVA_EXE=%%~$PATH:P

if not "%JAVA_EXE%" == "" goto execute

echo.
echo ERROR: JAVA_HOME not found in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:jhomeok
set JAVA_EXE=%JAVA_HOME%\bin\java.exe
if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = "%JAVA_HOME%"
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:execute

set CLSPTH=%PRJ%
if defined CLASSPATH set CLSPTH=%CLASSPATH%;%PRJ%;.

FOR /R "%PRJ%\lib" %%G IN (*.jar) DO set CLSPTH=!CLSPTH!;%%G

setlocal DISABLEDELAYEDEXPANSION
"%JAVA_EXE%" -Xmx1024M -Dfile.encoding="utf-8" -Dlog4j.configuration="%PRJ%\log4j.configuration" -Djava.io.tmpdir="%PRJ%\tmp" -classpath "%CLSPTH%" com.gooddata.processor.GdcDI %*
if %ERRORLEVEL% NEQ 0 goto error
goto end

:error
@REM error sink
exit /B 1

:end
@REM end of the script