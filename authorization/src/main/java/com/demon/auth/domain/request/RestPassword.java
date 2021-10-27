package com.demon.auth.domain.request;

import com.jarvis.frmk.core.model.base.SerializeCloneable;

/**
 * Created: KimChheng
 * Date: 13-Oct-2019 Sun
 * Time: 10:29 PM
 */

public class RestPassword implements SerializeCloneable {

    private static final long serialVersionUID = -7420323539395067333L;

    private String newPassword;
    private String otpCode;
    private String otpRefer;

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
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
