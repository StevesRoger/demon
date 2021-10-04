package org.jarvis.ws.medicine.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jarvis.core.ICore;

import java.util.Date;

/**
 * Created: kim chheng
 * Date: 27-Sep-2019 Fri
 * Time: 8:51 PM
 */
public class MedicineCreate extends BaseRequest {

    private static final long serialVersionUID = 3955610799156463268L;

    private String name;
    private String nameKh;
    private String label;
    private String brandName;
    private String country;
    private String composition;
    private String desc;
    private Integer quantity;
    private Float unitPrice;
    private Float unitSellPrice;
    private Integer remainingAlert;
    private String unitType;
    private Date expiryDate;
    private Date publishDate;
    private Integer supplierId;

    @JsonIgnore
    @Override
    public Integer getId() {
        return super.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNameKh() {
        return nameKh;
    }

    public void setNameKh(String nameKh) {
        this.nameKh = nameKh;
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

    public Float getUnitSellPrice() {
        return unitSellPrice == null || unitSellPrice == 0 ? getUnitPrice() : unitSellPrice;
    }

    public void setUnitSellPrice(Float unitSellPrice) {
        this.unitSellPrice = unitSellPrice;
    }

    public Integer getRemainingAlert() {
        return remainingAlert;
    }

    public void setRemainingAlert(Integer remainingAlert) {
        this.remainingAlert = remainingAlert;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = ICore.ASIA_PHONE_PENH)
    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = ICore.ASIA_PHONE_PENH)
    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }
}
