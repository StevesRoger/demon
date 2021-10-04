package com.jarvis.app.auth.service;

import com.jarvis.app.auth.Consts;
import com.jarvis.app.auth.model.entity.Otp;
import com.jarvis.app.auth.model.entity.UserAccount;
import com.jarvis.app.auth.model.entity.UserPersonalInfo;
import com.jarvis.app.auth.model.entity.ref.OtpType;
import com.jarvis.frmk.core.ICore;
import com.jarvis.frmk.core.annotation.LogSlf4j;
import com.jarvis.frmk.core.exception.FatalException;
import com.jarvis.frmk.core.log.LoggerJ;
import com.jarvis.frmk.core.service.ConfigService;
import com.jarvis.frmk.core.util.AppContextUtil;
import com.jarvis.frmk.core.util.DateUtil;
import com.jarvis.frmk.core.util.StringUtil;
import com.jarvis.frmk.hibernate.repository.EntityRepository;
import com.jarvis.frmk.security.RSASecurityProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class OTPService {

    @Autowired
    private EntityRepository repository;

    @Autowired
    private MailService mailService;

    @Autowired
    private ConfigService sysConfig;

    @Autowired
    private PasswordEncoder pwdEncoder;

    @Autowired
    private RSASecurityProvider rsaSecurity;

    @LogSlf4j
    private LoggerJ log;

    private static final String OTP_VALIDITY = "otp.validity.";

    @Transactional(rollbackFor = Exception.class)
    public Otp sendOTP(UserAccount userAccount, OtpType otpType) {
        log.info("send otp");
        UserPersonalInfo personalInfo = userAccount.getPersonalInfo();
        String fullName = personalInfo.getFullName();
        fullName = StringUtil.isNotEmpty(fullName) ? fullName : personalInfo.getFirstName() + " " + personalInfo.getLastName();
        String otpLength = sysConfig.getValue(Consts.SYS_OTP_LENGTH, "6");
        String characters = sysConfig.getValue(Consts.SYS_OTP_CHARACTERS, ICore.NUMBERS);
        String otpCode = StringUtil.random(Integer.parseInt(otpLength), characters);
        String sysOtpValidity = sysConfig.getValue(Consts.SYS_DEFAULT_OTP_VALIDITY, "3");
        String otpValidity = sysConfig.getValue(OTP_VALIDITY + otpType.toString().toLowerCase(), sysOtpValidity);
        int validity = Integer.parseInt(otpValidity);
        int expiryInSecond = (int) TimeUnit.SECONDS.convert(validity, TimeUnit.MINUTES);
        long expiryInMilliSecond = TimeUnit.MILLISECONDS.convert(validity, TimeUnit.MINUTES);
        Date expiryAfter = DateUtil.withoutMilliseconds(new Date(System.currentTimeMillis() + expiryInMilliSecond));
        Otp otp = new Otp();
        otp.setDetail(userAccount.getId().toString().getBytes(ICore.CHARSET_UTF_8));
        otp.setDestination(personalInfo.getEmail());
        otp.setCode(isDevelop() ? otpCode : pwdEncoder.encode(otpCode));
        otp.setExpiryDate(expiryAfter);
        otp.setValidity(expiryInSecond);
        otp.setType(otpType);
        repository.save(otp);
        mailService.send(fullName, otp.getDestination(), otpCode);
        if (log.isDebugEnabled()) {
            log.debug("to:{}", otp.getDestination());
            log.debug("otp refer:{}", otp.getId());
            log.debug("validity:{}", otp.getValidity());
            log.debug("expired after:{}", DateUtil.format(expiryAfter));
        }
        log.info("send otp successful");
        return otp;
    }

    @Transactional(rollbackFor = Exception.class)
    public Otp verify(String otpRefer, String otpCode) throws GeneralSecurityException, UnsupportedEncodingException {
        log.info("verify otp");
        log.info("otp refer:" + otpRefer);
        String rawCode = rsaSecurity.decryptText(otpCode.trim());
        Otp entity = repository.getEntityById(Otp.class, otpRefer);
        if (entity == null)
            throw FatalException.i18n("error.invalid.otp", "invalid otp reference");
        if (!isMatch(rawCode, entity.getCode()))
            throw FatalException.i18n("error.invalid.otp", "otp code mismatch");
        if (entity.isExpired())
            throw FatalException.i18n("error.otp.expired", "otp code expired");
        if (entity.getVerified())
            throw FatalException.i18n("error.invalid.otp", "otp already verified");
        entity.setVerified(true);
        log.info("verify otp successful");
        return entity;
    }

    private boolean isDevelop() {
        return "dev".equals(AppContextUtil.getActiveProfile());
    }

    private boolean isMatch(String rawCode, String code) {
        return isDevelop() ? rawCode.equals(code) : pwdEncoder.matches(rawCode, code);
    }
}
