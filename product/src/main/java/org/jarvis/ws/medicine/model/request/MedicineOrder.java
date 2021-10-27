package org.jarvis.ws.medicine.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jarvis.core.model.base.AnyJson;
import org.jarvis.core.model.base.SerializeCloneable;
import org.jarvis.ws.medicine.model.UserContext;

/**
 * Created: KimChheng
 * Date: 09-Nov-2020 Mon
 * Time: 10:29 AM
 */
public class MedicineOrder implements AnyJson, UserContext, SerializeCloneable {

    private static final long serialVersionUID = -8582141808545571088L;

    private Integer medicineId;
    private Integer orderId;
    private Integer quantity;
    private Float price;
    private Float vat;

    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    @JsonIgnore
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getVat() {
        return vat;
    }

    public void setVat(Float vat) {
        this.vat = vat;
    }
}
