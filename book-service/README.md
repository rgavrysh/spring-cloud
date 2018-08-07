#Simple Rest Service

By default initial port is defined in properties. If port is unavailable the number is increasing by 1 up to 8100.
If desired service port not available, fail-fast logic presented by implementing EnvironmentPostProcessor, which is added to resources/META-INF/spring.factories file.
It allows run multiple identical instances on defined ports range as java -jar book-service-0.0.1-SNAPSHOT.jar.