package com.jarvis.app.auth.service;

import com.jarvis.app.auth.model.response.AuthenticationAccessToken;
import com.jarvis.frmk.security.oauth2.IOAuth2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OAuth2Service {

    @Autowired
    private AuthorizationServerEndpointsConfiguration oauth2Config;


    public OAuth2AccessToken getAccessToken(Map<String, String> parameter) {
        return getEndpointConfigure().getTokenGranter().grant(IOAuth2.RESOURCE_OWNER, createTokenRequest(parameter));
    }

    public OAuth2AccessToken refreshToken(Map<String, String> parameter) {
        TokenRequest tokenRequest = createTokenRequest(parameter);
        return getEndpointConfigure().getTokenServices().refreshAccessToken(parameter.get(IOAuth2.REFRESH_TOKEN), tokenRequest);
    }

    public Optional<OAuth2AccessToken> revokeToken(String token) {
        Optional<OAuth2AccessToken> optAuth = Optional.ofNullable(getTokenStore().readAccessToken(token));
        optAuth.ifPresent(obj -> getTokenService().revokeToken(obj.getValue()));
        return optAuth;
    }

    public List<OAuth2AccessToken> revokeUserToken(String clientId, String username) {
        Collection<OAuth2AccessToken> tokens = listTokens(clientId, username);
        tokens.forEach(token -> getTokenService().revokeToken(token.getValue()));
        return (List<OAuth2AccessToken>) tokens;
    }

    public Collection<OAuth2AccessToken> listTokens(String clientId) {
        return getTokenStore().findTokensByClientId(clientId);
    }

    public Collection<OAuth2AccessToken> listTokens(String clientId, String username) {
        return getTokenStore().findTokensByClientIdAndUserName(clientId, username);
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
        ClientDetails clientDetails = getClientDetail(parameter.get(IOAuth2.CLIENT_ID));
        return getEndpointConfigure().getOAuth2RequestFactory().createTokenRequest(parameter, clientDetails);
    }

    private AuthorizationServerEndpointsConfigurer getEndpointConfigure() {
        return oauth2Config.getEndpointsConfigurer();
    }
}
