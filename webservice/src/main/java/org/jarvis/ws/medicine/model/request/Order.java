package org.jarvis.ws.medicine.model.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.jarvis.core.util.DateUtil;
import org.jarvis.ws.medicine.model.entity.enums.Currency;
import org.jarvis.ws.medicine.model.entity.enums.Status;

import java.util.Date;
import java.util.List;

/**
 * Created: KimChheng
 * Date: 09-Nov-2020 Mon
 * Time: 10:19 AM
 */
public class Order extends BaseRequest {

    private static final long serialVersionUID = -8426111870371913842L;

    private String remark;
    private String referNumber;
    private Float receiveAmount;
    private Float remainingAmount;
    private Float change;
    private Integer customerId;
    private Status status = Status.VALID;
    private Currency currency = Currency.USD;
    private List<MedicineOrder> orderItems;

    public Float getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(Float receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public Float getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Float remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Float getChange() {
        return change;
    }

    public void setChange(Float change) {
        this.change = change;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getReferNumber() {
        return referNumber == null ? DateUtil.format(new Date(), "yyyyMMddHHmm") : referNumber;
    }

    public void setReferNumber(String referNumber) {
        this.referNumber = referNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @JsonIgnore
    public List<MedicineOrder> getOrderItems() {
        return orderItems;
    }

    @JsonSetter
    public void setOrderItems(List<MedicineOrder> orderItems) {
        this.orderItems = orderItems;
    }

    @JsonGetter
    public Float getTotalAmount() {
        Float total = 0.0f;
        for (MedicineOrder item : orderItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
}
