package com.jarvis.app.auth.helper;

import com.jarvis.app.auth.model.entity.UserAccount;
import com.jarvis.app.auth.model.request.RegisterUser;
import com.jarvis.app.auth.repository.UserRepository;
import com.jarvis.frmk.core.I18N;
import com.jarvis.frmk.core.IRegex;
import com.jarvis.frmk.core.exception.FatalException;
import com.jarvis.frmk.core.util.AppContextUtil;
import com.jarvis.frmk.core.util.StringUtil;
import com.jarvis.frmk.hibernate.entity.ref.Status;

import java.util.Optional;

public interface ValidationHelper {

    UserRepository REPOSITORY = AppContextUtil.getBean(UserRepository.class);

    static void isEmailOrUsernameExist(RegisterUser registerUser) {
        Optional<UserAccount> optUserAccount = REPOSITORY.findByEmail(registerUser.getEmail());
        if (optUserAccount.isPresent())
            throw new FatalException(I18N.getMessage("user.email.already.exist", registerUser.getEmail()), "user email already existed");
        optUserAccount = REPOSITORY.findByUserName(registerUser.getUserName());
        if (optUserAccount.isPresent())
            throw new FatalException(I18N.getMessage("user.name.already.exist", registerUser.getUserName()), "user name already existed");
    }

    static UserAccount validateUserAccount(String email) {
        Optional<UserAccount> optUserAccount = REPOSITORY.findByEmail(email);
        optUserAccount.orElseThrow(() -> FatalException.i18n("user.email.not.found", "User not found"));
        UserAccount userAccount = optUserAccount.get();
        if (Status.SUSPENDED.equals(userAccount.getStatus()))
            throw new FatalException(I18N.getMessage("user.account.suspended", userAccount.getUsername()), "user account suspended");
        if (userAccount.getAuthorities().isEmpty())
            throw FatalException.i18n("user.profile.not.found", "User has no profile");
        return userAccount;
    }
}
