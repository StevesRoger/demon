package com.jarvis.app.auth.service;

import com.jarvis.app.auth.model.entity.Otp;
import com.jarvis.app.auth.model.entity.UserAccount;
import com.jarvis.app.auth.model.entity.ref.OtpType;
import com.jarvis.app.auth.repository.UserRepository;
import com.jarvis.frmk.core.I18N;
import com.jarvis.frmk.core.IRegex;
import com.jarvis.frmk.core.annotation.LogSlf4j;
import com.jarvis.frmk.core.log.LoggerJ;
import com.jarvis.frmk.core.model.http.response.ResponseJEntity;
import com.jarvis.frmk.core.util.DateUtil;
import com.jarvis.frmk.hibernate.entity.ref.Status;
import com.jarvis.frmk.security.exception.UserAccountNotActivateException;
import com.jarvis.frmk.security.exception.UserAccountNotFoundException;
import com.jarvis.frmk.security.exception.UserAccountProfileException;
import com.jarvis.frmk.security.exception.UserAccountSuspendException;
import com.jarvis.frmk.security.service.JarvisUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created: kim chheng
 * Date: 23-Mar-2020 Mon
 * Time: 11:28 AM
 */
@Service
public class UserAccountDetailsService implements JarvisUserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private OTPService otpService;

    @LogSlf4j
    private LoggerJ log;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccount> optUserAccount;
        if (IRegex.isValidEmailAddress(username)) {
            optUserAccount = repository.findByEmail(username);
            optUserAccount.orElseThrow(() -> new UserAccountNotFoundException(I18N.getMessage("user.email.not.found", "User not found")));
        } else {
            optUserAccount = repository.findByUserName(username);
            optUserAccount.orElseThrow(() -> new UserAccountNotFoundException(I18N.getMessage("user.not.found", username), "User not found"));
        }
        UserAccount userAccount = optUserAccount.get();
        if (Status.SUSPENDED.equals(userAccount.getStatus()))
            throw new UserAccountSuspendException(I18N.getMessage("user.account.suspended", username));
        if (!userAccount.getActivated())
            throw new UserAccountNotActivateException(I18N.getMessage("user.not.activate"), sendActivationOtp(userAccount));
        if (userAccount.getAuthorities().isEmpty())
            throw new UserAccountProfileException(I18N.getMessage("user.profile.not.found"), "User has no profile");
        return userAccount;
    }

    private ResponseJEntity sendActivationOtp(UserAccount userAccount) {
        log.info("user not activation yet");
        Otp otp = otpService.sendOTP(userAccount, OtpType.ACTIVATE);
        return ResponseJEntity.ok(I18N.getMessage("user.not.activate"))
                .jsonObject("otp_refer", otp.getId())
                .put("expiry_time", otp.getValidity())
                .put("expiry_date", DateUtil.format(otp.getExpiryDate()))
                .build();
    }
}
