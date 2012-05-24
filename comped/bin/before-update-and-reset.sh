#!/bin/bash


commandHere="$0"
binPath=`dirname $commandHere`
cd $binPath; cd ..
. ./bin/settings.sh


echo "---  move comped offline "
cd $COMPED_HOME
./bin/switch-comped-offline.sh

# flush changes
## wait 10 s
echo "---- Waiting 10s for changes to be applied"
sleep 10


cd $ONTOLOGY_HOME 
## TODO: here to import from offline work: just update instead of commit
## then going offline involves: - convert to read-only and commit
echo "--- Checking Consistency"
java -jar ${COMPED_HOME}/bin/i2geo-onto-fat-SNAPSHOT.jar  GeoSkills.owl
echo "--- Committing ontology"
svn -q commit -m "Onto update and comped reset." GeoSkills.owl

