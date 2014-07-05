Blog Me Do
==========

Prerequisites
=============
- An internet connection
- Maven 3.x
- JDK 7.0 and up

Arguments
=========
-Dserver.port - Sets the server's port. Default is 8080

Running the application
=======================
The application was written using spring-boot.
It uses an in-memory H2 database and hibernate for persistency. Spring-boot by default will load a tomcat server.
After the serevr is loaded some demo data will be added to the system.

Running locally
===============
Build the project with Maven and then execute the command
java -jar <root>/blog-main/target/blog-main-1.0-SNAPSHOT.jar <arg>

Deployment
==========
The application is ready for deployment on Heroku, for any other cloud provider please check the Spring Boot reference
at http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#cloud-deployment

Project structure
=================
There are 4 modules in this project:

1. blog-server - Contains all the server-side code - the entity, the spring data repository etc.
2. blog-client - Contains all the client side code - html and javascript.
3. blog-main - Contains the main method that loads the application along with the spring context and the demo data
               populator.
4. blog-system-tests - Contains the system tests for the REST API.

Disclaimer
==========
This is not a very good blogging system...