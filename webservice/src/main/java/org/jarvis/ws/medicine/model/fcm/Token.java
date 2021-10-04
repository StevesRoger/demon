package org.jarvis.ws.medicine.model.fcm;


import org.jarvis.core.model.base.AnyObject;

public class Token extends AnyObject {

    private int id;
    private String token;
    private String status;
    private int badge;
    private String platform;

    public Token() {
    }

    public Token(String token, String platform) {
        this.token = token;
        this.platform = platform;
        this.status = "active";
        this.badge = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
