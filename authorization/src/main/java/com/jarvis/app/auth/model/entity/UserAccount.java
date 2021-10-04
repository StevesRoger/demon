package com.jarvis.app.auth.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jarvis.frmk.hibernate.entity.audit.AuditAutoGenerateEntity;
import com.jarvis.frmk.hibernate.entity.base.RecyclableEntity;
import com.jarvis.frmk.hibernate.entity.converter.AuthTypeConverter;
import com.jarvis.frmk.hibernate.entity.converter.StatusConverter;
import com.jarvis.frmk.hibernate.entity.ref.AuthType;
import com.jarvis.frmk.hibernate.entity.ref.Status;
import com.jarvis.frmk.security.userdetails.UserAccountDetails;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.*;

/**
 * Created: kim chheng
 * Date: 23-Mar-2020 Mon
 * Time: 9:37 AM
 */
@Entity
@Table(name = "user_account")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class UserAccount extends AuditAutoGenerateEntity<Integer> implements RecyclableEntity, UserAccountDetails {

    private static final long serialVersionUID = -1240427364614581089L;

    private String username;
    private String password;
    private Boolean enabled = false;
    private Boolean activated = false;
    private Boolean locked = false;
    private String lockReason;
    private Integer failedAttempt;
    private Date expiryPassword;
    private Date lastLogin;
    private Date lastLogout;
    private AuthType authType;
    private Status status;
    private UserPersonalInfo personalInfo;
    private UserDevice userDevice;
    private PasswordPolicy passwordPolicy;
    private Set<UserRole> roles = new HashSet<>();

    @JsonManagedReference
    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    public UserPersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(UserPersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    @JsonManagedReference
    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    public UserDevice getUserDevice() {
        return userDevice;
    }

    public void setUserDevice(UserDevice userDevice) {
        this.userDevice = userDevice;
    }

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "pwd_policy_id", referencedColumnName = "id")
    public PasswordPolicy getPasswordPolicy() {
        return passwordPolicy;
    }

    public void setPasswordPolicy(PasswordPolicy passwordPolicy) {
        this.passwordPolicy = passwordPolicy;
    }

    @Convert(converter = AuthTypeConverter.class)
    @Column(name = "auth_type", length = 20)
    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    @Convert(converter = StatusConverter.class)
    @Column(name = "status", length = 20)
    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_account_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    public Set<UserRole> getRoles() {
        return roles == null ? new HashSet<>() : roles;
    }

    public void setRoles(Set<UserRole> profiles) {
        this.roles = profiles;
    }

    @Column(name = "username", nullable = false, length = 20)
    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password", nullable = false)
    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "activated")
    @Override
    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login")
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_logout")
    public Date getLastLogout() {
        return lastLogout;
    }

    public void setLastLogout(Date lastLogout) {
        this.lastLogout = lastLogout;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_password")
    public Date getExpiryPassword() {
        return expiryPassword;
    }

    public void setExpiryPassword(Date expiryPassword) {
        this.expiryPassword = expiryPassword;
    }

    @Column(name = "enable")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Column(name = "locked")
    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    @Column(name = "locked_reason", length = 50)
    public String getLockReason() {
        return lockReason;
    }

    public void setLockReason(String lockReason) {
        this.lockReason = lockReason;
    }

    @Column(name = "failed_attempt")
    public Integer getFailedAttempt() {
        return failedAttempt;
    }

    public void setFailedAttempt(Integer failedAttempt) {
        this.failedAttempt = failedAttempt;
    }

    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return getLocked() == null || !getLocked();
    }

    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        if (expiryPassword == null)
            return true;
        return expiryPassword.before(new Date());
    }

    @Transient
    @Override
    public boolean isEnabled() {
        return getEnabled();
    }

    @Override
    public int hashCode() {
        return super.hashCode() + Objects.hash(username);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && equals(obj, "username");
    }

    @Override
    public String toString() {
        return username;
    }
}
