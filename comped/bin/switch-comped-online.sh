#!/bin/sh

commandHere="$0"
binPath=`dirname $commandHere`
cd $binPath; cd ..
. ./bin/settings.sh

echo '==== stopping comped (in  maintenance)'
cd "$COMPED_HOME"
curl --silent "http://`cat bin/tomcatManagerUserPassword`@${ROOT_HOST}/manager/stop?path=/comped"

echo '==== replacing "in maintenance" by comped'
cd "$TOMCAT_HOME"
mv webapps/comped parking/comped-inmaintenance
mv parking/comped webapps/comped

echo ' ==== activating comped '
cd "$COMPED_HOME"


curl --silent "http://`cat bin/tomcatManagerUserPassword`@${ROOT_HOST}/manager/start?path=/ontoUpdate"
curl --silent "http://`cat bin/tomcatManagerUserPassword`@${ROOT_HOST}/manager/start?path=/comped"


