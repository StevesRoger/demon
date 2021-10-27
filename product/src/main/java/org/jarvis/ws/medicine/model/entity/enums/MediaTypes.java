package org.jarvis.ws.medicine.model.entity.enums;

public enum MediaTypes {
    NEWS,
    ADVERTISE,
    UNKNOWN;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
