package org.jarvis.ws.medicine.model.request.fcm;

import org.jarvis.core.model.JsonExcludeNullField;
import org.jarvis.core.model.JsonSnakeCase;
import org.jarvis.core.model.base.AnyObject;
import org.jarvis.ws.medicine.model.entity.enums.AlertType;
import org.jarvis.ws.medicine.model.request.Alert;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Notification extends AnyObject implements JsonExcludeNullField, JsonSnakeCase {

    private static final long serialVersionUID = -3285325509275357019L;

    private String to;
    private String condition;
    private NotificationPriority priority = NotificationPriority.NORMAL;
    private NotificationBody notification = new NotificationBody();
    private Map<String, Object> data = new LinkedHashMap<>();

    public Notification() {
    }

    @SuppressWarnings("unchecked")
    public Notification(String to, Alert alert) {
        this.to = to;
        this.notification.setTitle(alert.getTitle());
        this.notification.setBody(alert.getMessage());
        this.notification.setSound(alert.getSound());
        this.notification.setIcon(alert.getIcon());
        this.notification.setColor(alert.getColor());
        this.data = (Map<String, Object>) alert.getUnknownFields().get("data");
        if (data == null)
            this.data = new HashMap<>();
        this.data.putIfAbsent("type", alert.getType().toString());
        this.data.putIfAbsent("created_by", alert.getCreatedBy());
        this.data.putIfAbsent("user_id", alert.getUserId());
        this.data.putIfAbsent("seen", AlertType.ALERT.equals(alert.getType()));
    }

    public Notification(String to, String title, String message) {
        this.to = to;
        this.notification.setTitle(title);
        this.notification.setBody(message);
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public NotificationPriority getPriority() {
        return priority;
    }

    public void setPriority(NotificationPriority priority) {
        this.priority = priority;
    }

    public NotificationBody getNotification() {
        return notification;
    }

    public void setNotification(NotificationBody notification) {
        this.notification = notification;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}