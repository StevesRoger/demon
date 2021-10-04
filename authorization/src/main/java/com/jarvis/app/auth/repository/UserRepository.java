package com.jarvis.app.auth.repository;

import com.jarvis.app.auth.model.entity.PasswordPolicy;
import com.jarvis.app.auth.model.entity.UserAccount;
import com.jarvis.frmk.core.IRegex;
import com.jarvis.frmk.core.annotation.LogSlf4j;
import com.jarvis.frmk.core.exception.FatalException;
import com.jarvis.frmk.core.log.LoggerJ;
import com.jarvis.frmk.core.util.StringUtil;
import com.jarvis.frmk.hibernate.criterion.Conditions;
import com.jarvis.frmk.hibernate.criterion.JCriteria;
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
        if (StringUtil.isEmpty(email) || !IRegex.isValidEmailAddress(email))
            throw FatalException.i18n("error.invalid.email", "Invalid email");
        log.info("find user account by email {}", email);
        JCriteria<UserAccount> criteria = new JCriteria<>(UserAccount.class);
        criteria.join("personalInfo");
        criteria.condition(Conditions.equal("personalInfo.email", email));
        criteria.condition(Conditions.in("status", Status.ACTIVE, Status.SUSPENDED));
        List<UserAccount> userAccounts = list(criteria);
        return userAccounts.isEmpty() ? Optional.empty() : Optional.of(userAccounts.get(0));
    }

    public Optional<UserAccount> findByUserName(String username) {
        UserAccount userAccount = getEntityByConditions(UserAccount.class,
                Conditions.equal("username", username),
                Conditions.in("status", Status.ACTIVE, Status.SUSPENDED));
        return Optional.ofNullable(userAccount);
    }

    public PasswordPolicy findPwdPolicyById(String id) {
        return getEntityById(PasswordPolicy.class, id);
    }
}
