package com.demo.product.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.client.RestTemplate;

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
        tokenServices.setRestTemplate(getRestTemplate());
        return tokenServices;
    }

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
