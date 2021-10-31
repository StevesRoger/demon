package com.demo.auth.domain.request;


import com.demo.auth.domain.SerializeCloneable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Register implements SerializeCloneable {

    private static final long serialVersionUID = 1660457609872979370L;

    @NotNull
    private Integer customerId;
    private String username;
    private String password;
    private Method method;
    private String role;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
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

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
