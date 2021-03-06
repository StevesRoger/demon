package com.demo.auth.domain.response;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class AuthenticationAccessToken extends DefaultOAuth2AccessToken {

    private OAuth2Authentication authentication;

    public AuthenticationAccessToken(String value) {
        super(value);
    }

    public AuthenticationAccessToken(OAuth2AccessToken accessToken) {
        super(accessToken);
    }

    public AuthenticationAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        this(accessToken);
        this.authentication = authentication;
    }

    public Authentication getAuthentication() {
        return authentication.getUserAuthentication();
    }

    public Object getPrincipal() {
        return authentication.getPrincipal();
    }
}
