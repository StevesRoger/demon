package org.jarvis.ws.medicine.config;

import org.jarvis.security.oauth2.JarvisOAuth2AuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Created: KimChheng
 * Date: 10-Oct-2019 Thu
 * Time: 9:49 PM
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    private JarvisOAuth2AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("ph-store-rest-api")
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);
        /*.accessDeniedHandler((req,resp,ex)->{
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        });*/
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .anonymous().disable()
                .httpBasic().and()
                .authorizeRequests()
                .antMatchers("/medicine/**", "/media/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/alert/send").hasAnyRole("ADMIN", "SYSTEM")
                .antMatchers(HttpMethod.GET, "/alert", "/badge").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE, "/badge").hasAnyRole("ADMIN", "USER")
                //.antMatchers("/medicine/**").permitAll()
                //.and().logout().permitAll().clearAuthentication(true).logoutUrl("/")
                .and().exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                /*.and().exceptionHandling().accessDeniedHandler(new JarvisAccessDeniedHandler())
                .authenticationEntryPoint((req, resp, ex) -> {
                    System.out.println(ex);
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                });*/
    }
}
