package com.jarvis.app.auth.model.request;

import com.jarvis.frmk.core.model.base.AnyJson;
import com.jarvis.frmk.security.oauth2.IOAuth2;

import javax.validation.constraints.NotEmpty;

/**
 * Created: KimChheng
 * Date: 13-Oct-2019 Sun
 * Time: 10:29 PM
 */

public class RefreshTokenRequest implements AnyJson {

    private static final long serialVersionUID = 6105694248439245708L;
    @NotEmpty
    private String clientId;
    @NotEmpty
    private String clientSecret;
    @NotEmpty
    private String refreshToken;

    private final String grantType = IOAuth2.REFRESH_TOKEN;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getGrantType() {
        return grantType;
    }
}
