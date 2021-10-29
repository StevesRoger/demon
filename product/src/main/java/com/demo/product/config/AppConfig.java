package com.demo.product.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
//@EnableScheduling
//@EnableAsync
public class AppConfig {

    @Value("${oauth2.remote.check-token-url}")
    private String checkTokenUrl;

    @Value("${oauth2.remote.client-id}")
    private String clientId;

    @Value("${oauth2.remote.client-secret}")
    private String clientSecret;

    @Bean
    public RemoteTokenServices tokenServices() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setCheckTokenEndpointUrl(checkTokenUrl);
        tokenServices.setClientId(clientId);
        tokenServices.setClientSecret(clientSecret);
        return tokenServices;
    }
}
