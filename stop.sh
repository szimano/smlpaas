#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

. $DIR/env.sh

TOMCAT_PID=`ps aux | grep "org.apache.catalina.startup.Bootstrap start" | grep -v grep | awk '{print $2}'`

if [[ -z "$TOMCAT_PID" ]]
then
	echo "Tomcat is not started"
else
	echo "$TOMCAT_PID" > /tmp/catalina.pid
	export CATALINA_PID="/tmp/catalina.pid"

	$TOMCAT_HOME/bin/catalina.sh stop 30 -force
fi

