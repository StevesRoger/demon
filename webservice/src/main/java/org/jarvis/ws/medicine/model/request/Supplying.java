package org.jarvis.ws.medicine.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jarvis.core.ICore;

import java.util.Date;

/**
 * Created: KimChheng
 * Date: 31-Oct-2020 Sat
 * Time: 10:02 AM
 */
public class Supplying extends BaseRequest {

    private static final long serialVersionUID = -3446721140019951720L;

    private Integer medicineId;
    private Integer supplierId;
    private Integer quantity;
    private Integer remainingAlert;
    private Float unitPrice;
    private Float unitSellPrice;
    private String unitType;
    private Date expiryDate;
    private Date publishDate;

    public Supplying() {
    }

    public Supplying(MedicineCreate medicine) {
        this.medicineId = medicine.getId();
        this.supplierId = medicine.getSupplierId();
        this.quantity = medicine.getQuantity();
        this.unitPrice = medicine.getUnitPrice();
        this.unitType = medicine.getUnitType();
        this.expiryDate = medicine.getExpiryDate();
        this.publishDate = medicine.getPublishDate();
        this.remainingAlert = medicine.getRemainingAlert();
        this.unitSellPrice = medicine.getUnitSellPrice();
    }

    @JsonIgnore
    @Override
    public Integer getId() {
        return super.getId();
    }

    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
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

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
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

    public Integer getRemainingAlert() {
        return remainingAlert;
    }

    public void setRemainingAlert(Integer remainingAlert) {
        this.remainingAlert = remainingAlert;
    }

    public Float getUnitSellPrice() {
        return unitSellPrice == null || unitSellPrice == 0 ? getUnitPrice() : unitSellPrice;
    }

    public void setUnitSellPrice(Float unitSellPrice) {
        this.unitSellPrice = unitSellPrice;
    }
}
