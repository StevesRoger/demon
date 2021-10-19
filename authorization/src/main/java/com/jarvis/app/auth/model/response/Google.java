package com.jarvis.app.auth.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jarvis.app.auth.model.entity.UserDevice;
import com.jarvis.frmk.hibernate.entity.ref.AuthType;

/**
 * Created: KimChheng
 * Date: 23-Dec-2020 Wed
 * Time: 9:07 PM
 */
public class Google implements SocialMediaUserDetails {

    private static final long serialVersionUID = -5448546661305335260L;

    @JsonProperty("sub")
    private String id;
    private String name;
    @JsonProperty("given_name")
    private String firstName;
    @JsonProperty("family_name")
    private String lastName;
    private String email;
    private String picture;
    private String password;
    private UserDevice device;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String getFullName() {
        return name;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public AuthType getType() {
        return AuthType.GOOGLE;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public UserDevice getDevice() {
        return device;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDevice(UserDevice device) {
        this.device = device;
    }

}
