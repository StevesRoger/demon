package com.jarvis.app.auth.model.request;

import com.jarvis.app.auth.model.entity.ref.AuthType;
import com.jarvis.frmk.core.model.base.SerializeCloneable;

import javax.validation.constraints.NotEmpty;

/**
 * Created: KimChheng
 * Date: 13-Oct-2019 Sun
 * Time: 10:29 PM
 */

public class LoginRequest implements SerializeCloneable {

    private static final long serialVersionUID = -38237840687081676L;
    @NotEmpty
    private String clientId;
    @NotEmpty
    private String clientSecret;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private String clientInstanceId;
    private AuthType type = AuthType.ACCOUNT;
    private RegisterDevice device;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RegisterDevice getDevice() {
        return device;
    }

    public void setDevice(RegisterDevice device) {
        this.device = device;
    }

    public AuthType getType() {
        return type;
    }

    public void setType(AuthType type) {
        this.type = type;
    }

    public String getClientInstanceId() {
        return clientInstanceId;
    }

    public void setClientInstanceId(String clientInstanceId) {
        this.clientInstanceId = clientInstanceId;
    }
}
