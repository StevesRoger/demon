package com.jarvis.app.auth.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jarvis.frmk.core.model.JsonSnakeCase;
import com.jarvis.frmk.core.model.base.AnyJson;
import com.jarvis.frmk.core.model.base.AnyObject;
import com.jarvis.frmk.security.oauth2.IOAuth2;

import java.util.Map;

import static com.jarvis.frmk.security.oauth2.IOAuth2.RESOURCE_OWNER;

/**
 * Created: KimChheng
 * Date: 20-Dec-2020 Sun
 * Time: 11:39 AM
 */
public class PasswordGrant extends AnyObject implements JsonSnakeCase, AnyJson {

    private static final long serialVersionUID = -8955944931867905888L;

    private String clientId;
    private String clientSecret;
    private String password;
    private String username;
    private String clientInstanceId;
    private final String grantType = RESOURCE_OWNER;
    private RegisterDevice device;

    public PasswordGrant() {
    }

    public PasswordGrant(LoginRequest request) {
        this.clientId = request.getClientId();
        this.clientSecret = request.getClientSecret();
        this.password = request.getPassword();
        this.username = request.getUsername();
        this.device = request.getDevice();
        this.clientInstanceId = request.getClientInstanceId();
    }

    public PasswordGrant(Map<String, String> map) {
        this.clientId = map.get(IOAuth2.CLIENT_ID);
        this.clientSecret = map.get(IOAuth2.CLIENT_SECRET);
        this.username = map.get(IOAuth2.USERNAME);
        this.password = map.get(IOAuth2.PASSWORD);
        this.clientInstanceId = map.get("client_instance_id");
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGrantType() {
        return grantType;
    }

    @JsonIgnore
    public RegisterDevice getDevice() {
        return device;
    }

    public void setDevice(RegisterDevice device) {
        this.device = device;
    }

    public String getClientInstanceId() {
        return clientInstanceId;
    }

    public void setClientInstanceId(String clientInstanceId) {
        this.clientInstanceId = clientInstanceId;
    }
}
