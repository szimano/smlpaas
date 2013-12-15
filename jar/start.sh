#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

. $DIR/env.sh

PID_FILE="jar.pid"

cd $JAR_HOME

if [[ -e $PID_FILE ]]
then
	# maybe the jar is dead anyway?
	if kill -0 `cat $PID_FILE`; then
		echo "JAR is running, exiting..."
		exit -1
	fi
fi

if ! [[ -z "$1" ]]
then
	export JAVA_OPTS="$1"
fi

java $JAVA_OPTS -jar ROOT.jar > output.log &
PID=$!
echo $PID > $PID_FILE
disown

