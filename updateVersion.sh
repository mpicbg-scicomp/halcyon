#!/bin/bash
VERSION_NUMBER=1.0.0
BUILD_DATE=$(date +"%d.%m.%Y %H:%M:%S")

grep -v "^build.date" src/main/resources/Strings.properties > /tmp/Strings.properties
mv /tmp/Strings.properties src/main/resources/Strings.properties
echo "build.date=Version $VERSION_NUMBER ($BUILD_DATE)" >> src/main/resources/Strings.properties
