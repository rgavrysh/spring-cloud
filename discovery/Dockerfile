FROM java:8

COPY discovery/build/libs/discovery-0.0.1-SNAPSHOT.jar discovery.jar

ENV SPRING_CLOUD_CONFIG_URI http://config:8083

CMD java -jar discovery.jar
