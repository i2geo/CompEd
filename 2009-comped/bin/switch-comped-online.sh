#!/bin/sh

commandHere="$0"
binPath=`dirname $commandHere`
cd $binPath; cd ..
. ./bin/settings.sh

echo ==== stopping comped
cd "$COMPED_HOME"
wget -O - "http://`cat bin/tomcatManagerUserPassword`@${ROOT_HOST}/manager/stop?path=/comped"

echo ==== replacing comped by \"in maintenance\"
cd "$TOMCAT_HOME"
mv webapps/comped parking/comped-inmaintenance
mv parking/comped webapps/comped

echo ==== activating comped \"in maintenance\"
cd "$COMPED_HOME"
wget -O - "http://`cat bin/tomcatManagerUserPassword`@${ROOT_HOST}/manager/start?path=/comped"


