package com.jarvis.app.auth;

import com.jarvis.frmk.core.annotation.EnableErrorHandler;
import com.jarvis.frmk.core.annotation.EnableLogSuffix;
import com.jarvis.frmk.core.service.JdbcConfigService;
import com.jarvis.frmk.core.service.JdbcMessageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@EnableLogSuffix
@EnableErrorHandler
//@EntityReferInitialize
@Import(value = {JdbcMessageService.class, JdbcConfigService.class})
@SpringBootApplication
public class AuthApp {

    public static void main(String[] args) {
        SpringApplication.run(AuthApp.class, args);
    }


}
