# springboot-user-app

[![Build Status](https://travis-ci.org/codecentric/springboot-sample-app.svg?branch=master)](https://travis-ci.org/codecentric/springboot-sample-app)

Minimal [Spring Boot](http://projects.spring.io/spring-boot/) user app.

## Requirements

For building and running the application you need:

- [JDK 11](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
- [Maven 3](https://maven.apache.org)

This is a sample Java / Maven / Spring Boot (version 2.7.12) application that can be used as a starter for creating a microservice complete with built-in health check, metrics and much more. I hope it helps you.

## How to Run

This application is packaged as a jar that has Tomcat embedded. No Tomcat or JBoss installation is necessary. You run it using the ```java -jar``` command.

* Clone this repository
* Make sure you are using JDK 11 and Maven 3.x
* You can build the project and run the tests by running ```mvn clean package```
* Once successfully built, you can run the service by one of these two methods:
```
 java -jar target/users-0.0.1-SNAPSHOT.jar
or
 mvn spring-boot:run
```
* Check the stdout or log file to make sure no exceptions are thrown

Once the application runs you should see something like this

```
2023-06-19 13:31:23.091  INFO 19387 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
2023-06-19 13:31:23.097  INFO 19387 --- [           main] com.khoubyari.example.Application        : Started Application in 22.285 seconds (JVM running for 23.032)
```


## About the Service

The service is just a simple hotel review REST service. It uses an in-memory database (H2) to store the data. If your database connection properties work, you can call some REST endpoints defined in ```com.nisum.user.authController``` on **port 8080**. (see below)

More interestingly, you can start calling some of the operational endpoints (see full list below) like ```/metrics``` and ```/health```

You can use this sample service to understand the conventions and configurations that allow you to create a user registration and jwt token generation service.

Here are some endpoints you can call:

### Get information about system health, configurations, etc.

```
http://localhost:8080/health
http://localhost:8080/info
http://localhost:8080/metrics
```

### Create a user

```
POST http://localhost:8080/api/auth/signup
Accept: application/json
Content-Type: application/json

{
    "name":"username",
    "email":"mail@dominio.com",
    "password":"Sba@1234",
    "phones": [
        {
        "number": "099123456",
        "citycode": "1",
        "contrycode": "57"
        }
    ]
}

RESPONSE: HTTP 201 (Created)
Location header: http://localhost:8080/user/1
```

### Login user

```
POST http://localhost:8080/api/auth/signin
Accept: application/json
Content-Type: application/json

{
    "name":"username",
    "password":"Sba@1234"
}

RESPONSE: HTTP 200 
```


### User information

```
GET http://localhost:8080/user/{uuid}
Accept: application/json
Content-Type: application/json
Header: Authorization Bearer ...

RESPONSE: HTTP 200 
```

### To view your H2 in-memory database

The 'test' profile runs on H2 in-memory database. To view and query the database you can browse to http://localhost:8080/h2-console. The default username is 'sa' with a blank password. Make sure you disable this in your production profiles. For more, see https://goo.gl/U8m62X

It is recommended to create a database file in the user's home, in this case with the name test.mv.db

![Alt text](h2-console.png?raw=true "H2 Console")

Click on the connect button and the following will be displayed:

![Alt text](h2-console-detail.png?raw=true "H2 Console")

Here you can execute any select instruction on the database tables to visualize the data.

Below are some basic sequences and flowcharts to understand user registration.

![Alt text](flow.png?raw=true "flowchart")

![Alt text](sequence.png?raw=true "sequence diagram")

