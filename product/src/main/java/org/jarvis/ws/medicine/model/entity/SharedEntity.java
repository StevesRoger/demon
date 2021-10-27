package org.jarvis.ws.medicine.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jarvis.core.model.base.SerializeCloneable;
import org.jarvis.ws.medicine.model.entity.enums.Scope;

/**
 * Created: chheng
 * Date: 22-Jun-2020 Mon
 * Time: 21:00
 */
public class SharedEntity extends BaseEntity {

    private static final long serialVersionUID = 6633775286354620677L;

    private MedicineEntity medicineEntity = new MedicineEntity();
    private Integer ownerUserId;
    private Integer guestUserId;
    private Scope scope;

    public MedicineEntity getMedicineEntity() {
        return medicineEntity;
    }

    public void setMedicineEntity(MedicineEntity medicineEntity) {
        this.medicineEntity = medicineEntity;
    }

    @JsonIgnore
    public Integer getMedicineId() {
        return this.medicineEntity.getId();
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineEntity.setId(medicineId);
    }

    public Integer getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Integer ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public Integer getGuestUserId() {
        return guestUserId;
    }

    public void setGuestUserId(Integer guestUserId) {
        this.guestUserId = guestUserId;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "SharedEntity{" +
                "medicineId=" + medicineEntity.getId() +
                ", ownerUserId=" + ownerUserId +
                ", guestUserId=" + guestUserId +
                ", scope=" + scope +
                '}';
    }
}
