package com.demon.auth.service;

import com.jarvis.app.auth.component.Consts;
import com.demon.auth.helper.ValidationHelper;
import com.demon.auth.domain.entity.*;
import com.demon.auth.domain.entity.ref.OtpType;
import com.demon.auth.domain.request.*;
import com.demon.auth.domain.response.AuthenticationAccessToken;
import com.demon.auth.repository.UserRepository;
import com.demon.auth.service.security.UserAccountDetailsService;
import com.jarvis.frmk.core.I18N;
import com.jarvis.frmk.core.annotation.LogSlf4j;
import com.jarvis.frmk.core.exception.FatalException;
import com.jarvis.frmk.core.log.LoggerJ;
import com.jarvis.frmk.core.model.http.response.ResponseJEntity;
import com.jarvis.frmk.core.service.ConfigService;
import com.jarvis.frmk.core.util.DateUtil;
import com.jarvis.frmk.core.util.StringUtil;
import com.jarvis.frmk.hibernate.entity.ref.AuthType;
import com.jarvis.frmk.security.RSASecurityProvider;
import com.jarvis.frmk.security.exception.UserAccountNotActivateException;
import com.jarvis.frmk.security.oauth2.IOAuth2;
import com.jarvis.frmk.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    private OtpService otpService;

    @Autowired
    private RSASecurityProvider rsaSecurity;

    @Autowired
    private PasswordEncoder pwdEncoder;

    @Autowired
    private OAuth2Service auth2Service;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserAccountDetailsService userDetailService;

    @Autowired
    private ConfigService sysConfig;

    @LogSlf4j
    private LoggerJ log;

    @Transactional(rollbackFor = Exception.class)
    public ResponseJEntity register(UserRegister request) throws Exception {
        log.info("register user");
        log.infoNonNull("email:{}", request.getEmail());
        log.infoNonNull("user name:{}", request.getUserName());
        log.infoNonNull("first name:{}", request.getFirstName());
        log.infoNonNull("last name:{}", request.getLastName());
        log.infoNonNull("primary phone:{}", request.getPrimaryPhone());
        ValidationHelper.isEmailOrUsernameExist(request);
        String rawPwd = rsaSecurity.decryptText(request.getPassword().trim());
        String pwdPolicyId = StringUtil.isNotEmpty(request.getPwdPolicyId()) ? request.getPwdPolicyId() :
                sysConfig.getValue(Consts.SYS_DEFAULT_USER_PWD_POLICY);
        PasswordPolicy pwdPolicy = repository.findPwdPolicyById(pwdPolicyId)
                .orElseThrow(() -> FatalException.i18n("pwd.policy.not.found", "error cannot find pwd policy"));
        UserRole userRole = repository.findUserRoleByIdOrRole(request.getRoleId(), sysConfig.getValue(Consts.SYS_DEFAULT_USER_ROLE))
                .orElseThrow(() -> FatalException.i18n("user.profile.not.found", "error cannot find user role"));
        UserAccount userAccount = new UserAccount();
        request.createUserPersonalInfo(userAccount);
        userAccount.setAuthType(AuthType.ACCOUNT);
        userAccount.setUsername(request.getUserName());
        userAccount.setPassword(pwdEncoder.encode(rawPwd));
        userAccount.setPasswordPolicy(pwdPolicy.isValid(rawPwd));
        userAccount.addDevice(request.createUserDevice(userAccount));
        userAccount.addRole(userRole);
        repository.save(userAccount);
        Otp otp = otpService.sendOtp(request.getEmail(), request.getFullName(), userAccount.getUsername(), OtpType.ACTIVATE);
        log.info("register user successful");
        log.info("user account id:{}", userAccount.getId());
        return ResponseJEntity.ok(I18N.getMessage("register.successful"))
                .code(Consts.REQUIRE_ACTIVATE).jsonObject("otp_refer", otp.getId())
                .put("validity", otp.getValidity())
                .put("expiry_date", otp.getExpiryDate())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseJEntity activate(String otpRefer, String otpCode) throws GeneralSecurityException, UnsupportedEncodingException {
        log.info("activation user");
        Otp otp = otpService.verify(otpRefer, otpCode, OtpType.ACTIVATE);
        String username = new String(otp.getDetail());
        UserAccount userAccount = repository.findByUserName(username).orElseThrow(() ->
                new FatalException(I18N.getMessage("user.not.found", username)));
        userAccount.setActivated(true);
        userAccount.setEnabled(true);
        return ResponseJEntity.ok(I18N.getMessage("successful"));
    }

    public ResponseJEntity forgetPassword(ForgetPassword request) {
        try {
            log.info("forget password:{}", request.getUsername());
            UserAccount userAccount = (UserAccount) userDetailService.loadUserByUsername(request.getUsername());
            Otp otp = otpService.sendOtp(userAccount.getEmail(), userAccount.getFullName(), request.getUsername(), OtpType.FORGET_PASSWORD);
            log.info("register user successful");
            log.info("user account id:{}", userAccount.getId());
            return ResponseJEntity.ok(I18N.getMessage("register.successful"))
                    .jsonObject("otp_refer", otp.getId())
                    .put("validity", otp.getValidity())
                    .put("expiry_date", otp.getExpiryDate())
                    .build();
        } catch (UserAccountNotActivateException ex) {
            return handleNotActivate((UserAccount) ex.getDetail());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseJEntity resetPassword(RestPassword request) throws Exception {
        log.info("rest password");
        Otp otp = otpService.verify(request.getOtpRefer(), request.getOtpCode(), OtpType.FORGET_PASSWORD);
        UserAccount userAccount = (UserAccount) userDetailService.loadUserByUsername(new String(otp.getDetail()));
        userAccount.setPassword(pwdEncoder.encode(rsaSecurity.decryptText(request.getNewPassword())));
        return ResponseJEntity.ok(I18N.getMessage("successful"));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseJEntity changePassword(ChangePassword request) throws Exception {
        log.info("change password");
        UserAccount userAccount = (UserAccount) userDetailService.loadUserByUsername(request.getUsername());
        if (!pwdEncoder.matches(request.getCurrentPassword(), userAccount.getPassword()))
            throw FatalException.i18n("current.password.mismatch", "Change password fail old password mismatch");
        if (pwdEncoder.matches(request.getNewPassword(), userAccount.getPassword()))
            throw FatalException.i18n("error.new.password", "New password cannot be the same as current password");
        userAccount.setPassword(pwdEncoder.encode(rsaSecurity.decryptText(request.getNewPassword())));
        return ResponseJEntity.ok(I18N.getMessage("successful"));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseJEntity login(Login request, String authorization) throws IOException, GeneralSecurityException {
        try {
            log.info("login user:{}", request.getUsername());
            log.info("token:{}", request.getDevice().getToken());
            log.info("platform:{}", request.getDevice().getPlatform());
            String[] tokens = SecurityUtil.extractAndDecodeBasicHeader(authorization);
            String secret = rsaSecurity.decryptText(tokens[1]);
            AuthenticationAccessToken accessToken = (AuthenticationAccessToken) auth2Service.getAccessToken(request.passwordGrant(tokens[0], secret));
            UserAccount userAccount = (UserAccount) accessToken.getPrincipal();
            UserDevice device = userAccount.getDevice(request.getDevice().getId()).orElse(request.getDevice());
            device.setId(request.getDevice().getId());
            device.setPlatform(request.getDevice().getPlatform());
            device.setToken(request.getDevice().getToken());
            device.setModel(request.getDevice().getModel());
            device.setAppVersion(request.getDevice().getAppVersion());
            userAccount.addDevice(device);
            userAccount.setLastLogin(new Date());
            return ResponseJEntity.ok(I18N.getMessage("successful"), accessToken);
        } catch (UserAccountNotActivateException ex) {
            return handleNotActivate((UserAccount) ex.getDetail());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseJEntity logout(String token) {
        log.info("logout user revoke token:{}", token);
        Optional<OAuth2AccessToken> otpAccessToken = auth2Service.revokeToken(token);
        otpAccessToken.ifPresent((auth) -> {
            UserAccount userAccount = (UserAccount) ((AuthenticationAccessToken) auth).getPrincipal();
            userAccount.setLastLogout(new Date());
        });
        return otpAccessToken.isPresent() ? ResponseJEntity.ok(I18N.getMessage("successful")) :
                ResponseJEntity.fail(I18N.getMessage("error.token.not.found"));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseJEntity refreshToken(String token, String authorization) throws IOException, GeneralSecurityException {
        log.info("refresh token:{}", token);
        String[] tokens = SecurityUtil.extractAndDecodeBasicHeader(authorization);
        String secret = rsaSecurity.decryptText(tokens[1]);
        Map<String, String> parameter = new HashMap<>();
        parameter.put(IOAuth2.CLIENT_ID, tokens[0]);
        parameter.put(IOAuth2.CLIENT_SECRET, secret);
        parameter.put(IOAuth2.GRANT_TYPE, IOAuth2.REFRESH_TOKEN);
        parameter.put(IOAuth2.REFRESH_TOKEN, token);
        AuthenticationAccessToken accessToken = (AuthenticationAccessToken) auth2Service.refreshToken(parameter);
        UserAccount userAccount = (UserAccount) accessToken.getPrincipal();
        userAccount.setLastLogin(new Date());
        return ResponseJEntity.ok(I18N.getMessage("successful"), accessToken);
    }

    /*public ResponseJEntity getUserById(Integer userId) {
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
    }*/

    private ResponseJEntity handleNotActivate(UserAccount userAccount) {
        log.info("user not activation yet");
        Otp otp = otpService.sendOtp(userAccount.getEmail(), userAccount.getFullName(), userAccount.getId().toString(), OtpType.ACTIVATE);
        return ResponseJEntity.ok(I18N.getMessage("user.not.activate"))
                .code(Consts.REQUIRE_ACTIVATE)
                .jsonObject("otp_refer", otp.getId())
                .put("expiry_time", otp.getValidity())
                .put("expiry_date", DateUtil.format(otp.getExpiryDate()))
                .build();
    }
}
