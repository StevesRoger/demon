package org.jarvis.ws.medicine.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jarvis.ws.medicine.model.entity.enums.Status;

/**
 * Created: KimChheng
 * Date: 29-Oct-2020 Thu
 * Time: 3:54 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicineUpdate extends BaseRequest {

    private static final long serialVersionUID = -3916034914859810434L;

    private Status status;
    private String name;
    private String nameKh;
    private String label;
    private String brandName;
    private String country;
    private String composition;
    private String desc;
    private Float unitPrice;
    private String unitType;
    private Integer remainingAlert;
    private Integer quantity;

    public MedicineUpdate() {
    }

    public MedicineUpdate(Integer id, Status status) {
        this.id = id;
        this.status = status;
    }

    public MedicineUpdate(Supplying supplying) {
        this.unitPrice = supplying.getUnitSellPrice();
        this.id = supplying.getMedicineId();
        this.unitType = supplying.getUnitType();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public Integer getRemainingAlert() {
        return remainingAlert;
    }

    public void setRemainingAlert(Integer remainingAlert) {
        this.remainingAlert = remainingAlert;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
