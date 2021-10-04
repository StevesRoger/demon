package com.jarvis.app.auth.model.request;

import com.jarvis.frmk.core.model.base.SerializeCloneable;

import javax.validation.constraints.NotEmpty;

/**
 * Created: KimChheng
 * Date: 01-Dec-2020 Tue
 * Time: 4:24 PM
 */
public class LogoutRequest implements SerializeCloneable {

    private static final long serialVersionUID = 8008668984852233711L;

    @NotEmpty
    private String clientId;
    @NotEmpty
    private String username;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
