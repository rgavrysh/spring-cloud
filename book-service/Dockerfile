FROM java:8

COPY book-service/build/libs/book-service-0.0.1-SNAPSHOT.jar book-service.jar

ENV SPRING_PROFILES_ACTIVE zone1
ENV SPRING_CLOUD_CONFIG_URI http://config:8083
ENV EUREKA_CLIENT_SERVICEURL_ZONE1 http://discovery:8082/eureka/

CMD java -jar book-service.jar
