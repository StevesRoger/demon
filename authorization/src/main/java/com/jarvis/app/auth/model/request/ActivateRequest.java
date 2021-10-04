package com.jarvis.app.auth.model.request;

import com.jarvis.frmk.core.model.base.SerializeCloneable;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotEmpty;

/**
 * Created: KimChheng
 * Date: 06-Jun-2020 Sat
 * Time: 10:39
 */
@ApiModel
public class ActivateRequest implements SerializeCloneable {

    private static final long serialVersionUID = 3263760847779440187L;
    @NotEmpty
    private String otpCode;
    @NotEmpty
    private String otpRefer;

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getOtpRefer() {
        return otpRefer;
    }

    public void setOtpRefer(String otpRefer) {
        this.otpRefer = otpRefer;
    }
}
