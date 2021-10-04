package org.jarvis.ws.medicine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Date;


@SpringBootApplication
public class MedicineApplication {

    @PostConstruct
    public void init() {
       // TimeZone.setDefault(PHNOM_PENH_TIME_ZONE);
        System.out.println("Spring boot application running in UTC timezone :" + new Date());
    }

    public static void main(String[] args) {
        SpringApplication.run(MedicineApplication.class, args);
    }

}
