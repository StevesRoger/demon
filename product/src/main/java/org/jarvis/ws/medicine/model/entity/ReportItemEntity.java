package org.jarvis.ws.medicine.model.entity;

import org.jarvis.core.model.JsonExcludeNullField;
import org.jarvis.ws.medicine.model.entity.enums.ReportType;

import java.util.Date;

/**
 * Created: KimChheng
 * Date: 20-Nov-2020 Fri
 * Time: 8:04 PM
 */
public class ReportItemEntity extends BaseEntity implements JsonExcludeNullField {

    private ReportType type;
    private String label;
    private String name;
    private String nameKh;
    private String brand;
    private String unitType;
    private String composition;
    private Integer quantity;
    private Float unitPrice;
    private Date createdDate;
    private Date expiryDate;
    private Date publishDate;

    public void addQuantity(Integer qty) {
        this.quantity += qty;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameKh() {
        return nameKh;
    }

    public void setNameKh(String nameKh) {
        this.nameKh = nameKh;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public boolean equals(Object that) {
        return super.equals(that, "id", "unitPrice");
    }
}
