package com.home.microservices.gateway.loadbalancer.bookservice;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;


public class BookServiceLoadBalancerConfiguration {
    @Autowired
    private IClientConfig ribbonClientConfig;

    @Autowired
    private ILoadBalancer ribbonLoadBalancer;

    @Bean
    public IPing ribbonPing(IClientConfig config) {
        return new PingUrl(false, "/");
    }

    @Bean
    public ServerListFilter ribbonServerListFilter(IClientConfig config) {
        return new ZoneAffinityServerListFilter<>(config);
    }

    @Bean
    public IRule ribbonRule(IClientConfig config) {
        System.out.println("new random rule");
        return new RoundRobinRule();
    }
}
