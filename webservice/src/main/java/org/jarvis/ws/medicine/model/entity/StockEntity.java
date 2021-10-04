package org.jarvis.ws.medicine.model.entity;

import org.jarvis.core.I18N;
import org.jarvis.ws.medicine.exception.StockMovementException;
import org.jarvis.ws.medicine.model.request.MedicineOrder;
import org.jarvis.ws.medicine.model.request.MedicineStock;

import java.util.Date;

public class StockEntity extends BaseEntity {

    private static final long serialVersionUID = -3655980111912228071L;

    private Integer postQuantity;
    private Integer preQuantity;
    private Integer remainingAlert;
    private Date createdDate;
    private Date modifiedDate;
    private String createdBy;
    private String modifiedBy;
    private Integer medicineId;

    public Integer getPostQuantity() {
        return postQuantity;
    }

    public void setPostQuantity(Integer postQuantity) {
        this.postQuantity = postQuantity;
    }

    public Integer getPreQuantity() {
        return preQuantity;
    }

    public void setPreQuantity(Integer preQuantity) {
        this.preQuantity = preQuantity;
    }

    public Integer getRemainingAlert() {
        return remainingAlert;
    }

    public void setRemainingAlert(Integer remainingAlert) {
        this.remainingAlert = remainingAlert;
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

    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public MedicineStock movement(MedicineOrder order) {
        if (postQuantity < order.getQuantity())
            throw new StockMovementException(I18N.getMessage("stock.movement.fatal"), "Stock id " + getId() + " quantity smaller then order quantity");
        int postQty = postQuantity;
        postQty -= order.getQuantity();
        int preQty = postQty + order.getQuantity();
        MedicineStock medicineStock = new MedicineStock();
        medicineStock.setPostQuantity(postQty);
        medicineStock.setPreQuantity(preQty);
        medicineStock.setMedicineId(medicineId);
        return medicineStock;
    }
}
