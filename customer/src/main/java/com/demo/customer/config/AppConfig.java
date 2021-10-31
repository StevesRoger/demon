package com.demo.customer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

import java.util.Map;

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
        DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
        tokenConverter.setUserTokenConverter(new DefaultUserAuthenticationConverter() {
            @Override
            public Authentication extractAuthentication(Map<String, ?> map) {
                UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) super.extractAuthentication(map);
                authentication.setDetails(map);
                return authentication;
            }
        });
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setCheckTokenEndpointUrl(checkTokenUrl);
        tokenServices.setClientId(clientId);
        tokenServices.setClientSecret(clientSecret);
        tokenServices.setAccessTokenConverter(tokenConverter);
        return tokenServices;
    }
}
