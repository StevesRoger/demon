package org.jarvis.ws.medicine.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jarvis.ws.medicine.model.entity.StockEntity;

/**
 * Created: KimChheng
 * Date: 31-Oct-2020 Sat
 * Time: 10:29 AM
 */
public class MedicineStock extends BaseRequest {

    private static final long serialVersionUID = 5809008345540075646L;

    private Integer medicineId;
    private Integer postQuantity;
    private Integer preQuantity;
    private Integer remainingAlert;

    public MedicineStock() {
    }

    public static MedicineStock create(MedicineCreate medicine) {
        MedicineStock stock = new MedicineStock();
        stock.setMedicineId(medicine.getId());
        stock.setPostQuantity(medicine.getQuantity());
        stock.setRemainingAlert(medicine.getRemainingAlert());
        return stock;
    }

    public static MedicineStock supplying(Supplying supplying, StockEntity stockEntity) {
        MedicineStock stock = new MedicineStock();
        stock.setMedicineId(supplying.getMedicineId());
        stock.setPreQuantity(stockEntity.getPostQuantity());
        stock.setPostQuantity(supplying.getQuantity() + stockEntity.getPostQuantity());
        stock.setRemainingAlert(supplying.getRemainingAlert());
        return stock;
    }

    public static MedicineStock updateRemainingAlert(Integer medicineId, Integer remainingAlert) {
        MedicineStock stock = new MedicineStock();
        stock.setMedicineId(medicineId);
        stock.setRemainingAlert(remainingAlert);
        return stock;
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
}
