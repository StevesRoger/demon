package com.demo.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@SpringBootApplication
@EnableEurekaServer
public class RegistryApp {

    private static final Logger logger = LoggerFactory.getLogger(RegistryApp.class);

    public static void main(String[] args) {
        SpringApplication.run(RegistryApp.class, args);
    }
}
