package org.jarvis.ws.medicine.model.entity;

import org.jarvis.core.model.JsonFieldFilter;
import org.jarvis.ws.medicine.model.entity.enums.AlertType;
import org.jarvis.ws.medicine.model.entity.enums.Status;

import java.util.Date;

/**
 * Created: chheng
 * Date: 27-Jun-2020 Sat
 * Time: 15:11
 */
public class AlertEntity extends BaseEntity {

    private static final long serialVersionUID = 6908498744957898601L;

    private Date createdDate;
    private String createdBy;
    private String content;
    private String response;
    private Status status = Status.UNKNOWN;
    private AlertType alertType;

    public AlertEntity() {
    }

    public AlertEntity(String createdBy, String content) {
        this(createdBy, content, AlertType.NOTIFICATION);
    }

    public AlertEntity(String createdBy, String content, AlertType alertType) {
        this.createdBy = createdBy;
        this.content = content;
        this.alertType = alertType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
