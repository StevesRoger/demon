package com.demo.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import javax.annotation.PostConstruct;
import java.util.Date;


@SpringBootApplication
@EnableZuulProxy
@EnableEurekaServer
public class GatewayApp {

    private static final Logger logger = LoggerFactory.getLogger(GatewayApp.class);

    @PostConstruct
    public void init() {
        //TimeZone.setDefault(PHNOM_PENH_TIME_ZONE);
        logger.info("Spring boot application running in UTC timezone :" + new Date());
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApp.class, args);
    }

}
