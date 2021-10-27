package org.jarvis.ws.medicine.model.entity.enums;

/**
 * Created: chheng
 * Date: 08-Nov-2020 Sun
 * Time: 16:12
 */
public enum Scope {

    READ,
    WRITE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
