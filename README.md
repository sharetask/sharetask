ShareTask
======

* Checkout from repository.
* Build application and run it in embeded jetty or tomcat7: 

Linux:
>    mvn clean install; mvn -pl backend jetty:run

Windows:
>    mvn clean install & mvn -pl backend jetty:run

When you want to run application in tomcat container change jetty goal to tomcat7 goal e.g. linux variant:
>    mvn clean install; mvn -pl backend tomcat7:run
