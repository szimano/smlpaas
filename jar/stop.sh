#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

. $DIR/env.sh

PID_FILE="jar.pid"

cd $JAR_HOME

if [[ -e $PID_FILE ]]
then
	kill `cat $PID_FILE`
	rm $PID_FILE
else
	echo "JAR is not started"
fi
