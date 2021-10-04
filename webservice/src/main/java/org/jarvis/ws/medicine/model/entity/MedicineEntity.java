package org.jarvis.ws.medicine.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.time.DateUtils;
import org.jarvis.core.ICore;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.core.util.DateUtil;
import org.jarvis.ws.medicine.model.entity.enums.Status;
import org.jarvis.ws.medicine.repository.SystemRepository;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class MedicineEntity extends BaseEntity {

    private static final long serialVersionUID = -8029305034267739386L;

    private String name;
    private String nameKh;
    private String label;
    private String brandName;
    private String country;
    private String composition;
    private String desc;
    private Status status;
    private Float unitPrice;
    private Date createdDate;
    private Date modifiedDate;
    private String createdBy;
    private String modifiedBy;
    private String unitType;
    private Date deletedDate;
    private StockEntity stock;
    private Set<ImageEntity> images;
    private Integer recycleRemaining = 0;
   /* private float unitPriceImport;
    private Date expiryDate;
    private Date publishDate;*/

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
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

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    @JsonFormat(pattern = "MMM dd, yyyy", timezone = ICore.ASIA_PHONE_PENH)
    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Integer getRecycleRemaining() {
        return recycleRemaining;
    }

    public void setRecycleRemaining(Integer recycleRemaining) {
        this.recycleRemaining = recycleRemaining;
    }

    public StockEntity getStock() {
        return stock;
    }

    public void setStock(StockEntity stock) {
        this.stock = stock;
    }

    @JsonIgnore
    public Set<ImageEntity> getImages() {
        return images;
    }

    public void setImages(Set<ImageEntity> images) {
        this.images = images;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, label, brandName, country, composition);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && equals(obj, "name", "label", "brandName", "country", "composition");
    }

    @JsonIgnore
    public boolean isRecyclable() {
        if (deletedDate == null || !Status.RECYCLABLE.equals(status))
            return false;
        //2 week from deleted date
        String limitRecyclable = ContextUtil.getBean(SystemRepository.class).getConfig("recycle.day");
        Date deadLine = DateUtils.addDays(deletedDate, Integer.valueOf(limitRecyclable));
        Date today = new Date();
        if (DateUtils.isSameDay(today, deadLine))
            return false;
        else if (today.before(deadLine)) {
            recycleRemaining = DateUtil.betweenDates(today, deadLine).intValue();
            return true;
        } else if (today.after(deadLine))
            return false;
        return false;
    }
}
