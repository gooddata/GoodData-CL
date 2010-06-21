@echo off
rem $SnapRemLicense:
rem 
rem SnapLogic - Open source data services
rem 
rem Copyright (C) 2006 - 2010, SnapLogic, Inc.  All rights reserved.
rem 
rem See http://www.snaplogic.org for more information about
rem the SnapLogic project. 
rem 
rem This program is free software, distributed under the terms of
rem the GNU General Public License Version 2. See the LEGAL file
rem at the top of the source tree.
rem 
rem "SnapLogic" is a trademark of SnapLogic, Inc.
rem 
rem 
rem $

rem $Id: install.bat 11145 2010-04-06 20:54:47Z dmitri $


set PLATFORM=Windows
set SCRIPT=%0

setlocal

set CURDIR=%CD%
for %%l in (%SCRIPT%) do set FNAME=%%~nl
for %%l in (%SCRIPT%) do set PKGDIR=%%~dpl
if  "%PKGDIR%" == "" set PKGDIR=%CD%

cd /d "%PKGDIR%"
for /D %%l in ("*") do if NOT "%%~l" == "components" set PKGNAME=%%~l


set N_PATCH=patch
set N_EXTENSION=extension
set NOUN=%N_EXTENSION%

rem If this is a patch, call it a patch
if exist "*_patch_*" set NOUN=%N_PATCH%

rem  Find original $INSTDIR for SnapLogic

set INSTDIR=C:\Program Files\snaplogic

rem Show a banner describing what we are

echo.
echo.
echo -------------------------------------------------------------
echo Welcome to the Snap Installer
echo -------------------------------------------------------------
echo.
echo This installer will install a Snap in your instance of the SnapLogic Server.
echo The Install includes the following steps:
echo 1. Stop the SnapLogic server to enable the install
echo 2. Install the Snap components into your local SnapLogic directory
echo 3. Re-Start the SnapLogic server 
echo 4. Update the SnapLogic server by importing Snap files and/or 
echo    running install scripts
echo.
echo Before Installation begins, please make sure you have the following 
echo information handy:
echo - Install directory for SnapLogic, e.g. C:\Program Files\snaplogic
echo - SnapLogic Admin Password
echo.
echo During installation you may be asked for additional information, 
echo such as credentials for accessing a source/target integration system.
echo We are diligently working to simplify the installation process 
echo in an upcoming release.  Stay tuned.....
echo.
echo.

:getdirname
set INSTDIRTMP=


SET /P INSTDIRTMP="Please specify the install directory for SnapLogic [%INSTDIR%]: "

if "%INSTDIRTMP%" == "" set INSTDIRTMP=%INSTDIR%
rem 
rem Look to see if there is a repository dir under the specified install dir
rem 
if  exist "%INSTDIRTMP%\repository" goto xinstall
rem
rem No repository, so probably not the install dir.  Give feedback based on 
rem whether specified install dir exists, or not
rem
if NOT exist "%INSTDIRTMP%" (
        echo.
        echo.
        echo The directory you specified, %INSTDIRTMP%, does not exist.
        echo Please specify an existing directory.
        echo.
        echo.
    ) else (
        echo.
        echo.
        echo The directory you specified does not appear to be the base install
        echo dir for SnapLogic.  The base install dir should be the same one
        echo you specified when running the SnapLogic product installer.
        echo.
        echo.
    )
    
goto getdirname

:xinstall

set INSTDIR=%INSTDIRTMP%
set PYTHONEXE=%INSTDIR%\python\Scripts\python.exe
set SITEPACKAGES=%INSTDIR%\python\Lib\site-packages
"%PYTHONEXE%" "%PKGDIR%\common.py" "%INSTDIR%"

endlocal

pause



