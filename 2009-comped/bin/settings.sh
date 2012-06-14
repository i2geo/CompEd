#!/bin/sh

. ${HOME}/.bash_profile

COMPED_HOME=`pwd`
echo COMPED_HOME=${COMPED_HOME}
TOMCAT_HOME=`cd ../../tomcat-for-draft && pwd`
echo TOMCAT_HOME=${TOMCAT_HOME}
ONTOLOGY_HOME=`cd ../../tomcat-for-draft/webapps/ontologies/current && pwd`
echo ONTOLOGY_HOME=${ONTOLOGY_HOME}
ROOT_HOST="draft.i2geo.net"
ROOT_URL="http://${ROOT_HOST}/"
echo ROOT_URL=${ROOT_URL}
LOCALHOST_URL="http://localhost:51080/"
echo LOCALHOST_URL=${LOCALHOST_URL}



