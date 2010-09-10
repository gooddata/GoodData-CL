#!/bin/sh

# $SnapHashLicense:
# 
# SnapLogic - Open source data services
# 
# Copyright (C) 2006 - 2010, SnapLogic, Inc.  All rights reserved.
# 
# See http://www.snaplogic.org for more information about
# the SnapLogic project. 
# 
# This program is free software, distributed under the terms of
# the GNU General Public License Version 2. See the LEGAL file
# at the top of the source tree.
# 
# "SnapLogic" is a trademark of SnapLogic, Inc.
# 
# 
# $

# $Id: install.sh 11145 2010-04-06 20:54:47Z dmitri $

#  Set some variables needed for the installation

CURDIR=`pwd`
export CURDIR

PKGDIR=`dirname $0`
if [ "${PKGDIR}x" = "x" -o "${PKGDIR}" = "." ]
then
  PKGDIR=${CURDIR}
fi
export PKGDIR

PKGNAME=`find ${PKGDIR} -mindepth 1 -maxdepth 1 -mode d -print | awk -F/ '{ print $NF }' | grep -v "^\." | grep -v components`
export PKGNAME

# Nouns for installer

N_PATCH="patch"
export N_PATCH

N_EXTENSION="extension"
export N_EXTENSION

##################################################################
#
# Main body of script
#
##################################################################

# Is this a patch or an extension
echo ${PKGNAME} | grep "_patch_" > /dev/null 2>&1
if [ $? -eq 0 ] ; then
  NOUN=${N_PATCH}
else
  NOUN=${N_EXTENSION}
fi

display_banner()
{
  cat << __EOM__

-------------------------------------------------------------
Welcome to the Snap Installer
-------------------------------------------------------------

This installer will install a Snap in your instance of the SnapLogic Server.
The Install includes the following steps:
1. Stop the SnapLogic server to enable the install
2. Install the Snap components into your local SnapLogic directory
3. Re-Start the SnapLogic server 
4. Update the SnapLogic server by importing Snap files and/or 
   running install scripts

Before Installation begins, please make sure you have the following 
information handy:
- Install directory for SnapLogic, e.g. /opt/snaplogic
- SnapLogic Admin Password

During installation you may be asked for additional information, 
such as credentials for accessing a source/target integration system.
We are diligently working to simplify the installation process 
in an upcoming release.  Stay tuned.....

__EOM__
}

# Find original $INSTDIR for SnapLogic
find_instdir()
{
  INSTDIR="/opt/snaplogic"
  
  while [ TRUE ]
  do
    echo -n "Please specify the install directory for SnapLogic [${INSTDIR}]: "
    read INSTDIRTMP
   
    if [ "${INSTDIRTMP}x" = "x" ]
    then
      INSTDIRTMP=${INSTDIR}
    fi
    
    # Double check that directory exists
    if [ ! -d ${INSTDIRTMP} ]
    then
      # specified directory does not exist
      echo ""
      echo ""
      echo "The directory you entered, ${INSTDIRTMP}, does not exist."
      echo "Please enter the valid directory for the SnapLogic installation."
      echo ""
    elif [ ! -d ${INSTDIRTMP}/repository ]
    then
      # specified directory exists, but not repository subdir so probably
      # not the base install directory
      echo ""
      echo ""
      echo "The directory you entered, ${INSTDIRTMP}, does not appear to be the"
      echo "base install directory for a SnapLogic installation. "
      echo "Please enter the valid directory for the SnapLogic installation."
      echo ""
    else
      # specified directory exists and has repository subdir, so let's move on. 
      INSTDIR=${INSTDIRTMP}
      break
    fi
  done
  export INSTDIR
  PYTHONEXE=${INSTDIR}/python/bin/python
}



main()
{
  # Do we have a python in the path?
  PYTHONEXE=`which python`

  # No python in path? Drat! Prompt instdir so we can get to our private python.
  if [ $? -ne 0 ] ; then
    display_banner
    find_instdir
    ${PYTHONEXE} ${PKGDIR}/common.py $INSTDIR
  else
    ${PYTHONEXE} ${PKGDIR}/common.py
  fi
}

main
