# Argu

## Authors

- De Luca, Juan Manuel - 60103
- Kim, Azul Candelaria - 60264
- Negro, Juan Manuel - 61225
- Sambartolomeo, Mauro Daniel - 61279

## Installation

The application has two releases. The first one is a static web application built using SpringMVC and JSP. The second one is a REST API using the same backend migrated to Jersey with Spring and a Single Page Application using React that consumes it.

To switch between releases, reviewers must use git to checkout the respective branches. To switch releases, please run:

- Static web application: `git checkout release-6.0.1`
- REST API and SPA: `git checkout release-7.0.0`

## Build Requirements

- Java 1.8
- PostgreSQL 14+
- Apache Maven

To create a build run `mvn package`, this will produce an `app.war` file located in the `target` folder of the `webapp` module

## Execution

The application can be served with Tomcat 9, deploying the `app.war` artifact. If running release number 7.0.0 or higher, you must use the basename route `/paw-2022a-06` and modify the URL of the `.env.production` file on the `frontend` module to the URL deployment of the REST API.

## Reviewing

To allow reviewing of the application, we provide two accounts already configured. The first account is of a regular user, the second one is of a moderator. Both accounts also have their respective email addresses, to allow the reviewer to test the email sending functionality.

### User

mail: prueba.paw.grupo6.user@gmail.com
username: grupo6.user
password: pawgrupo6user

### Moderator

mail: prueba.paw.grupo6.mod@gmail.com
username: grupo6.mod
password: pawgrupo6mod
