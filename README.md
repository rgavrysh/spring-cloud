### Status
[![Build Status](https://travis-ci.org/rgavrysh/spring-cloud.png?branch=master)](https://travis-ci.org/rgavrysh/spring-cloud)

# Spring-Cloud POC

This project is based on Baeldung article, and shows the simplest example how to build multiservice cloud based application.
It consist of 5 different parts:
- application configuration - keeps configurations for every module
- config-server - manages configuration for all our applications
- discovery-server (eureka) - serve as an application address lookup, so each new application communicate with discovery-server and register its address so others can communicate with it
- gateway-server (zuul) - act as a proxy shuttling requests from clients to backend, also it allows all responses to originate from single host. (CORS, Authentication ...)
- custom-services - our services (in this example book-service, rating-service)

The next step would be:
- containerize application
- travis build
- authorization
