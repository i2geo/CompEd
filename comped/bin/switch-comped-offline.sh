#!/bin/sh

commandHere="$0"
binPath=`dirname $commandHere`
cd $binPath; cd ..
. ./bin/settings.sh

echo '----- stopping comped'
cd "$COMPED_HOME"
curl --silent "http://`cat bin/tomcatManagerUserPassword`@${ROOT_HOST}/manager/stop?path=/comped"
sleep 10
curl --silent "http://`cat bin/tomcatManagerUserPassword`@${ROOT_HOST}/manager/stop?path=/ontoUpdate"

echo '----- replacing comped by in Maintenance'
cd "$TOMCAT_HOME"
mv webapps/comped parking/comped
mv parking/comped-inmaintenance webapps/comped

echo '---- activating comped \"in maintenance\"'
cd "$COMPED_HOME"
curl --silent  "http://`cat bin/tomcatManagerUserPassword`@${ROOT_HOST}/manager/start?path=/comped"

echo '----'
echo ''
echo "---- please don't forget to commit webapps/ontologies/current ----- "
echo ''

