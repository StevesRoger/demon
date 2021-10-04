package com.jarvis.app.auth.model.request;

import com.jarvis.frmk.core.model.base.SerializeCloneable;

/**
 * Created: KimChheng
 * Date: 13-Oct-2019 Sun
 * Time: 10:29 PM
 */

public class ForgetPassword implements SerializeCloneable {

    private static final long serialVersionUID = -7420323539395067333L;

    private String email;
    private String newPassword;
    private String otpRefer;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOtpRefer() {
        return otpRefer;
    }

    public void setOtpRefer(String otpRefer) {
        this.otpRefer = otpRefer;
    }
}
