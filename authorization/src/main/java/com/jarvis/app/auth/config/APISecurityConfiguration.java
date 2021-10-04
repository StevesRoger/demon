package com.jarvis.app.auth.config;

import com.jarvis.app.auth.component.security.UserAuthenticationProvider;
import com.jarvis.app.auth.service.AuthenticationService;
import com.jarvis.app.auth.service.UserAccountDetailsService;
import com.jarvis.frmk.security.oauth2.ClientDetailAuthenticationProvider;
import com.jarvis.frmk.security.oauth2.ClientSecretAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created: KimChheng
 * Date: 04-Oct-2019 Fri
 * Time: 10:32 PM
 */
@Order(1000)
@Configuration
@EnableWebSecurity
public class APISecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientDetailsUserDetailsService clientUserDetailService;

    @Autowired
    private UserAccountDetailsService userDetailsService;

    @Autowired
    private AuthenticationService authenticationService;

    /*@Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }*/

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        UserAuthenticationProvider userAccountProvider = new UserAuthenticationProvider(userDetailsService, passwordEncoder);
        userAccountProvider.setAuthenticationService(authenticationService);
        ClientDetailAuthenticationProvider clientDetailProvider = new ClientDetailAuthenticationProvider(clientUserDetailService, passwordEncoder);
        clientDetailProvider.setSupport(ClientSecretAuthenticationToken.class::equals);
        return new ProviderManager(Collections.unmodifiableList(Arrays.asList(clientDetailProvider, userAccountProvider)));
    }

    /*@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/oauth/**");
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .anonymous().disable()
                .authorizeRequests()
                .antMatchers("/system/**").hasAuthority("SYSTEM")
                .antMatchers("/user/**").hasAnyAuthority("USER", "SYSTEM")
                .and().httpBasic()
                .and().authenticationProvider(new ClientDetailAuthenticationProvider(clientUserDetailService, passwordEncoder))
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                //.defaultAuthenticationEntryPointFor(authenticationEntryPoint, new AntPathRequestMatcher("/system/**"))
                //.defaultAccessDeniedHandlerFor(accessDeniedHandler, new AntPathRequestMatcher("/system/**"))
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
