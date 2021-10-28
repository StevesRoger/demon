package com.demon.auth.domain.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_account")
public class UserAccount extends AbstractEntity implements UserDetails {

    private static final long serialVersionUID = -1240427364614581089L;

    private String username;
    private String password;
    private Boolean enabled = true;
    private Boolean locked = false;
    private String lockReason;
    private Integer failedAttempt;
    private Date expiryPassword;
    private Date lastLogin;
    private Date lastLogout;
    private Status status = Status.ACTIVE;
    private Integer customerId;
    private Set<UserRole> roles = new HashSet<>();

    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(value = EnumType.STRING)
    public Status getStatus() {
        return status;
    }

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

    @Column(name = "username", nullable = false, length = 100)
    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Column(name = "customer_id", nullable = false)
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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
        return expiryPassword == null || expiryPassword.before(new Date());
    }

    @Transient
    @Override
    public boolean isEnabled() {
        return getEnabled();
    }

    @Override
    public String toString() {
        return username;
    }
}
