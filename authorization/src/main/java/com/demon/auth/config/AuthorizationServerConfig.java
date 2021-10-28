package com.demon.auth.config;

import com.demon.auth.service.UserAccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    @Qualifier("jdbcClientDetailsService")
    private ClientDetailsService clientDetailsService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ApprovalStore approvalStore;

    @Autowired
    private TokenEnhancer tokenEnhancer;

    @Autowired
    private UserAccountDetailsService userDetailService;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.reuseRefreshTokens(false)
                .tokenStore(tokenStore)
                .tokenEnhancer(tokenEnhancer)
                .userDetailsService(userDetailService)
                .authenticationManager(authenticationManager)
                .approvalStore(approvalStore)
                .authorizationCodeServices(authorizationCodeServices)
                .getFrameworkEndpointHandlerMapping();
    }
}
