package com.demon.auth.service.security;

import com.demon.auth.domain.entity.UserAccount;
import com.demon.auth.repository.UserRepository;
import com.jarvis.frmk.core.I18N;
import com.jarvis.frmk.core.IRegex;
import com.jarvis.frmk.hibernate.entity.ref.Status;
import com.jarvis.frmk.security.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created: KimChheng
 * Date: 20-Dec-2020 Sun
 * Time: 11:37 AM
 */
@Service
public class UserAccountDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount;
        if (IRegex.isValidEmailAddress(username))
            userAccount = repository.findByEmail(username).orElseThrow(() ->
                    new UserAccountNotFoundException(I18N.getMessage("user.email.not.found", "User not found")));
        else
            userAccount = repository.findByUserName(username).orElseThrow(() ->
                    new UserAccountNotFoundException(I18N.getMessage("user.not.found", username), "User not found"));
        if (Status.SUSPENDED.equals(userAccount.getStatus()))
            throw new UserAccountSuspendException(I18N.getMessage("user.account.suspended", username));
        if (!userAccount.getActivated())
            throw new UserAccountNotActivateException(I18N.getMessage("user.not.activate"), "User not yet activate").detail(userAccount);
        if (!userAccount.isEnabled())
            throw new UserAccountInvalidException(I18N.getMessage("error.user.disable"), "User is disable");
        if (userAccount.getAuthorities().isEmpty())
            throw new UserAccountProfileException(I18N.getMessage("user.profile.not.found"), "User has no profile");
        return userAccount;
    }
}
