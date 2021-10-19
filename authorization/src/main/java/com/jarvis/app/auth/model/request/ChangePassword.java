package com.jarvis.app.auth.model.request;

import com.jarvis.frmk.core.model.base.SerializeCloneable;

/**
 * Created: chheng
 * Date: 13-Jun-2020 Sat
 * Time: 21:47
 */
public class ChangePassword implements SerializeCloneable {

    private static final long serialVersionUID = -3839342801487001824L;

    private String username;
    private String currentPassword;
    private String newPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
