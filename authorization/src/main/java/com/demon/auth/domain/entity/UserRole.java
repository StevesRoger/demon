package com.demon.auth.domain.entity;

import com.demon.auth.component.Const;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "user_role")
public class UserRole extends AbstractEntity implements GrantedAuthority {

    private static final long serialVersionUID = -6922310965036631952L;

    private String role;
    private String description;

    public UserRole() {
    }

    public UserRole(String role, String desc) {
        this.role = role;
        this.description = desc;
    }

    @Column(name = "role", nullable = false, length = 20)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Column(name = "description", length = 50)
    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    @Transient
    @Override
    public String getAuthority() {
        return role == null ? null : role.startsWith(Const.SPRING_PREF_ROLE) ? role : Const.SPRING_PREF_ROLE + role;
    }

    @Override
    public String toString() {
        return role;
    }
}
