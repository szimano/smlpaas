#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

. $DIR/env.sh

if ! [[ -z "$1" ]]
then
	export JAVA_OPTS="$1"
fi

$TOMCAT_HOME/bin/catalina.sh start

