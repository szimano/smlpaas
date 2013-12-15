#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

. $DIR/env.sh

DEST=$JAR_HOME/$2
FILE=$DROPBOX/$1

mkdir -p $DEST

cp $FILE $DEST
rm $FILE
