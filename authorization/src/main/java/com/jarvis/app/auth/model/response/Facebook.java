package com.jarvis.app.auth.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jarvis.app.auth.model.entity.ref.AuthType;
import com.jarvis.app.auth.model.request.RegisterDevice;
import org.json.JSONObject;

/**
 * Created: KimChheng
 * Date: 23-Dec-2020 Wed
 * Time: 10:28 AM
 */
public class Facebook implements ThirdPartyUserAccount {

    private static final long serialVersionUID = -8583737155882053708L;

    private String id;
    private String email;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private JSONObject picture;
    private String password;
    private RegisterDevice device;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public AuthType getAuthType() {
        return AuthType.FACEBOOK;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public RegisterDevice getDevice() {
        return device;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDevice(RegisterDevice device) {
        this.device = device;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public JSONObject getPicture() {
        return picture;
    }

    public void setPicture(JSONObject picture) {
        this.picture = picture;
    }
}
