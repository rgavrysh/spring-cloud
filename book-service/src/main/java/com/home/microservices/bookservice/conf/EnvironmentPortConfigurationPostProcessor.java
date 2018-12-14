package com.home.microservices.bookservice.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.IOException;
import java.net.ServerSocket;

public class EnvironmentPortConfigurationPostProcessor implements EnvironmentPostProcessor {
    private static final int MAX_SUPPORTED_PORT = 8100;
    private final Logger logger = LoggerFactory.getLogger(EnvironmentPortConfigurationPostProcessor.class);


    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        final String serverPortVariable = "server.port";
        int port = Integer.parseInt(environment.getRequiredProperty(serverPortVariable));
        if (port > MAX_SUPPORTED_PORT) {
            throw new IllegalStateException("Trying to run on port : " + port + ". " +
                    "This service can not be run on port higher than " + MAX_SUPPORTED_PORT);
        }
        System.setProperty(serverPortVariable, String.valueOf(getAvailablePort(port)));
        logger.info("---------------------ENV CUSTOM CONF-------------------------------");
        logger.info("Assigned port is: {}", environment.getProperty(serverPortVariable));
    }

    private int getAvailablePort(int initialPort) {
        for (int p = initialPort; p < 8100; p++) {
            if (isPortAvailable(p)) {
                return p;
            }
        }
        throw new IllegalStateException("No available ports in range " + initialPort + " - " + MAX_SUPPORTED_PORT);
    }

    private boolean isPortAvailable(int port) {
        boolean isAvailable = false;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            isAvailable = true;
        } catch (IOException e) {
            logger.error("Port {} is used.", port);
        }
        return isAvailable;
    }
}
