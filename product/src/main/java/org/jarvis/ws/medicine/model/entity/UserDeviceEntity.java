package org.jarvis.ws.medicine.model.entity;

/**
 * Created: KimChheng
 * Date: 21-Apr-2021 Wed
 * Time: 5:13 PM
 */
public class UserDeviceEntity extends BaseEntity {

    private static final long serialVersionUID = -1049630249328931290L;

    private String deviceId;
    private String fcmToken;
    private String platform;
    private String appVersion;
    private String model;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
