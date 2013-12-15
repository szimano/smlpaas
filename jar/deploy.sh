#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

. $DIR/env.sh

rm -rf $JAR_HOME/ROOT.jar

cp $DROPBOX/FILE $JAR_HOME/ROOT.jar

