package org.jarvis.ws.medicine.model.request;

import org.jarvis.core.model.JsonSnakeCase;
import org.jarvis.core.model.http.request.JsonUnknownProperties;
import org.jarvis.ws.medicine.model.entity.enums.AlertType;

/**
 * Created: KimChheng
 * Date: 21-Apr-2021 Wed
 * Time: 2:33 PM
 */
public class Alert extends JsonUnknownProperties implements JsonSnakeCase {

    private static final long serialVersionUID = 3566932470070971875L;

    private Integer userId;
    private String topic;
    private String createdBy = "system";
    private String title;
    private String message;
    private String sound = "default";
    private String color;
    private String icon;
    private AlertType type = AlertType.NOTIFICATION;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public AlertType getType() {
        return type;
    }

    public void setType(AlertType type) {
        this.type = type;
    }
}
