package com.demon.auth.helper;

import com.demon.auth.domain.entity.UserAccount;
import com.demon.auth.domain.request.UserRegister;
import com.demon.auth.repository.UserRepository;
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
