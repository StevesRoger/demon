package com.jarvis.app.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created: KimChheng
 * Date: 13-Oct-2019 Sun
 * Time: 10:09 PM
 */
@Order(2000)
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

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
                .authorizeRequests()
                .antMatchers("/", "/index", "/swagger", "/swagger-ui.html").hasAuthority("ADMIN")
                .and().formLogin().permitAll()
                .and().logout().clearAuthentication(true).invalidateHttpSession(true).permitAll()
                .and().exceptionHandling().accessDeniedPage("/403");
    }
}
