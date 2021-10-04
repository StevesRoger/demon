package com.jarvis.app.auth.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jarvis.frmk.hibernate.entity.audit.AuditAutoGenerateEntity;
import com.jarvis.frmk.security.ISecurity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Objects;

/**
 * Created: kim chheng
 * Date: 23-Mar-2020 Mon
 * Time: 9:37 AM
 */
@Entity
@Table(name = "user_role")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class UserRole extends AuditAutoGenerateEntity<Integer> implements GrantedAuthority {

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
        return role == null ? null : role.startsWith(ISecurity.SPRING_PREF_ROLE) ? role : ISecurity.SPRING_PREF_ROLE + role;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + Objects.hash(role);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && equals(obj, "role");
    }

    @Override
    public String toString() {
        return role;
    }
}
