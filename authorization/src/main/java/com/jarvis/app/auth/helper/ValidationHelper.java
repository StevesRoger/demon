package com.jarvis.app.auth.helper;

import com.jarvis.app.auth.model.entity.UserAccount;
import com.jarvis.app.auth.model.request.UserRegister;
import com.jarvis.app.auth.repository.UserRepository;
import com.jarvis.frmk.core.I18N;
import com.jarvis.frmk.core.exception.FatalException;
import com.jarvis.frmk.core.util.AppContextUtil;

import java.util.Optional;

public interface ValidationHelper {

    UserRepository REPOSITORY = AppContextUtil.getBean(UserRepository.class);

    static void isEmailOrUsernameExist(UserRegister userRegister) {
        Optional<UserAccount> optUserAccount = REPOSITORY.findByEmail(userRegister.getEmail());
        if (optUserAccount.isPresent())
            throw new FatalException(I18N.getMessage("user.email.already.exist", userRegister.getEmail()), "user email already existed");
        optUserAccount = REPOSITORY.findByUserName(userRegister.getUserName());
        if (optUserAccount.isPresent())
            throw new FatalException(I18N.getMessage("user.name.already.exist", userRegister.getUserName()), "user name already existed");
    }
}
