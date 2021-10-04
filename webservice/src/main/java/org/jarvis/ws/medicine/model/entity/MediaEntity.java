package org.jarvis.ws.medicine.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.jarvis.core.ICore;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.core.util.StringUtil;
import org.jarvis.ws.medicine.model.UserContext;
import org.jarvis.ws.medicine.model.entity.enums.MediaTypes;

import java.util.Date;
import java.util.Objects;

/**
 * Created: chheng
 * Date: 14-Jun-2020 Sun
 * Time: 09:45
 */

public class MediaEntity extends BaseEntity implements UserContext {

    private static final long serialVersionUID = 4649075935396458473L;

    private String link;
    private String status;
    private String publisher;
    private Date createdDate;
    private Date publishDate;
    private Date modifiedDate;
    private String createdBy;
    private String modifiedBy;
    private MediaTypes type;
    private String title;
    private Long width;
    private Long height;
    private String backgroundColor;
    private Boolean isSaved = false;
    private ImageEntity image;

    public MediaEntity() {
    }

    public MediaEntity(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy", timezone = ICore.ASIA_PHONE_PENH)
    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public MediaTypes getType() {
        return type;
    }

    public void setType(MediaTypes type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public ImageEntity getImage() {
        return image;
    }

    public void setImage(ImageEntity image) {
        this.image = image;
    }

    public Boolean getSaved() {
        return isSaved;
    }

    public void setSaved(Boolean saved) {
        isSaved = saved;
    }

    public static void buildUrl(MediaEntity entity) {
        ImageEntity image = entity.getImage();
        if (image != null && StringUtil.isEmpty(image.getUrl())) {
            String url = ContextUtil.getProperty("gateway.service.url");
            image.setUrl(url + "/api/image/" + image.getId());
        }
    }

    public static void buildDefaultWH(MediaEntity entity) {
        if (entity.getWidth() == null || entity.getWidth() < 0)
            entity.setWidth(300l);
        if (entity.getHeight() == null || entity.getHeight() < 0)
            entity.setHeight(100l);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj, "id", "link");
    }
}
