package com.jarvis.app.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Created: chheng
 * Date: 19-Oct-2021 Tue
 * Time: 21:47
 */
@EnableWebSecurity
public class SecurityConfig {

    @Order(1)
    @Configuration
    public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private AccessDeniedHandler accessDeniedHandler;

        @Autowired
        private AuthenticationEntryPoint authenticationEntryPoint;

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers(
                    "/resources/static**",
                    "/v2/api-docs",
                    "/swagger-resources/**",
                    "/configuration/**",
                    "/webjars/**",
                    "/404", "/403");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and()
                    .csrf().disable()
                    .anonymous().disable()
                    .antMatcher("/system/**")
                    .antMatcher("/user/**")
                    .authorizeRequests()
                    .antMatchers("/system/**").hasAuthority("SYSTEM")
                    .antMatchers("/user/**").hasAnyAuthority("USER", "SYSTEM")
                    .and().httpBasic()
                    .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }

    @Order(2)
    @Configuration
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/", "/index", "/swagger", "/swagger-ui.html").hasAuthority("ADMIN")
                    .and().formLogin().permitAll()
                    .and().logout().clearAuthentication(true)
                    .invalidateHttpSession(true).permitAll()
                    .and().exceptionHandling().accessDeniedPage("/403");
        }
    }
}
