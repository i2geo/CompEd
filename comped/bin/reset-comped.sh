#!/bin/sh

commandHere="$0"
binPath=`dirname $commandHere`
cd $binPath; cd ..
. ./bin/settings.sh


echo "===  move comped offline "
cd $COMPED_HOME
./bin/switch-comped-offline.sh

# flush changes
## wait 10 s
echo "=== Waiting 10s for changes to be applied"
sleep 10


cd $ONTOLOGY_HOME 
## TODO: here to import from offline work: just update instead of commit
## then going offline involves: - convert to read-only and commit
echo "=== Checking Consistency"
java -jar ${COMPED_HOME}/bin/i2geo-onto-fat-SNAPSHOT.jar  GeoSkills.owl
cp GeoSkills.owl GeoSkills-before-cleanup.owl
java -jar "${COMPED_HOME}/bin/geoskills-cleaner-jar-with-deps-2009-01-19.jar" \
  GeoSkills-before-cleanup.owl GeoSkills.owl
echo "=== Committing ontology"
svn -q commit -m "Onto update and comped reset." GeoSkills.owl
echo "=== Placing ontology"

## updating public URLs
cd ${ONTOLOGY_PUBLIC}
cp ${ONTOLOGY_HOME}/GeoSkills.owl geoskills.owl
cd ${ONTOLOGY_PUBLIC}
cp ${ONTOLOGY_HOME}/GeoSkills.owl ./GeoSkills.owl
cp ./GeoSkills.owl ./geoskills.owl
svn up
cp GeoSkills.owl GeoSkills


## 
echo "=== Digesting ontology to tables"
cd $COMPED_HOME
mvn comped:gs2comped

# come back here, repopulate with DB
cd $COMPED_HOME
## database dump
backup_name="backups/comped-`date  "+%Y-%m-%d_%H:%M"`.sql"
echo "=== Securing a MySQL backup on $backup_name"
mkdir -p backups
mysqldump -u `cat bin/dbuser` -p`cat bin/dbpassword` -h `cat bin/dbhost` -P `cat bin/dbport` `cat bin/dbname` > ${backup_name}


## activate repopulation
echo "=== Repopulating database."
cd $COMPED_HOME
mvn -Dfile.encoding=UTF-8 -Pprod-repopulate,mysql-prod test-compile > $ONTOLOGY_HOME/last-reset-log.txt

# start tomcat again
# echo "=== Starting tomcat."
# cd $TOMCAT_HOME
# bin/startup.sh

echo "===  move comped online "
cd $COMPED_HOME
./bin/switch-comped-online.sh
echo "===  comped is online "

# indexing
sleep 10
echo "=== Requesting index to be rebuilt."
curl --silent ${ROOT_URL}'SearchI2G/indexing/doIndex'

