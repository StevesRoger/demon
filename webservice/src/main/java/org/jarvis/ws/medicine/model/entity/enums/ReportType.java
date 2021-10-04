package org.jarvis.ws.medicine.model.entity.enums;

/**
 * Created: KimChheng
 * Date: 29-May-2021 Sat
 * Time: 10:34 PM
 */
public enum ReportType {

    SOLD,
    IMPORTED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
