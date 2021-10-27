package com.demon.auth.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.demon.auth.domain.entity.UserAccount;
import com.demon.auth.domain.entity.UserPersonalInfo;
import com.jarvis.frmk.core.ICore;
import com.jarvis.frmk.core.annotation.ValidEmail;
import com.jarvis.frmk.core.model.base.SerializeCloneable;
import com.jarvis.frmk.core.util.StringUtil;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * Created: KimChheng
 * Date: 13-Oct-2019 Sun
 * Time: 10:29 PM
 */
public class UserRegister implements SerializeCloneable {

    private static final long serialVersionUID = -8283860787575626748L;

    @NotEmpty
    private String password;
    private String userName;
    private String pwdPolicyId;
    private Integer roleId;
    @ValidEmail
    private String email;
    private String fullName;
    private String firstName;
    private String middleName;
    private String lastName;
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = ICore.ASIA_PHONE_PENH)
    private Date dob;
    private String primaryPhone;
    private String secondaryPhone;
    private DeviceRegister device;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPwdPolicyId() {
        return pwdPolicyId;
    }

    public void setPwdPolicyId(String pwdPolicyId) {
        this.pwdPolicyId = pwdPolicyId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return StringUtil.isNotEmpty(fullName) ? fullName : firstName + " " + lastName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    public DeviceRegister getDevice() {
        return device;
    }

    public void setDevice(DeviceRegister device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return userName;
    }

    public UserPersonalInfo createUserPersonalInfo(UserAccount userAccount) {
        UserPersonalInfo info = new UserPersonalInfo(userAccount);
        info.setFullName(getFullName());
        info.setFirstName(firstName);
        info.setMiddleName(middleName);
        info.setLastName(lastName);
        info.setEmail(email);
        info.setPrimaryPhone(primaryPhone);
        info.setSecondaryPhone(secondaryPhone);
        info.setDob(dob);
        return info;
    }

    public UserDevice createUserDevice(UserAccount userAccount) {
        UserDevice device = new UserDevice(userAccount);
        device.setId(this.device.getId());
        device.setAppVersion(this.device.getAppVersion());
        device.setToken(this.device.getToken());
        device.setModel(this.device.getModel());
        device.setPlatform(this.device.getPlatform());
        return device;
    }
}
