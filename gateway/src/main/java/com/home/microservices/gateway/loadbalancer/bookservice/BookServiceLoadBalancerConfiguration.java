package com.home.microservices.gateway.loadbalancer.bookservice;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;


public class BookServiceLoadBalancerConfiguration {
    @Autowired
    private IClientConfig ribbonClientConfig;

    @Autowired
    private ILoadBalancer loadBalancer;

    @Bean
    public IPing ribbonPing(IClientConfig config) {
        return new PingUrl(false, "/");
    }

    public IRule ribbonRule(IClientConfig iClientConfig) {
        return new RoundRobinRule();
    }
}
