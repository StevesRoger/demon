package com.jarvis.app.auth.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jarvis.app.auth.model.entity.UserDevice;
import com.jarvis.frmk.core.model.base.SerializeCloneable;

/**
 * Created: KimChheng
 * Date: 24-Oct-2020 Sat
 * Time: 9:42 AM
 */
public class RegisterDevice implements SerializeCloneable {

    private static final long serialVersionUID = -1001530775524637857L;

    private String id;
    private String platform;
    private String token;
    private String model;
    private String appVersion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @JsonIgnore
    public UserDevice toEntity() {
        UserDevice userDevice = new UserDevice();
        userDevice.setId(id);
        userDevice.setToken(token);
        userDevice.setModel(model);
        userDevice.setPlatform(platform);
        userDevice.setAppVersion(appVersion);
        return userDevice;
    }
}
