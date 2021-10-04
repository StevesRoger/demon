package org.jarvis.ws.medicine.model.entity.enums;

/**
 * Created: KimChheng
 * Date: 11-Mar-2021 Thu
 * Time: 10:24 AM
 */
public enum AlertType {

    NOTIFICATION,
    ALERT,
    MAIL,
    SMS;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
