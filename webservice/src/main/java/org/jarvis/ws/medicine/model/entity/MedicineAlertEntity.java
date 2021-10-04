package org.jarvis.ws.medicine.model.entity;

/**
 * Created: KimChheng
 * Date: 25-Oct-2020 Sun
 * Time: 10:42 AM
 */
public class MedicineAlertEntity extends BaseEntity {
    private static final long serialVersionUID = 1745929043259368674L;

    private String name;
    private String nameKh;
    private Integer postQuantity;
    private Integer preQuantity;
    private Integer remainingAlert;
    private Integer userId;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
