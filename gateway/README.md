#Zuul Gateway Service

This service works as an gateway proxy server, routes to other services defined in gateway.properties from config server.
Additionally, Hystrix CircuitBreaker and Dashboard added. For book-service FallbackProvider implemented, which works for up-to 100 requests.
Dashboard usually available by endpoint http://localhost:8080/hystrix.metrix