package com.demon.auth.config;

import com.demon.auth.component.Const;
import com.demon.auth.component.RsaBCryptPasswordEncoder;
import com.demon.auth.component.UserTokenEnhanceDetails;
import com.demon.auth.domain.entity.UserAccount;
import com.demon.auth.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
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
import java.util.Arrays;
import java.util.Collections;

@EntityScan(basePackageClasses = {UserAccount.class})
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
    public ClientDetailsUserDetailsService clientDetailsUserDetailsService() throws Exception {
        return new ClientDetailsUserDetailsService(jdbcClientDetailsService());
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
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
