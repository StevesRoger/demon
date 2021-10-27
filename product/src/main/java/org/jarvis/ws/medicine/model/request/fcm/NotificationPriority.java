package org.jarvis.ws.medicine.model.request.fcm;

/**
 * Created: KimChheng
 * Date: 24-Oct-2020 Sat
 * Time: 3:17 PM
 */
public enum NotificationPriority {
    HIGH("high"),
    NORMAL("normal");

    private String value;

    NotificationPriority(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
