package com.jarvis.app.auth.model.request;

import com.jarvis.frmk.core.model.base.SerializeCloneable;

/**
 * Created: chheng
 * Date: 13-Jun-2020 Sat
 * Time: 21:47
 */
public class ChangePasswordRequest implements SerializeCloneable {

    private static final long serialVersionUID = -3839342801487001824L;
    private String email;
    private String currentPassword;
    private String newPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
