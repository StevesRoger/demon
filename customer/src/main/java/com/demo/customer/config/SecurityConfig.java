package com.demo.customer.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${auth-url}")
    private String authBaseUrl;

    @Value("${oauth2.remote.client-id}")
    private String clientId;

    @Value("${oauth2.remote.client-secret}")
    private String clientSecret;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/actuator/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .anonymous().disable()
                .requestMatchers()
                .antMatchers("/register")
                .and().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/register")
                .hasAnyAuthority("MOBILE", "WEB")
                .and().httpBasic()
                .and().authenticationProvider(remoteAuthenticationProvider());
    }

    @SuppressWarnings("unchecked")
    private AuthenticationProvider remoteAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder() {
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                try {
                    HttpHeaders header = new HttpHeaders();
                    header.setBasicAuth(clientId, clientSecret);
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
                    form.add("raw", rawPassword.toString());
                    form.add("encode", encodedPassword);
                    ResponseEntity<Map> response = restTemplate.postForEntity(authBaseUrl + "/system/check_password", new HttpEntity<>(form, header), Map.class);
                    return HttpStatus.OK.equals(response.getStatusCode());
                } catch (RestClientException e) {
                    return false;
                }
            }
        });
        provider.setUserDetailsService((username) -> {
            try {
                HttpHeaders header = new HttpHeaders();
                header.setBasicAuth(clientId, clientSecret);
                Map<String, Object> body = restTemplate.exchange(authBaseUrl + "/system/" + username, HttpMethod.GET, new HttpEntity<>(header), Map.class).getBody();
                List<SimpleGrantedAuthority> authorities = ((List<String>) body.get("authorities"))
                        .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                return new User(username, body.get("password").toString(), authorities);
            } catch (RestClientException e) {
                throw new UsernameNotFoundException(e.getMessage());
            }
        });
        return provider;
    }
}
