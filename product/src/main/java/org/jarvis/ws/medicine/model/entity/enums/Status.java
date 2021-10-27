package org.jarvis.ws.medicine.model.entity.enums;

public enum Status {
    UNKNOWN,
    ACTIVE,
    INACTIVE,
    RECYCLABLE,
    CREATED,
    SENT,
    INVALID,
    VALID,
    PAID,
    SUCCESSFUL,
    FAILED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
