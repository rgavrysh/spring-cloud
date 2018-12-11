FROM java:8

COPY gateway/build/libs/gateway-0.0.1-SNAPSHOT.jar gateway.jar

ENV SPRING_PROFILES_ACTIVE zone1
ENV SPRING_CLOUD_CONFIG_URI http://config:8083
ENV EUREKA_CLIENT_SERVICEURL_ZONE1 http://discovery:8082/eureka/

CMD java -jar gateway.jar
