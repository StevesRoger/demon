package com.demo.auth.config;

import com.demo.auth.component.Const;
import com.demo.auth.component.RsaBCryptPasswordEncoder;
import com.demo.auth.component.UserTokenEnhanceDetails;
import com.demo.auth.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

@EnableTransactionManagement
@Configuration
public class AppConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public RsaBCryptPasswordEncoder passwordEncoder() throws Exception {
        PrivateKey privateKey = SecurityUtil.readPrivateKey(getClass().getResourceAsStream("/rsa-key/private-key.pem"));
        RSAPublicKey publicKey = SecurityUtil.readPublicKey(getClass().getResourceAsStream("/rsa-key/public-key.pem"));
        RsaBCryptPasswordEncoder passwordEncoder = new RsaBCryptPasswordEncoder();
        passwordEncoder.setPrivateKey(privateKey);
        passwordEncoder.setPublicKey(publicKey);
        return passwordEncoder;
    }

    /*@Bean
    public Sampler sampler(){
        return Sampler.ALWAYS_SAMPLE;
    }*/

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new UserTokenEnhanceDetails();
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public ClientDetailsService jdbcClientDetailsService() throws Exception {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder());
        return clientDetailsService;
    }

    @Bean
    public CorsConfigurationSource corsConfig() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedHeaders(Arrays.asList(Const.CORS_ALLOW_HEADERS.split(",")));
        config.setAllowedMethods(Arrays.asList(Const.CORS_ALLOW_METHODS.split(",")));
        config.setExposedHeaders(Arrays.asList(Const.CORS_EXPOSE_HEADERS.split(",")));
        config.setMaxAge(3600L);
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
