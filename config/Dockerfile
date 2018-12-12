FROM java:8

RUN mkdir -pv ${HOME}/Java/spring/spring-cloud/application-config/
ADD application-config/ ${HOME}/Java/spring/spring-cloud/application-config/

COPY config/build/libs/config-0.0.1-SNAPSHOT.jar config.jar

EXPOSE 8083
CMD java -jar config.jar
