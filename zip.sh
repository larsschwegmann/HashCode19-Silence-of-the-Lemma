#!/bin/sh
FILE="hashcodesrc.zip"
if [ -e $FILE ]; then
  rm -rf $FILE
fi
zip -r $FILE ./src
