package com.jarvis.app.auth.model.entity.ref;

/**
 * Created: KimChheng
 * Date: 20-Dec-2020 Sun
 * Time: 11:30 AM
 */
public enum AuthType {

    FACEBOOK("facebook"),
    GOOGLE("google"),
    GITHUB("github"),
    ACCOUNT("account");

    private String name;

    AuthType(String name) {
        this.name = name;
    }

    public static AuthType of(String name) {
        switch (name) {
            case "FACEBOOK":
                return FACEBOOK;
            case "GOOGLE":
                return GOOGLE;
            case "GITHUB":
                return GITHUB;
            case "ACCOUNT":
                return ACCOUNT;
            default:
                throw new IllegalStateException("Auth type " + name + " not found");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
