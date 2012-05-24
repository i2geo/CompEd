Comped README 
=============================


To get started, please complete the following steps:

1. The project uses MySQL by default.

   Download and install a MySQL 5.x database from 
   http://dev.mysql.com/downloads/mysql/5.0.html#downloads.

   Create a new database schema 'comped' and a user 'comped' with all
   privileges on the database schema 'comped'
   
2. Run "mvn jetty:run-war" and view the application at http://localhost:8080.


CompEd in production mode with Apache Tomcat:
=============================================

	1. Take comped offline
		Either shutdown tomcat, from the tomcat directory:
  		./bin/shutdown.sh
	   or take it offline using the manager, from the comped directory:
	   	./bin/switch-comped-offline.sh

	2. Create/update the database schema file (XML) from GeoSkills ontology
		
		mvn comped:gs2comped
		
		Note: the svn provides some production state data which might be old.
	
	3. Populate database and create a package for a servlet container
		 mvn -Pprod-repopulate,mysql-prod clean package 
		 
	   This command will repopulate the database and package comped to a war file
	   
	4. Copy comped.war to the webapp directory of your Apache Tomcat, or copy directory
     to the webapps directory.
     If using the online/offline scripts. Copy the webapp directory as the directory
     comped inside {TOMCAT_HOME}/parking
    
	
	5. Restart apache tomcat
		either, from the tomcat dir ./bin/startup.sh
    or get online, from the comped dir
     bin/switch-comped-online.sh
      
		
	6. See your logs
	
	    tail -f logs/catalina.out	   
	
	     
CompEd in production mode running with Jetty:
=============================================
	1. Stop Jetty to put CompEd in offline mode.
	
	2. Create/update the database schema file (XML) from GeoSkills ontology
		
		mvn comped:gs2comped
		
		Note: the svn provides some production state data which might be old.
		
    3. Populate database and run CompEd
    
    	mvn -Pprod-repopulate jetty:run-war
    	
    ------- Optional -------
    	
    4. If your database is already populated and you simply want to run CompEd
    	
       mvn -Pprod jetty:run-war
       
    5. If you only want to populate the database 

       mvn -Pprod-repopulate test-compile

    6. In test phase, you might want to make a clean repopulate of the database

	   mvn -Pprod-clean-repopulate jetty:run-war
	   		  
If you encounter any constraint violation problems, drop your database. CompEd will create
a new database in step 3. CompEd is pre-configured for use with MySQL with a database named
"comped_prod". If you want to run tests, drop the profile parameter and CompEd will run
JUnit tests with a database called "comped_test".  This way, you can run tests without
harming your production state. 


Alternative databases:
========================
You can run Comped with other databases. Make changes in the database profiles 
section in pom.xml, i.e., provide a correct user/password. Run maven with the
appropriate profile:

      mvn -Ppostgresql jetty:run-war

For testing purposes, you can run the application with an in-memory database:

      mvn -Phsqldb jetty:run-war

Note: due to dbunit and maven issues, a clean insert of sample data will cause an exception.
Be careful, this configuration will run a test environment. If you want to change the database
for the production state, you have the following options: i) make changes in "prod" profile,
ii) add a new production profile with correct database/jdbc settings, or iii) make changes
in the database profile by overriding the source for the dbunit maven plugin.


Alternative Mail Service Agents:
==================================

The default mail host is set to 'mail'. If you want to use another host,
set the 'mail.host' property in 'comped/src/main/resources/mail.properties'.

When you want to test at home where mail cannot be resolves, set the mail
host to 'localhost'. CompEd will warn you when it cannot find any mail service,
but it won't fail the integration tests. To run all tests successfully, you
can install a localmail service. For instance James, a complete mailand news server.
Find it at: http://james.apache.org/ 

We recommend to change the default "from" property to an accurate address. For
testing an address with care, otherwise you will get loads of mails on each test
cycle.  

Other Maven Profiles:
======================

CompEd's build script is flexible enough to run in different contexts defined
by profiles. Most database profiles have also a second productionm profile whose
id is the same as default, but with a "-prod" suffix.

There are three different build production profiles:

	1. prod
	
		Run this when you simply want to recompile your application.
		It won't change the database.
		
	2. prod-clean-repopulate
		
		Drops the database and repopulates it from default-data.xml
		
	3. prod-repopulate
		
		Drops all tables but the "user", "role", user/role" table and
		repopulates the dropped tables from default-data.xml.
		
Testing ChooseMe:
==================

- open page at <Domain>/comped/test/chooseMeOpener.html
- Click on the opener link.
- navigate to a competency/topic and click the "choose me" link
- the item should appear in the SKB with current focus

To edit this project:
=======================

1. NetBeans is able to read Maven project files. Just open the POM.

2. IntelliJ Idea is able to read Maven project files. Just open the POM.
   To edit the project in older versions of IntelliJ Idea:
   
      mvn idea:idea

3. To edit the project in Eclipse:

      mvn eclipse:eclipse

   Launch Eclipse and go to File > Import > Existing Projects into Workspace (under the 
   General category). Select the root directory of your project, followed by the modules 
   to import. Click Finish to complete the process.
      
   Set the classpath variable M2_REPO. Eclipse needs to know the path to the local maven 
   repository. You should be able to do this by executing the command: 
   
      mvn -Declipse.workspace=C:\Source eclipse:add-maven-repo.

   If that doesn't work, can also define a new classpath variable inside Eclipse. From the 
   menu bar, select Window > Preferences. Select the Java > Build Path > Classpath Variables 
   page and set M2_REPO to equal ~/.m2/repository.
   
