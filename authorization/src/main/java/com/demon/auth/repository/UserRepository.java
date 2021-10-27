package com.demon.auth.repository;

import com.demon.auth.domain.entity.UserAccount;
import com.demon.auth.domain.entity.UserRole;
import com.jarvis.frmk.core.IRegex;
import com.jarvis.frmk.core.annotation.LogSlf4j;
import com.jarvis.frmk.core.exception.FatalException;
import com.jarvis.frmk.core.log.LoggerJ;
import com.jarvis.frmk.core.util.StringUtil;
import com.jarvis.frmk.hibernate.criterion.Conditions;
import com.jarvis.frmk.hibernate.criterion.JCriteria;
import com.jarvis.frmk.hibernate.entity.ref.AuthType;
import com.jarvis.frmk.hibernate.entity.ref.Status;
import com.jarvis.frmk.hibernate.repository.impl.AbstractEntityRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

/**
 * Created: KimChheng
 * Date: 26-Sep-2021 Sun
 * Time: 9:56 AM
 */
@Repository
public class UserRepository extends AbstractEntityRepository {

    @LogSlf4j
    private LoggerJ log;

    public UserRepository(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public Optional<UserAccount> findByEmail(String email) {
        return findByEmailAndType(email, AuthType.ACCOUNT);
    }

    public Optional<UserAccount> findByEmailAndType(String email, AuthType authType) {
        if (StringUtil.isEmpty(email) || !IRegex.isValidEmailAddress(email))
            throw FatalException.i18n("error.invalid.email", "Invalid email");
        log.info("find user account by email {}", email);
        JCriteria<UserAccount> criteria = new JCriteria<>(UserAccount.class);
        criteria.join("personalInfo", "info");
        criteria.condition(Conditions.equal("info.email", email));
        criteria.condition(Conditions.in("status", Status.ACTIVE, Status.SUSPENDED));
        if (authType != null) {
            log.info("auth type {}", authType);
            criteria.condition(Conditions.equal("authType", authType));
        }
        List<UserAccount> list = list(criteria);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public Optional<UserAccount> findByUserName(String username) {
        return findByUserNameAndType(username, AuthType.ACCOUNT);
    }

    public Optional<UserAccount> findByUserNameAndType(String username, AuthType authType) {
        log.info("find user account by username {}", username);
        JCriteria<UserAccount> criteria = new JCriteria<>(UserAccount.class);
        criteria.condition(Conditions.equal("username", username));
        criteria.condition(Conditions.in("status", Status.ACTIVE, Status.SUSPENDED));
        if (authType != null) {
            log.info("auth type {}", authType);
            criteria.condition(Conditions.equal("authType", authType));
        }
        List<UserAccount> list = list(criteria);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public Optional<PasswordPolicy> findPwdPolicyById(String id) {
        return Optional.ofNullable(getEntityById(PasswordPolicy.class, id));
    }

    public Optional<UserRole> findUserRoleByIdOrRole(Integer id, String role) {
        UserRole userRole = id != null ? getEntityById(UserRole.class, id) : getEntityByProperty(UserRole.class, "role", role);
        return Optional.ofNullable(userRole);
    }
}
