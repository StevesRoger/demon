package org.jarvis.ws.medicine.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * Created: KimChheng
 * Date: 20-Nov-2020 Fri
 * Time: 4:16 PM
 */
public class ReportEntity extends BaseEntity implements Comparable<ReportEntity> {

    private static final long serialVersionUID = -6505454189868872182L;
    private String remark;
    private Date createdDate;
    private Float totalOrder = 0.0f;
    private Float totalSupplying = 0.0f;
    private Map<String, ReportItemEntity> itemMap = new HashMap<>();
    private List<ReportItemEntity> items;

    @JsonIgnore
    @Override
    public Integer getId() {
        return super.getId();
    }

    public void addTotalOrder(float total) {
        this.totalOrder += total;
    }

    public void addTotalSupplying(float total) {
        this.totalSupplying += total;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Float getTotalSupplying() {
        return totalSupplying;
    }

    public void setTotalSupplying(Float totalSupplying) {
        this.totalSupplying = totalSupplying;
    }

    public Float getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(Float totalOrder) {
        this.totalOrder = totalOrder;
    }

    public List<ReportItemEntity> getItems() {
        if (items == null)
            items = new ArrayList<>(itemMap.values());
        return items;
    }

    public ReportItemEntity putIfAbsent(String id, ReportItemEntity item) {
        return itemMap.putIfAbsent(id, item);
    }

    @Override
    public boolean equals(Object that) {
        return super.equals(that, "id");
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, createdDate);
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @Override
    public int compareTo(ReportEntity other) {
        return other.getCreatedDate().compareTo(createdDate);
    }
}
