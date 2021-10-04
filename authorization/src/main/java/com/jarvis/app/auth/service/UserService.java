package com.jarvis.app.auth.service;

import com.jarvis.app.auth.Consts;
import com.jarvis.app.auth.helper.ValidationHelper;
import com.jarvis.app.auth.model.entity.*;
import com.jarvis.app.auth.model.entity.ref.OtpType;
import com.jarvis.app.auth.model.request.*;
import com.jarvis.app.auth.repository.UserRepository;
import com.jarvis.frmk.core.I18N;
import com.jarvis.frmk.core.annotation.LogSlf4j;
import com.jarvis.frmk.core.exception.FatalException;
import com.jarvis.frmk.core.log.LoggerJ;
import com.jarvis.frmk.core.model.http.response.ResponseJEntity;
import com.jarvis.frmk.core.service.ConfigService;
import com.jarvis.frmk.core.util.ObjectUtil;
import com.jarvis.frmk.hibernate.criterion.Conditions;
import com.jarvis.frmk.hibernate.entity.ref.AuthType;
import com.jarvis.frmk.hibernate.entity.ref.Status;
import com.jarvis.frmk.security.RSASecurityProvider;
import com.jarvis.frmk.security.exception.DeviceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created: kim chheng
 * Date: 29-Sep-2019 Sun
 * Time: 1:33 PM
 */

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private OTPService otpService;

    @Autowired
    private RSASecurityProvider rsaSecurity;

    @Autowired
    private PasswordEncoder pwdEncoder;

    @Autowired
    private OAuth2Service auth2Service;

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private ConfigService sysConfig;

    @LogSlf4j
    private LoggerJ log;

    @Transactional(rollbackFor = Exception.class)
    public ResponseJEntity registerUser(RegisterUser registerUser) throws Exception {
        log.info("register user");
        log.infoNonNull("email:{}", registerUser.getEmail());
        log.infoNonNull("user name:{}", registerUser.getUserName());
        log.infoNonNull("first name:{}", registerUser.getFirstName());
        log.infoNonNull("last name:{}", registerUser.getLastName());
        log.infoNonNull("primary phone:{}", registerUser.getPrimaryPhone());
        ValidationHelper.isEmailOrUsernameExist(registerUser);
        String rawPwd = rsaSecurity.decryptText(registerUser.getPassword().trim());
        PasswordPolicy pwdPolicy = repository.findPwdPolicyById(registerUser.getPwdPolicyId() != null ?
                registerUser.getPwdPolicyId() :
                sysConfig.getValue(Consts.SYS_DEFAULT_USER_PWD_POLICY)).isValid(rawPwd);
        UserRole userRole = registerUser.getRoleId() != null ?
                repository.getEntityById(UserRole.class, registerUser.getRoleId()) :
                repository.getEntityByProperty(UserRole.class, "role", sysConfig.getValue(Consts.SYS_DEFAULT_USER_ROLE));
        UserAccount userAccount = new UserAccount();
        userAccount.setAuthType(AuthType.ACCOUNT);
        userAccount.setUsername(registerUser.getUserName());
        userAccount.setPassword(pwdEncoder.encode(rawPwd));
        userAccount.setPersonalInfo(registerUser.personalInfo(userAccount));
        userAccount.setUserDevice(registerUser.userDevice(userAccount));
        userAccount.setPasswordPolicy(pwdPolicy);
        userAccount.getRoles().add(userRole);
        repository.save(userAccount);
        Otp otp = otpService.sendOTP(userAccount, OtpType.ACTIVATE);
        log.info("register user successful");
        log.info("user account id:{}", userAccount.getId());
        return ResponseJEntity.ok(I18N.getMessage("register.successful"))
                .jsonObject("otp_refer", otp.getId())
                .put("validity", otp.getValidity())
                .put("expiry_date", otp.getExpiryDate())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseJEntity sendOTP(String email, OtpType type) {
        UserAccount userAccount = ValidationHelper.validateUserAccount(email);
        log.info("user account id:{}", userAccount.getId());
        Otp otp = otpService.sendOTP(userAccount, type);
        return ResponseJEntity.ok(I18N.getMessage("successful"))
                .jsonObject("otp_refer", otp.getId())
                .put("validity", otp.getValidity())
                .put("expiry_date", otp.getExpiryDate())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseJEntity verifyOTP(String otpRefer, String otpCode) throws GeneralSecurityException, UnsupportedEncodingException {
        Otp otp = otpService.verify(otpRefer, otpCode);
        if (OtpType.ACTIVATE.equals(otp.getType())) {
            log.info("verify activation otp");
            Integer userId = Integer.parseInt(new String(otp.getDetail()));
            UserAccount userAccount = repository.getEntityById(UserAccount.class, userId);
            userAccount.setActivated(true);
            userAccount.setEnabled(true);
        } else if (OtpType.FORGET_PASSWORD.equals(otp.getType())) {

        }
        return ResponseJEntity.ok(I18N.getMessage("successful"));
    }

    /*@Transactional(rollbackFor = Exception.class)
    public ResponseJEntity resetPassword(RestPasswordRequest request) throws Exception {
        log.info("rest password");
        UserAccountOTP userAccountOTP = repository.getEntityByProperty(UserAccountOTP.class, "otpRefer", request.getOtpRefer());
        Otp otp = repository.getEntityByProperty(Otp.class, "otpRefer", request.getOtpRefer());
        if (userAccountOTP == null || otp == null)
            throw new FatalException(I18N.getMessage("reset.password.fail"), "Reset password fail invalid otp reference");
        if (!otp.getVerified())
            throw new FatalException(I18N.getMessage("reset.password.fail"), "Reset password fail otp reference not yet verify");
        if (!OTPType.RESET_PASSWORD.equals(otp.getType()))
            throw new FatalException(I18N.getMessage("reset.password.fail"), "Reset password fail otp reference type not rest password");
        UserAccount userAccount = userAccountOTP.getUserAccount();
        if (userAccount == null || Status.INACTIVE.equals(userAccount.getStatus()))
            throw new FatalException(I18N.getMessage("user.email.not.found"), "user account not found");
        if (Status.SUSPENDED.equals(userAccount.getStatus()))
            throw new FatalException(I18N.getMessage("user.account.suspended", "email", userAccount.getUsername()), "user account suspended");
        userAccount.setPassword(pwdEncoder.encode(rsaSecurity.decryptText(request.getNewPassword())));
        userAccountOTP.invalidateOtpRefer();
        repository.saveOrUpdate(userAccountOTP);
        return ResponseJEntity.ok(I18N.getMessage("successful"));
    }*/

    @Transactional(rollbackFor = Exception.class)
    public ResponseJEntity changePassword(ChangePasswordRequest request) throws Exception {
        log.info("change password");
        UserAccount userAccount = ValidationHelper.validateUserAccount(request.getEmail());
        if (!userAccount.getActivated() || !Status.ACTIVE.equals(userAccount.getStatus()))
            throw new FatalException(I18N.getMessage("user.not.activate"), "Change password fail user status " + userAccount.getStatus());
        if (!pwdEncoder.matches(request.getCurrentPassword(), userAccount.getPassword()))
            throw new FatalException(I18N.getMessage("current.password.mismatch"), "Change password fail old password mismatch");
        if (pwdEncoder.matches(request.getNewPassword(), userAccount.getPassword()))
            throw new FatalException(I18N.getMessage("error.new.password"), "New password cannot be the same as current password");
        String newPassword = rsaSecurity.decryptText(request.getNewPassword());
        userAccount.setPassword(pwdEncoder.encode(newPassword));
        repository.saveOrUpdate(userAccount);
        return ResponseJEntity.ok(I18N.getMessage("successful"));
    }

    public ResponseJEntity login(LoginRequest request) {
        log.info("login user name:{}", request.getUsername());
        PasswordGrant passwordGrant = new PasswordGrant(request);
        UserAccount userAccount = authService.authenticate(request.getType(), passwordGrant);
        UserDevice userDevice = userAccount.getUserDevice();
        if (userDevice == null)
            throw new DeviceNotFoundException(I18N.getMessage("user.device.not.found"), "User device not found");
        log.info("user id:{}", userAccount.getId());
        log.info("fcm token:{}", request.getDevice().getToken());
        log.info("platform:{}", request.getDevice().getPlatform());
        userDevice.setToken(request.getDevice().getToken());
        userDevice.setPlatform(request.getDevice().getPlatform());
        userDevice.setAppVersion(request.getDevice().getAppVersion());
        userDevice.setId(request.getDevice().getId());
        userDevice.setModel(request.getDevice().getModel());
        userAccount.setLastLogin(new Date());
        repository.saveOrUpdate(userAccount);
        OAuth2AccessToken accessToken = auth2Service.getAccessToken(passwordGrant);
        return ResponseJEntity.ok(I18N.getMessage("successful"), accessToken);
    }

    @SuppressWarnings("unchecked")
    public ResponseJEntity refreshToken(RefreshTokenRequest request) {
        log.info("refresh token:{}", request.getRefreshToken());
        return ResponseJEntity.ok(I18N.getMessage("successful"), auth2Service.refreshToken(request));
    }

    public ResponseJEntity logout(LogoutRequest request) {
        log.info("logout user {}", request.getUsername());
        UserAccount userAccount = authService.authenticate(request.getUsername());
        List<OAuth2AccessToken> tokens = auth2Service.revokeUserToken(request.getClientId(), request.getUsername());
        log.info(tokens.size() + " Tokens have been revoked");
        userAccount.setLastLogout(new Date());
        repository.saveOrUpdate(userAccount);
        ResponseJEntity response = ResponseJEntity.ok(I18N.getMessage("successful"));
        return tokens.isEmpty() ? response : response.jsonObject("token_revoked", tokens.size()).build();
    }

    public ResponseJEntity getUserById(Integer userId) {
        if (userId != null) {
            log.info("get user by id:{}", userId);
            UserAccount userAccount = repository.getEntityByConditions(UserAccount.class,
                    Conditions.equal("id", userId),
                    Conditions.equal("status", Status.ACTIVE),
                    Conditions.equal("activated", true));
            if (userAccount == null)
                return ResponseJEntity.fail(I18N.getMessage("user.not.found"));
            return ResponseJEntity.ok(I18N.getMessage("successful")).data(convertUserEntity(userAccount));
        } else {
            log.info("list all user");
            List<UserAccount> userAccountAccounts = repository.listEntityByConditions(UserAccount.class,
                    Conditions.equal("status", Status.ACTIVE),
                    Conditions.equal("activated", true));
            List<Map<String, Object>> users = new ArrayList<>();
            for (UserAccount userAccount : userAccountAccounts)
                users.add(convertUserEntity(userAccount));
            return ResponseJEntity.ok(I18N.getMessage("successful")).data(users);
        }
    }

    private Map<String, Object> convertUserEntity(UserAccount userAccount) {
        UserDevice userDevice = userAccount.getUserDevice();
        Map<String, Object> userMap = ObjectUtil.chooseField(userAccount, "id", "username", "primaryPhone", "email", "firstName", "lastName", "roles.role");
        Map<String, Object> deviceMap = ObjectUtil.chooseField(userDevice, "id", "deviceId", "fcmToken", "platform", "model", "appVersion");
        userMap.put("device", deviceMap);
        return userMap;
    }

}
