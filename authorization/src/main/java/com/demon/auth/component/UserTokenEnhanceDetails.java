package com.demon.auth.component;

import com.demon.auth.domain.response.AuthenticationAccessToken;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserTokenEnhanceDetails implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken token, OAuth2Authentication authentication) {
        DefaultExpiringOAuth2RefreshToken refreshToken = (DefaultExpiringOAuth2RefreshToken) token.getRefreshToken();
        Map<String, Object> additional = new HashMap<>();
        additional.put("issued_at", new Date());
        if (token.getExpiration() != null)
            additional.put("expires_date", token.getExpiration());
        if (refreshToken != null && refreshToken.getExpiration() != null) {
            Date expiration = refreshToken.getExpiration();
            int expired = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L).intValue();
            additional.put("refresh_token_expires_in", expired);
            additional.put("refresh_token_expires_date", expiration);
        }
        ((DefaultOAuth2AccessToken) token).setAdditionalInformation(additional);
        return new AuthenticationAccessToken(token, authentication);
    }
}
