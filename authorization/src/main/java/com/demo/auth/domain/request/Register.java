package com.demo.auth.domain.request;


import com.demo.auth.domain.SerializeCloneable;

import javax.validation.constraints.NotEmpty;

public class Register implements SerializeCloneable {

    private static final long serialVersionUID = 1660457609872979370L;

    private int customerId;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String role;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
