package com.home.microservices.gateway;

import com.home.microservices.gateway.filters.BookServiceZuulFilter;
import com.home.microservices.gateway.loadbalancer.bookservice.BookServiceLoadBalancerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableHystrixDashboard
@RibbonClients({
        @RibbonClient(name = "book-service", configuration = BookServiceLoadBalancerConfiguration.class)})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

//    @Bean
    public BookServiceZuulFilter bookServiceZuulFilter() {
        return new BookServiceZuulFilter();
    }
}
