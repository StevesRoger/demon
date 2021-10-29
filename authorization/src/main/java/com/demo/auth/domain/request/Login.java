package com.demo.auth.domain.request;

import com.demo.auth.component.Const;
import com.demo.auth.domain.SerializeCloneable;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

public class Login implements SerializeCloneable {

    private static final long serialVersionUID = -38237840687081676L;

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

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

    @JsonIgnore
    public Map<String, String> passwordGrant(String clientId, String secret) {
        Map<String, String> map = new HashMap<>();
        map.put(Const.CLIENT_ID, clientId);
        map.put(Const.CLIENT_SECRET, secret);
        map.put(Const.PASSWORD, password);
        map.put(Const.USERNAME, username);
        map.put(Const.GRANT_TYPE, Const.RESOURCE_OWNER);
        return map;
    }
}
