package com.demon.auth.service;

import com.jarvis.app.auth.component.Consts;
import com.demon.auth.domain.entity.UserAccount;
import com.demon.auth.domain.entity.ref.OtpType;
import com.demon.auth.repository.UserRepository;
import com.jarvis.frmk.core.I18N;
import com.jarvis.frmk.core.ICore;
import com.jarvis.frmk.core.annotation.LogSlf4j;
import com.jarvis.frmk.core.exception.FatalException;
import com.jarvis.frmk.core.log.LoggerJ;
import com.jarvis.frmk.core.model.http.response.ResponseJEntity;
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
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    @Autowired
    private UserRepository userRepo;

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
    public Otp sendOtp(String to, String fullName, String detail, OtpType otpType) {
        log.info("send otp");
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
        otp.setDestination(to);
        otp.setCode(isDevelop() ? otpCode : pwdEncoder.encode(otpCode));
        otp.setExpiryDate(expiryAfter);
        otp.setValidity(expiryInSecond);
        otp.setType(otpType);
        if (StringUtil.isNotEmpty(detail))
            otp.setDetail(detail.getBytes(ICore.CHARSET_UTF_8));
        repository.save(otp);
        mailService.send(fullName, to, otpCode);
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
    public Otp verify(String otpRefer, String otpCode, OtpType otpType) throws GeneralSecurityException, UnsupportedEncodingException {
        log.info("verify otp");
        log.info("otp refer:" + otpRefer);
        String rawCode = rsaSecurity.decryptText(otpCode.trim());
        Otp entity = repository.getEntityById(Otp.class, otpRefer);
        if (entity == null)
            throw FatalException.i18n("error.invalid.otp", "invalid otp reference");
        if (!isMatch(rawCode, entity.getCode()))
            throw FatalException.i18n("error.invalid.otp", "otp code mismatch");
        if (!otpType.equals(entity.getType()))
            throw new FatalException(I18N.getMessage("error.invalid.otp.type", otpType.toString()), "invalid otp type");
        if (entity.isExpired())
            throw FatalException.i18n("error.otp.expired", "otp code expired");
        if (entity.getVerified())
            throw FatalException.i18n("error.invalid.otp", "otp already verified");
        entity.setVerified(true);
        log.info("verify otp successful");
        return entity;
    }


    @Transactional(rollbackFor = Exception.class)
    public ResponseJEntity resendOtp(String otpRefer) {
        log.info("resend otp previous ref {}", otpRefer);
        Otp oldOtp = Optional.ofNullable(repository.getEntityById(Otp.class, otpRefer))
                .orElseThrow(() -> FatalException.i18n("error.cannot.resend.otp", "invalid otp reference cannot resend otp"));
        String username = new String(oldOtp.getDetail());
        UserAccount userAccount = userRepo.findByUserName(username).orElseThrow(() ->
                new FatalException(I18N.getMessage("user.not.found", username)));
        Otp otp = sendOtp(oldOtp.getDestination(), userAccount.getFullName(), username, oldOtp.getType());
        return ResponseJEntity.ok(I18N.getMessage("successful"))
                .jsonObject("otp_refer", otp.getId())
                .put("validity", otp.getValidity())
                .put("expiry_date", otp.getExpiryDate())
                .build();
    }

    private boolean isDevelop() {
        return "dev".equals(AppContextUtil.getActiveProfile());
    }

    private boolean isMatch(String rawCode, String code) {
        return isDevelop() ? rawCode.equals(code) : pwdEncoder.matches(rawCode, code);
    }
}
