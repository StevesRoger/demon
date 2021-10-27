package org.jarvis.ws.medicine.model.entity;

import org.jarvis.ws.medicine.model.entity.enums.Status;

import java.util.Date;
import java.util.List;

/**
 * Created: KimChheng
 * Date: 09-Nov-2020 Mon
 * Time: 2:32 PM
 */
public class OrderEntity extends BaseEntity {

    private static final long serialVersionUID = -7025462713606738086L;

    private Status status = Status.VALID;
    private String remark;
    private String referNo;
    private String currency;
    private Float total;
    private Float receive;
    private Float remaining;
    private Float change;
    private Date createdDate;
    private String createdBy;
    private Date modifiedDate;
    private String modifiedBy;
    private Integer customerId;
    private List<MedicineOrderEntity> orderItem;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReferNo() {
        return referNo;
    }

    public void setReferNo(String referNo) {
        this.referNo = referNo;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Float getReceive() {
        return receive;
    }

    public void setReceive(Float receive) {
        this.receive = receive;
    }

    public Float getRemaining() {
        return remaining;
    }

    public void setRemaining(Float remaining) {
        this.remaining = remaining;
    }

    public Float getChange() {
        return change;
    }

    public void setChange(Float change) {
        this.change = change;
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

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List<MedicineOrderEntity> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<MedicineOrderEntity> orderItem) {
        this.orderItem = orderItem;
    }
}
