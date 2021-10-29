package com.demo.auth.service;

import com.demo.auth.component.Const;
import com.demo.auth.component.RsaBCryptPasswordEncoder;
import com.demo.auth.domain.entity.Status;
import com.demo.auth.domain.entity.UserAccount;
import com.demo.auth.domain.entity.UserRole;
import com.demo.auth.domain.request.Login;
import com.demo.auth.domain.request.Register;
import com.demo.auth.domain.response.AuthenticationAccessToken;
import com.demo.auth.domain.response.ResponseBody;
import com.demo.auth.repository.UserRepository;
import com.demo.auth.repository.UserRoleRepository;
import com.demo.auth.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@Service
public class UserService {

    @Autowired
    private RsaBCryptPasswordEncoder pwdEncoder;

    @Autowired
    private OAuth2Service auth2Service;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserRoleRepository roleRepo;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> login(Login request, String authorization) throws IOException, GeneralSecurityException {
        LOG.info("username:{}", request.getUsername());
        String[] tokens = SecurityUtil.extractAndDecodeBasicHeader(authorization);
        String secret = pwdEncoder.decryptText(tokens[1]);
        Map<String, String> parameter = request.passwordGrant(tokens[0], secret);
        AuthenticationAccessToken accessToken = (AuthenticationAccessToken) auth2Service.getAccessToken(parameter);
        UserAccount userAccount = (UserAccount) accessToken.getPrincipal();
        userAccount.setLastLogin(new Date());
        return ResponseEntity.ok(new ResponseBody("Successful", accessToken));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> logout(String token) {
        LOG.info("logout user revoke token:{}", token);
        Optional<OAuth2AccessToken> otpAccessToken = auth2Service.revokeToken(token);
        if (otpAccessToken.isPresent()) {
            UserAccount userAccount = (UserAccount) ((AuthenticationAccessToken) otpAccessToken.get()).getPrincipal();
            userAccount.setLastLogout(new Date());
            return ResponseEntity.ok(new ResponseBody("Successful"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseBody.fail("E400", "Token not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> refreshToken(String token, String authorization) throws IOException, GeneralSecurityException {
        LOG.info("refresh token:{}", token);
        String[] tokens = SecurityUtil.extractAndDecodeBasicHeader(authorization);
        String secret = pwdEncoder.decryptText(tokens[1]);
        Map<String, String> parameter = new HashMap<>();
        parameter.put(Const.CLIENT_ID, tokens[0]);
        parameter.put(Const.CLIENT_SECRET, secret);
        parameter.put(Const.GRANT_TYPE, Const.REFRESH_TOKEN);
        parameter.put(Const.REFRESH_TOKEN, token);
        AuthenticationAccessToken accessToken = (AuthenticationAccessToken) auth2Service.refreshToken(parameter);
        UserAccount userAccount = (UserAccount) accessToken.getPrincipal();
        userAccount.setLastLogin(new Date());
        return ResponseEntity.ok(new ResponseBody("Successful", accessToken));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> registerUserAccount(Register request) throws GeneralSecurityException, UnsupportedEncodingException {
        Map<String, Object> data = new HashMap<>();
        Optional<UserAccount> optUserAccount = userRepo.findByUsernameAndStatus(request.getUsername(), Status.ACTIVE);
        if (optUserAccount.isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseBody.fail("E400", "User name " + request.getUsername() + " already exist"));
        UserRole role = roleRepo.findByRole(request.getRole()).orElseThrow(() -> new RuntimeException("Role " + request.getRole() + " not found"));
        String rawPwd = pwdEncoder.decryptText(request.getPassword());
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(request.getUsername());
        userAccount.setPassword(pwdEncoder.encode(rawPwd));
        userAccount.setCustomerId(request.getCustomerId());
        userAccount.setCreatedBy(SecurityUtil.getAuthenticateUsername());
        userAccount.getRoles().add(role);
        userRepo.save(userAccount);
        data.put("user_id", userAccount.getId());
        data.put("role", role.getRole());
        return ResponseEntity.ok(new ResponseBody("Successful", data));
    }
}
