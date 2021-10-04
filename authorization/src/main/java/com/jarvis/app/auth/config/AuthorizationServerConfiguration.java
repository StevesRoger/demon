package com.jarvis.app.auth.config;

import com.jarvis.app.auth.component.security.UserAuthenticationConverter;
import com.jarvis.app.auth.service.UserAccountDetailsService;
import com.jarvis.frmk.security.ISecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Created: KimChheng
 * Date: 04-Oct-2019 Fri
 * Time: 10:32 PM
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

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
    private WebResponseExceptionTranslator<OAuth2Exception> oauth2ResponseExceptionTranslator;

    @Autowired
    private TokenEnhancer tokenEnhancer;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Autowired
    private UserAccountDetailsService userAccountService;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security //.tokenKeyAccess("permitAll()")
                //.tokenKeyAccess("hasRole('ROLE_CLIENT_USER')")
                //.checkTokenAccess("hasRole('ROLE_CLIENT_SYSTEM')")
                .checkTokenAccess("isAuthenticated()")
                .realm(ISecurity.REALM_NAME)
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                //.addTokenEndpointAuthenticationFilter()
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        DefaultAccessTokenConverter accessTokenConverter = (DefaultAccessTokenConverter) endpoints.getAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(new UserAuthenticationConverter());
        endpoints.reuseRefreshTokens(false)
                .tokenStore(tokenStore)
                .userDetailsService(userAccountService)
                .authenticationManager(authenticationManager)
                .approvalStore(approvalStore)
                .authorizationCodeServices(authorizationCodeServices)
                .tokenEnhancer(tokenEnhancer)
                .exceptionTranslator(oauth2ResponseExceptionTranslator)
                .getFrameworkEndpointHandlerMapping()
                .setCorsConfigurationSource(corsConfigurationSource);
    }
}
