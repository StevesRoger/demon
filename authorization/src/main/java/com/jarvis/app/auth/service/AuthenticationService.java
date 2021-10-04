package com.jarvis.app.auth.service;

import com.jarvis.app.auth.model.entity.UserAccount;
import com.jarvis.app.auth.model.entity.ref.AuthType;
import com.jarvis.app.auth.model.request.PasswordGrant;
import com.jarvis.app.auth.model.response.Facebook;
import com.jarvis.app.auth.model.response.Google;
import com.jarvis.app.auth.model.response.ThirdPartyUserAccount;
import com.jarvis.app.auth.repository.UserRepository;
import com.jarvis.frmk.core.I18N;
import com.jarvis.frmk.core.annotation.LogSlf4j;
import com.jarvis.frmk.core.log.LoggerJ;
import com.jarvis.frmk.core.util.HttpUtil;
import com.jarvis.frmk.hibernate.criterion.JRestriction;
import com.jarvis.frmk.hibernate.entity.ref.Status;
import com.jarvis.frmk.security.exception.ThirdPartyAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created: KimChheng
 * Date: 20-Dec-2020 Sun
 * Time: 11:37 AM
 */
@Service
public class AuthenticationService {

    @Value("${facebook.api.url}")
    private String facebookUrl;

    @Value("${google.api.url}")
    private String googleUrl;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserAccountDetailsService accountDetailsService;

    @LogSlf4j
    private LoggerJ log;

    public UserAccount authenticate(AuthType authType, PasswordGrant request) throws AuthenticationException {
        try {
            switch (authType) {
                case GITHUB:
                    return authenticate(request, this::gitHubAuthenticate);
                case FACEBOOK:
                    return authenticate(request, this::facebookAuthenticate);
                case GOOGLE:
                    return authenticate(request, this::googleAuthenticate);
                case ACCOUNT:
                    return (UserAccount) accountDetailsService.loadUserByUsername(request.getUsername());
                default:
                    throw new BadCredentialsException(I18N.getMessage("error.invalid.authentication"));
            }
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            String message = authType == null ? e.getMessage() : I18N.getMessage("auth." + authType.toString() + ".failed");
            throw new ThirdPartyAuthenticationException(message, e);
        }
    }

    public UserAccount authenticate(String username) {
        return (UserAccount) accountDetailsService.loadUserByUsername(username);
    }

    private UserAccount authenticate(PasswordGrant request, Function<PasswordGrant, ThirdPartyUserAccount> funcAuth) {
        UserAccount userAccount = userRepo.getEntityByConditions(UserAccount.class,
                JRestriction.equal("username", request.getUsername()),
                JRestriction.equal("status", Status.ACTIVE));
        return userAccount == null ? funcAuth.andThen(this::createUser).apply(request) : userAccount;
    }

    private UserAccount createUser(ThirdPartyUserAccount user) {
        UserAccount userAccount = new UserAccount();
       /* userAccount.setUsername(user.getEmail());
        userAccount.setPassword("nope");
        userAccount.setAuthType(user.getAuthType());
        userAccount.setStatus(Status.ACTIVE);
        userAccount.setActivated(true);
        userAccount.setFirstName(user.getFirstName());
        userAccount.setLastName(user.getLastName());
        userAccount.setUserDevice(user.getDevice().toEntity());
        userAccount.setEmail(userAccount.getUsername());
        userAccount.getRoles().add(entityRepository.getEntityByProperty(UserRole.class, "role", ISecurity.ROLE_USER));
        entityRepository.saveOrUpdate(userAccount);*/
        return userAccount;
    }

    public ThirdPartyUserAccount facebookAuthenticate(PasswordGrant request) {
        String url = "/v2.12/me?fields={fields}&redirect={redirect}&access_token={access_token}";
        String fields = "email,first_name,last_name,id,picture.width(720).height(720)";
        Map<String, String> variables = new HashMap<>();
        variables.put("fields", fields);
        variables.put("redirect", "false");
        variables.put("access_token", request.getPassword());
        Facebook facebook = HttpUtil.get(facebookUrl + url, Facebook.class, variables);
        facebook.setDevice(request.getDevice());
        facebook.setPassword(request.getPassword());
        return facebook;
    }

    public ThirdPartyUserAccount googleAuthenticate(PasswordGrant request) {
        String url = "/oauth2/v3/userinfo?access_token={token}";
        Map<String, String> variables = new HashMap<>();
        variables.put("token", request.getPassword());
        Google google = HttpUtil.get(googleUrl + url, Google.class, variables);
        google.setDevice(request.getDevice());
        google.setPassword(request.getPassword());
        return google;
    }

    public ThirdPartyUserAccount gitHubAuthenticate(PasswordGrant request) {
        return null;
    }
}
