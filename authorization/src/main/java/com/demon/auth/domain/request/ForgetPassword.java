package com.demon.auth.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jarvis.frmk.core.ICore;
import com.jarvis.frmk.core.model.base.SerializeCloneable;

import java.util.Date;

/**
 * Created: chheng
 * Date: 05-Oct-2021 Tue
 * Time: 21:31
 */
public class ForgetPassword implements SerializeCloneable {

    private static final long serialVersionUID = -9062485763737169300L;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = ICore.ASIA_PHONE_PENH)
    private Date dob;
    private String username;

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
