package com.demo.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


@SpringBootApplication
@EnableZuulProxy
public class GatewayApp {

    private static final Logger logger = LoggerFactory.getLogger(GatewayApp.class);

    public static void main(String[] args) {
        SpringApplication.run(GatewayApp.class, args);
    }
}
