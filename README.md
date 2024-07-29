# Re.Connect
## Deployed URLs

## Deployed URLs

### Production
- **Webpage**: [http://csci5308-vm4.research.cs.dal.ca:8080/#/](http://csci5308-vm4.research.cs.dal.ca:8080/#/)
- **Swagger**: [http://csci5308-vm4.research.cs.dal.ca:8080/swagger-ui/index.html](http://csci5308-vm4.research.cs.dal.ca:8080/swagger-ui/index.html)

### Test
- **Deployed frontend application**: [https://csci-5308-re-connect.netlify.app](https://csci-5308-re-connect.netlify.app)
- **Deployed backend application**: [https://reconnect-dal-asdc-reconnect.onrender.com](https://reconnect-dal-asdc-reconnect.onrender.com)


## Overview
Reconnect is an application designed to streamline the process of obtaining referrals. The platform caters to two types of users: Referents and Referrers.

Referents are individuals seeking referrals. They can browse profiles on the platform and send referral requests to potential Referrers.
Referrers are individuals who provide referrals. Upon receiving a request, a Referrer can review the Referent's profile to assess suitability. If the Referrer is satisfied, they can accept the request, enabling both parties to communicate further and exchange information through the integrated chat system.
Reconnect simplifies the referral process, making it easier for users to connect and collaborate based on mutual interests and needs.

## Tech Stack
- Spring Boot
- Web Socket
- MySQL
- Liquibase
- HTML
- CSS
- TypeScript
- Angular.js
- GitLab
- netlify
- onrender

# Backend Setup

## Prerequisite
- **Java**
- **Spring Boot**
- **Maven**

## Dependencies


| Dependency Group              | Artifact                                   | Description                                     |
|-------------------------------|--------------------------------------------|-------------------------------------------------|
| org.springframework.boot      | spring-boot-starter-data-jpa               | JPA integration with SpringBoot.                |
| org.springframework.boot      | spring-boot-starter-security               | Security features for Spring applications.      |
| org.springframework.boot      | spring-boot-starter-web                    | Spring MVC for web applications.                |
| org.springframework.boot      | spring-boot-starter-mail                   | Email support in Spring Boot.                   |
| com.mysql                     | mysql-connector-j                          | JDBC driver for MySQL.                          |
| org.projectlombok             | lombok                                     | Reduces boilerplate code with annotations.      |
| org.springframework.boot      | spring-boot-starter-test                   | Testing support with JUnit, Mockito, etc.       |
| org.springframework.security  | spring-security-test                       | Testing support for Spring Security.            |
| org.junit.jupiter             | junit-jupiter-api                          | JUnit 5 API for writing tests.                  |
| org.junit.jupiter             | junit-jupiter-engine                       | JUnit 5 engine for running tests.               |
| org.mockito                   | mockito-core                               | Mocking framework for unit tests.               |
| org.liquibase                 | liquibase-core                             | Database schema change management.              |
| org.springdoc                 | springdoc-openapi-starter-webmvc-ui        | OpenAPI documentation for Spring MVC.           |
| org.slf4j                     | slf4j-api                                  | Logging abstraction layer.                      |
| ch.qos.logback                | logback-classic                            | Logging framework compatible with SLF4J.        |
| io.jsonwebtoken               | jjwt-api                                   | JSON Web Token (JWT) library.                   |
| io.jsonwebtoken               | jjwt-impl                                  | Implementation for JWT library.                 |
| io.jsonwebtoken               | jjwt-jackson                               | Jackson integration for JWT.                    |
| org.springframework           | spring-websocket                           | WebSocket support in Spring.                    |
| org.springframework           | spring-messaging                           | Messaging capabilities in Spring.               |
| com.corundumstudio.socketio   | netty-socketio                             | WebSocket library with advanced features.       |




## Execution
Steps to run the project locally:

### 1. Clone the repository

```bash
git clone https://git.cs.dal.ca/courses/2024-summer/csci-5308/group04
```

### 2. Navigate to backend folder

```bash
Navigate to group04/backend
```

### 3. Build the project

```bash
mvn clean install
```

### 3. Run the application

```bash
mvn spring-boot:run
```
Finally, the application backend will start running locally.

# Frontend Setup

## Prerequisite
- **NodeJS**
- **npm**

## Dependencies


| Dependency             | Description                                                                                             | Version    |
|------------------------|---------------------------------------------------------------------------------------------------------|------------|
| @angular/animations    | Angular animation library for building animations in applications.                                      | ^17.3.0    |
| @angular/common        | Core Angular module with common directives and pipes.                                                   | ^17.3.0    |
| @angular/compiler      | Angular's compiler for compiling applications and libraries.                                            | ^17.3.0    |
| @angular/core          | Core functionalities for building Angular applications.                                                 | ^17.3.0    |
| @angular/forms         | Supports template-driven and reactive forms in Angular.                                                 | ^17.3.0    |
| @angular/platform-browse | Browser-related services and utilities for Angular.                                                   | ^17.3.0    |
| @angular/platform-browser-dynamic| Compiles Angular applications at runtime, primarily for development.                          | ^17.3.0    |
| @angular/router        | Angular's module for handling navigation and URL routing.                                               | ^17.3.0    |
| bootstrap              | Front-end framework for building responsive web applications.                                           | ^5.3.3     |
| chart.js               | JavaScript library for creating interactive charts.                                                     | ^4.4.3     |
| date-fns               | Modern JavaScript date utility library.                                                                 | ^3.6.0     |
| primeflex              | CSS utility library for layout and spacing in UI components.                                            | ^3.3.1     |
| primeicons             | Icon set used with PrimeNG UI components.                                                               | ^7.0.0     |
| primeng                | Rich UI component library for Angular applications.                                                     | ^17.17.0   |
| rxjs                   | Library for reactive programming with Observables, used in Angular.                                     | ~7.8.0     |
| socket.io-client       | Client library for real-time communication via WebSockets.                                              | ^4.7.5     |
| tslib                  | Runtime library for TypeScript, includes helper functions.                                              | ^2.3.0     |
| zone.js                | Execution context library to manage async operations in Angular.                                        | ~0.14.3    |


## Execution
Steps to run the project locally:

### 1. Clone the repository

```bash
git clone https://git.cs.dal.ca/courses/2024-summer/csci-5308/group04
```

### 2. Navigate to backend folder

```bash
Navigate to group04/frontend
```

### 3. Run the application

```bash
npm start
```
Finally, the application frontend will start running locally.


## Building The Backend Application

#### Spring Boot Application Deployment

Use a base image like Ubuntu or Alpine:
* `FROM ubuntu:latest`

Install Java:
* `RUN apt-get update && apt-get install -y default-jdk`

Install Nginx:
* `RUN apt-get install -y nginx`

Copy your Nginx configuration files and Java applications:
* `COPY frontend/Re-Connect/dist/re-connect /usr/share/nginx/html`
* `COPY backend/target/reconnect-0.0.1-SNAPSHOT.jar reconnect.jar`

Expose ports for Nginx and your Java application:
* `EXPOSE 80`
* `EXPOSE 8080`

#### Start Nginx and your Java application
* `CMD service nginx start && java -jar /reconnect.jar`

To build and run the container:

* `docker build -t reconnect .`

* `docker run -p 8080:8080 reconnect`

----------
## Building The Frontend Application

#### Node.js Application Deployment

Use an official Nginx image as the base image:
* `FROM nginx:latest`

Set the working directory inside the container:
* `WORKDIR /usr/share/nginx/html`

Remove default Nginx static assets:
* `RUN rm -rf ./*`

Copy the built Angular application files into the container:
* `COPY dist/re-connect .`

Expose port 80 to allow external access:
* `EXPOSE 80`

#### Start Nginx when the container starts
* `CMD ["nginx", "-g", "daemon off;"]`

------------
# GitLab CI/CD Pipeline Configuration

This `.gitlab-ci.yml` file defines a GitLab CI/CD pipeline with four stages: build, test, push, and deploy.

### Build Stage

#### build_frontend
- **Image Used:** `node:20`
- **Script Actions:**
  - Change directory to `./frontend/Re-Connect`.
  - Install npm dependencies with `npm install --legacy-peer-deps --prefer-offline`.
  - Run either `npm run build:production` for the main branch or `npm run build:development` for other branches.
- **Execution Condition:** Runs on merge requests and branches, triggered by changes in the frontend directory or `.gitlab-ci.yml`.

#### build_backend
- **Image Used:** `maven:3.8.5-openjdk-17`
- **Script Actions:**
  - Change directory to `./backend`.
  - Clean and package the backend application with `mvn clean package -DskipTests=true`.
- **Execution Condition:** Runs on merge requests and branches, triggered by changes in the backend directory or `.gitlab-ci.yml`.

### Test Stage

#### test_backend
- **Image Used:** `maven:3.8.5-openjdk-17`
- **Script Actions:**
  - Change directory to `./backend`.
  - Run the unit tests with `mvn test`.
- **Execution Condition:** Runs on merge requests and branches, triggered by changes in the backend directory or `.gitlab-ci.yml`.

### Push Stage

#### push_frontend
- **Image Used:** `docker:20`
- **Script Actions:**
  - Change directory to `./frontend/Re-Connect`.
  - Build and push the frontend Docker image.
- **Execution Condition:** Runs on the main and dev branches, triggered by changes in the frontend directory or `.gitlab-ci.yml`.

#### push_backend
- **Image Used:** `docker:20`
- **Script Actions:**
  - Change directory to `./backend`.
  - Build and push the backend Docker image.
- **Execution Condition:** Runs on the main and dev branches, triggered by changes in the backend directory or `.gitlab-ci.yml`.

### Deploy Stage

#### deploy_local_frontend
- **Image Used:** `docker:20`
- **Script Actions:**
  - Deploy the frontend Docker image to a local Docker environment.
- **Execution Condition:** Runs on the dev branch with the `SPRING_PROFILES_ACTIVE` variable set to "dev".

#### deploy_local_backend
- **Image Used:** `docker:20`
- **Script Actions:**
  - Deploy the backend Docker image to a local Docker environment.
- **Execution Condition:** Runs on the dev branch with the `SPRING_PROFILES_ACTIVE` variable set to "dev".

#### deploy_prod_frontend
- **Image Used:** `alpine:latest`
- **Script Actions:**
  - Deploy the frontend Docker image to a VM environment.
- **Execution Condition:** Runs on the main branch.

#### deploy_prod_backend
- **Image Used:** `alpine:latest`
- **Script Actions:**
  - Deploy the backend Docker image to a VM environment.
- **Execution Condition:** Runs on the main branch with the `SPRING_PROFILES_ACTIVE` variable set to "prod".

This pipeline ensures that the code is built, tested, pushed to a Docker registry, and then deployed to the appropriate environments in an automated manner.


# Testing

In this project, we are heavily focused on test cases, ensuring that almost every part of our services is covered. The coverage percentages for different areas are provided below.

- **Class percentage:**
  - 100
- **Method percentage:**
  - 100
- **Line percentage:**
  - 97


