package com.jarvis.app.auth.service;

import com.jarvis.app.auth.model.request.PasswordGrant;
import com.jarvis.app.auth.model.request.RefreshTokenRequest;
import com.jarvis.frmk.security.oauth2.ClientSecretAuthenticationToken;
import com.jarvis.frmk.security.oauth2.IOAuth2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OAuth2Service {

    @Autowired
    private AuthorizationServerEndpointsConfiguration oauth2Config;

    @Autowired
    private AuthenticationManager authenticationManager;

    @SuppressWarnings("unchecked")
    public OAuth2AccessToken getAccessToken(PasswordGrant passwordGrant) {
        authenticationManager.authenticate(new ClientSecretAuthenticationToken(passwordGrant.getClientId(), passwordGrant.getClientSecret()));
        TokenGranter tokenGranter = getEndpointConfigure().getTokenGranter();
        ClientDetails clientDetails = getClientDetail(passwordGrant.getClientId());
        TokenRequest tokenRequest = createTokenRequest(passwordGrant.toMap(), clientDetails);
        return tokenGranter.grant(IOAuth2.RESOURCE_OWNER, tokenRequest);
    }

    @SuppressWarnings("unchecked")
    public OAuth2AccessToken refreshToken(RefreshTokenRequest request) {
        authenticationManager.authenticate(new ClientSecretAuthenticationToken(request.getClientId(), request.getClientSecret()));
        ClientDetails clientDetails = getClientDetail(request.getClientId());
        TokenRequest tokenRequest = createTokenRequest(request.toMap(), clientDetails);
        return getEndpointConfigure().getTokenServices().refreshAccessToken(request.getRefreshToken(), tokenRequest);
    }

    public List<OAuth2AccessToken> listTokens(String clientId) {
        return new ArrayList<>(getTokenStore().findTokensByClientId(clientId));
    }

    public List<OAuth2AccessToken> listTokens(String clientId, String username) {
        return new ArrayList<>(getTokenStore().findTokensByClientIdAndUserName(clientId, username));
    }

    public OAuth2AccessToken revokeToken(String token) {
        OAuth2AccessToken auth = getTokenStore().readAccessToken(token);
        Optional.ofNullable(auth).ifPresent(obj -> getTokenService().revokeToken(obj.getValue()));
        return auth;
    }

    public List<OAuth2AccessToken> revokeUserToken(String clientId, String username) {
        List<OAuth2AccessToken> tokens = listTokens(clientId, username);
        tokens.forEach(token -> getTokenService().revokeToken(token.getValue()));
        return tokens;
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

    private TokenRequest createTokenRequest(Map<String, Object> request, ClientDetails clientDetails) {
        Map<String, String> parameter = new HashMap<>();
        request.forEach((k, v) -> parameter.put(k, String.valueOf(v)));
        return getEndpointConfigure().getOAuth2RequestFactory().createTokenRequest(parameter, clientDetails);
    }

    private AuthorizationServerEndpointsConfigurer getEndpointConfigure() {
        return oauth2Config.getEndpointsConfigurer();
    }
}
