package com.jarvis.app.auth.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jarvis.frmk.core.I18N;
import com.jarvis.frmk.core.exception.FatalException;
import com.jarvis.frmk.core.util.StringUtil;
import com.jarvis.frmk.hibernate.entity.audit.AuditIdentityEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created: KimChheng
 * Date: 28-Aug-2021 Sat
 * Time: 10:16 PM
 */
@Entity
@Table(name = "password_policy")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class PasswordPolicy extends AuditIdentityEntity<String> {

    private static final long serialVersionUID = 2147648178406747702L;

    private String policy;
    private String description;

    public PasswordPolicy() {
    }

    public PasswordPolicy(String policy) {
        this(policy, null);
    }

    public PasswordPolicy(String policy, String description) {
        this(null, policy, description);
    }

    public PasswordPolicy(String id, String policy, String description) {
        this.id = id;
        this.policy = policy;
        this.description = description;
    }

    @Column(name = "policy", nullable = false)
    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    @Column(name = "description", length = 200)
    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public PasswordPolicy isValid(String pwd) {
        if (StringUtil.isEmpty(pwd))
            throw FatalException.i18n("error.pwd.empty", "Password cannot be empty");
        else if (StringUtil.isEmpty(policy))
            throw FatalException.i18n("error.empty.policy", "Policy cannot be empty");
        else if (!pwd.matches(policy))
            throw new FatalException(I18N.getMessage("error.pwd8.policy"), "invalid PWD8 policy");
        return this;
    }
}
