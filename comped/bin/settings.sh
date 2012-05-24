#!/bin/sh



# Setting Path's
PATH=.:$PATH
PATH=${HOME}/javalibs/maven/apache-maven-2.1.0/bin:/usr/bin:/usr/local/bin:${PATH}:/opt/csw/bin


JAVA_HOME=/usr/jdk/latest
export JAVA_HOME
PATH=${JAVA_HOME}/bin:${PATH}



COMPED_HOME=`pwd`
echo COMPED_HOME=${COMPED_HOME}
TOMCAT_HOME=`cd ${HOME}/platform/tomcat && pwd`
echo TOMCAT_HOME=${TOMCAT_HOME}
ONTOLOGY_HOME=`cd ${TOMCAT_HOME}/webapps/ontologies/current && pwd`
echo ONTOLOGY_HOME=${ONTOLOGY_HOME}
ONTOLOGY_PUBLIC=`cd ${TOMCAT_HOME}/webapps/ontologies/dev && pwd`
echo ONTOLOGY_PUBLIC=${ONTOLOGY_PUBLIC}

ROOT_HOST="i2geo.net"
ROOT_URL="http://${ROOT_HOST}/"
echo ROOT_URL=${ROOT_URL}
LOCALHOST_URL="http://localhost:53080/"
echo LOCALHOST_URL=${LOCALHOST_URL}



