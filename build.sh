#! /bin/bash

QUOTED_ARGS=""
while [ "$1" != "" ] ; do

  QUOTED_ARGS="$QUOTED_ARGS \"$1\""
  shift

done

if [ -z "$M2CMD" ] ; then
  if [ -n "$M2_HOME"  ] ; then
    M2CMD="$M2_HOME/bin/mvn"
  else
    M2CMD="`which mvn`"
  fi
fi

if [ ! -x "$M2CMD" ] ; then
  echo "Error: M2_HOME is not defined correctly."
  echo "  We cannot execute $M2CMD"
  exit 1
fi


cd snaplogic
"$M2CMD" install
cd ..
"$M2CMD" install
cd cli-distro
"$M2CMD" assembly:assembly
cd ..
cd snap-distro
"$M2CMD" assembly:assembly