package org.jarvis.ws.medicine.model.entity;

/**
 * Created: KimChheng
 * Date: 09-Nov-2020 Mon
 * Time: 2:40 PM
 */
public class MedicineOrderEntity extends BaseEntity {

    private static final long serialVersionUID = 2634273159718170874L;

    private Integer quantity;
    private Float unitPrice;
    private Float vat;
    private Integer supplyingId;
    private String medicineName;
    private String medicineNameKh;
    private String brand;
    private String unitType;
    private String label;
    private String composition;
    private Float discount;
    private String multiplyType;

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

    public Float getVat() {
        return vat;
    }

    public void setVat(Float vat) {
        this.vat = vat;
    }

    public Integer getSupplyingId() {
        return supplyingId;
    }

    public void setSupplyingId(Integer supplyingId) {
        this.supplyingId = supplyingId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineNameKh() {
        return medicineNameKh;
    }

    public void setMedicineNameKh(String medicineNameKh) {
        this.medicineNameKh = medicineNameKh;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public String getMultiplyType() {
        return multiplyType;
    }

    public void setMultiplyType(String multiplyType) {
        this.multiplyType = multiplyType;
    }
}
