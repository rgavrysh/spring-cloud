package com.home.microservices.bookservice.conf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.IOException;
import java.net.ServerSocket;

public class EnvironmentPortConfigurationPostProcessor implements EnvironmentPostProcessor {
    final int maxSupportedPort = 8100;

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        int port = Integer.valueOf(environment.getRequiredProperty("server.port"));
        if (port > maxSupportedPort) {
            throw new RuntimeException("Trying to run on port : " + port + ". " +
                    "This service can not be run on port higher than " + maxSupportedPort);
        }
        System.setProperty("server.port", String.valueOf(getAvailablePort(port)));
        System.out.println("---------------------ENV CUSTOM CONF-------------------------------");
        System.out.println("Assigned port is: " + environment.getProperty("server.port"));
    }

    private int getAvailablePort(int initialPort) {
        for (int p = initialPort; p < 8100; p++) {
            if (isPortAvailable(p)) {
                return p;
            }
        }
        throw new RuntimeException("No available ports in range " + initialPort + " - " + maxSupportedPort);
    }

    private boolean isPortAvailable(int port) {
        boolean isAvailable = false;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.close();
            isAvailable = true;
        } catch (IOException e) {
            System.out.println("Port " + port + " is used.");
        }
        return isAvailable;
    }
}
