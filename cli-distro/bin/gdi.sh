#! /bin/bash

# java run wrapper

A=`readlink "$0"` # resolve symlinks
A="${A:-$0}"      # if original wasn't a symlink, BSD returns empty string
SCRIPT_REAL_PATH=$(cd "${A%/*}" && echo "$PWD/${A##*/}") # path to original shell script, resolved from relative paths
PROJECT_DIR=`dirname "$SCRIPT_REAL_PATH"`
PROJECT_DIR=`dirname "$PROJECT_DIR"` # base directory

# OS specific support.  $var _must_ be set to either true or false.
cygwin=false;
darwin=false;
mingw=false
case "`uname`" in
  CYGWIN*) cygwin=true ;;
  MINGW*) mingw=true;;
  Darwin*) darwin=true
           if [ -z "$JAVA_VERSION" ] ; then
             JAVA_VERSION="CurrentJDK"
           else
             echo "Using Java version: $JAVA_VERSION"
           fi
           if [ -z "$JAVA_HOME" ] ; then
             JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/${JAVA_VERSION}/Home
           fi
           ;;
esac

if [ -z "$JAVA_HOME" ] ; then
  if [ -r /etc/gentoo-release ] ; then
    JAVA_HOME=`java-config --jre-home`
  fi
fi

if [ -z "$JAVA_HOME" ] ; then
  if [ -d /usr/lib/jvm/jre ] ; then
    JAVA_HOME=/usr/lib/jvm/jre
  fi
fi

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
  [ -n "$JAVA_HOME" ] &&
    JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] &&
    CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

# For Migwn, ensure paths are in UNIX format before anything is touched
if $mingw ; then
  [ -n "$JAVA_HOME" ] &&
    JAVA_HOME="`(cd "$JAVA_HOME"; pwd)`"
fi

if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD="`which java`"
  fi
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly."
  echo "  We cannot execute $JAVACMD"
  exit 1
fi

if [ -z "$JAVA_HOME" ] ; then
  echo "Warning: JAVA_HOME environment variable is not set."
fi

CLSPTH="$PROJECT_DIR"
for i in "$PROJECT_DIR/lib/"*.jar
do
  CLSPTH="${CLSPTH}:${i}"
done

if [ -z "$TMPDIR" ] ; then
  TMPDIR=/tmp
fi

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  [ -n "$JAVA_HOME" ] &&
    JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
  [ -n "$HOME" ] &&
    HOME=`cygpath --path --windows "$HOME"`
  [ -n "$PROJECT_DIR" ] &&
    PROJECT_DIR=`cygpath --path --windows "$PROJECT_DIR"`
  [ -n "$CLSPTH" ] &&
    CLSPTH=`cygpath --path --windows "$CLSPTH"`
  [ -n "$TMPDIR" ] &&
    TMPDIR==`cygpath --path --windows "$TMPDIR"`
fi

"$JAVACMD" -Xmx1024M -Dlog4j.configuration="$PROJECT_DIR/log4j.configuration" -Dfile.encoding="utf-8" -Djava.io.tmpdir="$TMPDIR" -cp "${CLSPTH}" com.gooddata.processor.GdcDI "$@"
