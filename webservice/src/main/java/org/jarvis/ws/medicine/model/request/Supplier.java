package org.jarvis.ws.medicine.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created: KimChheng
 * Date: 31-Oct-2020 Sat
 * Time: 10:08 AM
 */
public class Supplier extends BaseRequest {

    private static final long serialVersionUID = -3185214353862385073L;

    private String name;
    private String email;
    private String tel1;
    private String tel2;

    @JsonIgnore
    @Override
    public Integer getId() {
        return super.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }
}
