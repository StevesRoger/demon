package com.jarvis.app.auth.config;

import com.jarvis.app.auth.service.security.UserAccountDetailsService;
import com.jarvis.app.auth.service.security.UserSocialMediaService;
import com.jarvis.frmk.security.ISecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
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
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

/**
 * Created: KimChheng
 * Date: 04-Oct-2019 Fri
 * Time: 10:32 PM
 */
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
    private UserAccountDetailsService userDetailService;

    @Autowired
    private UserSocialMediaService socialMediaService;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security //.tokenKeyAccess("permitAll()")
                //.tokenKeyAccess("hasRole('ROLE_CLIENT_USER')")
                //.checkTokenAccess("hasRole('ROLE_CLIENT_SYSTEM')")
                //.checkTokenAccess("hasAnyAuthority('USER')")
                .checkTokenAccess("isAuthenticated()")
                .realm(ISecurity.REALM_NAME)
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .allowFormAuthenticationForClients();
        //.addTokenEndpointAuthenticationFilter(new JarvisClientCredentialsTokenEndpointFilter());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.reuseRefreshTokens(false)
                .tokenStore(tokenStore)
                .tokenServices(tokenServices())
                .userDetailsService(userDetailService)
                .authenticationManager(authenticationManager)
                .approvalStore(approvalStore)
                .authorizationCodeServices(authorizationCodeServices)
                .tokenEnhancer(tokenEnhancer)
                .exceptionTranslator(oauth2ResponseExceptionTranslator)
                .getFrameworkEndpointHandlerMapping()
                .setCorsConfigurationSource(corsConfigurationSource);
        /*DefaultAccessTokenConverter accessTokenConverter = (DefaultAccessTokenConverter) endpoints.getAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(new UserAuthenticationConverter());*/
    }

    private AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(false);
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setTokenEnhancer(tokenEnhancer);
        PreAuthenticatedAuthenticationProvider preSocialMediaProvider = new PreAuthenticatedAuthenticationProvider();
        preSocialMediaProvider.setPreAuthenticatedUserDetailsService(socialMediaService);
        ProviderManager providerManager = new ProviderManager(Collections.singletonList(preSocialMediaProvider));
        tokenServices.setAuthenticationManager(providerManager);
        return tokenServices;
    }
}
