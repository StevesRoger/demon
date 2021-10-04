package org.jarvis.ws.medicine.model.entity;

import org.jarvis.ws.medicine.model.entity.enums.AlertType;

import java.util.Date;

/**
 * Created: KimChheng
 * Date: 20-Apr-2021 Tue
 * Time: 4:46 PM
 */
public class InboxEntity extends BaseEntity {

    private static final long serialVersionUID = 870847971240342023L;

    private String title;
    private String message;
    private Date createdDate;
    private String createdBy;
    private Boolean seen;
    private Integer userId;
    private AlertType type;

    public InboxEntity() {
    }

    public InboxEntity(String title, String message) {
        this(title, message, null);
    }

    public InboxEntity(String title, String message, String createdBy) {
        this.title = title;
        this.message = message;
        this.createdBy = createdBy;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public AlertType getType() {
        return type;
    }

    public void setType(AlertType type) {
        this.type = type;
    }
}
