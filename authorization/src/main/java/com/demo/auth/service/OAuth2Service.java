package com.demo.auth.service;

import com.demo.auth.component.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class OAuth2Service {

    @Autowired
    private AuthorizationServerEndpointsConfiguration oauth2Config;

    public OAuth2AccessToken getAccessToken(Map<String, String> parameter) {
        return getEndpointConfigure().getTokenGranter().grant(Const.RESOURCE_OWNER, createTokenRequest(parameter));
    }

    public OAuth2AccessToken refreshToken(Map<String, String> parameter) {
        TokenRequest tokenRequest = createTokenRequest(parameter);
        return getEndpointConfigure().getTokenServices().refreshAccessToken(parameter.get(Const.REFRESH_TOKEN), tokenRequest);
    }

    public Optional<OAuth2AccessToken> revokeToken(String token) {
        Optional<OAuth2AccessToken> optAuth = Optional.ofNullable(getTokenStore().readAccessToken(token));
        optAuth.ifPresent(obj -> getTokenService().revokeToken(obj.getValue()));
        return optAuth;
    }

    private ConsumerTokenServices getTokenService() {
        return getEndpointConfigure().getConsumerTokenServices();
    }

    private TokenStore getTokenStore() {
        return getEndpointConfigure().getTokenStore();
    }

    private ClientDetails getClientDetail(String clientId) {
        return getEndpointConfigure().getClientDetailsService().loadClientByClientId(clientId);
    }

    private TokenRequest createTokenRequest(Map<String, String> parameter) {
        ClientDetails clientDetails = getClientDetail(parameter.get(Const.CLIENT_ID));
        return getEndpointConfigure().getOAuth2RequestFactory().createTokenRequest(parameter, clientDetails);
    }

    private AuthorizationServerEndpointsConfigurer getEndpointConfigure() {
        return oauth2Config.getEndpointsConfigurer();
    }
}
