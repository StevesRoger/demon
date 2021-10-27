package com.demon.auth.service.security;

import com.jarvis.app.auth.component.Consts;
import com.demon.auth.domain.entity.UserAccount;
import com.demon.auth.domain.entity.UserPersonalInfo;
import com.demon.auth.domain.entity.UserRole;
import com.demon.auth.domain.request.Login;
import com.demon.auth.domain.response.Facebook;
import com.demon.auth.domain.response.Google;
import com.demon.auth.domain.response.SocialMediaUserDetails;
import com.demon.auth.repository.UserRepository;
import com.jarvis.frmk.core.I18N;
import com.jarvis.frmk.core.exception.FatalException;
import com.jarvis.frmk.core.service.ConfigService;
import com.jarvis.frmk.core.util.AppContextUtil;
import com.jarvis.frmk.core.util.HttpUtil;
import com.jarvis.frmk.hibernate.entity.ref.AuthType;
import com.jarvis.frmk.hibernate.entity.ref.Status;
import com.jarvis.frmk.security.exception.UserAccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created: chheng
 * Date: 16-Oct-2021 Sat
 * Time: 11:10
 */
@Service
public class UserSocialMediaService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    @Autowired
    private UserRepository repository;

    @Autowired
    private ConfigService sysConfig;

    @Autowired
    private UserAccountDetailsService userDetailService;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        try {
            UserAccount userAccount = new UserAccount();
            if (token.getPrincipal() instanceof UsernamePasswordAuthenticationToken)
                userAccount = (UserAccount) ((UsernamePasswordAuthenticationToken) token.getPrincipal()).getPrincipal();
            else if (token.getPrincipal() instanceof PreAuthenticatedAuthenticationToken)
                userAccount = (UserAccount) ((PreAuthenticatedAuthenticationToken) token.getPrincipal()).getPrincipal();
            Login request = new Login();
            request.setType(userAccount.getAuthType());
            request.setUsername(userAccount.getUsername());
            request.setPassword(userAccount.getPassword());
            return loadUserSocialMedia(request);
        } catch (UsernameNotFoundException e) {
            return userDetailService.loadUserByUsername(token.getName());
        }
    }

    public UserAccount loadUserSocialMedia(Login request) throws UsernameNotFoundException {
        Optional<UserAccount> optUserAccount = repository.findByUserNameAndType(request.getUsername(), request.getType());
        if (AuthType.GOOGLE.equals(request.getType()))
            return optUserAccount.orElseGet(() -> createUser(googleAuthenticate(request)));
        else if (AuthType.FACEBOOK.equals(request.getType()))
            return optUserAccount.orElseGet(() -> createUser(facebookAuthenticate(request)));
        else if (AuthType.GITHUB.equals(request.getType()))
            return optUserAccount.orElseGet(() -> createUser(gitHubAuthenticate(request)));
        throw new UserAccountNotFoundException(I18N.getMessage("user.not.found", request.getUsername()), "User not found");
    }

    public SocialMediaUserDetails facebookAuthenticate(Login request) {
        String url = AppContextUtil.getProperty("facebook.api.url") + "/v2.12/me?fields={fields}&redirect={redirect}&access_token={access_token}";
        String fields = "email,first_name,last_name,id,picture.width(720).height(720)";
        Map<String, String> variables = new HashMap<>();
        variables.put("fields", fields);
        variables.put("redirect", "false");
        variables.put("access_token", request.getPassword());
        Facebook facebook = HttpUtil.get(url, Facebook.class, variables);
        facebook.setDevice(request.getDevice());
        facebook.setPassword(request.getPassword());
        return facebook;
    }

    public SocialMediaUserDetails googleAuthenticate(Login request) {
        String url = AppContextUtil.getProperty("google.api.url") + "/oauth2/v3/userinfo?access_token={token}";
        Map<String, String> variables = new HashMap<>();
        variables.put("token", request.getPassword());
        Google google = HttpUtil.get(url, Google.class, variables);
        google.setDevice(request.getDevice());
        google.setPassword(request.getPassword());
        return google;
    }

    public SocialMediaUserDetails gitHubAuthenticate(Login request) {
        return null;
    }

    private UserAccount createUser(SocialMediaUserDetails user) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(user.getEmail());
        userAccount.setPassword(user.getPassword());
        userAccount.setAuthType(user.getType());
        userAccount.setStatus(Status.ACTIVE);
        userAccount.setActivated(true);
        userAccount.setEnabled(true);
        userAccount.addDevice(user.getDevice());
        UserPersonalInfo info = new UserPersonalInfo(userAccount);
        info.setEmail(user.getEmail());
        info.setFullName(user.getFullName());
        info.setFirstName(user.getFirstName());
        info.setLastName(user.getLastName());
        UserRole userRole = repository.findUserRoleByIdOrRole(null, sysConfig.getValue(Consts.SYS_DEFAULT_USER_ROLE))
                .orElseThrow(() -> FatalException.i18n("user.profile.not.found", "error cannot find user role"));
        userAccount.getRoles().add(userRole);
        repository.save(userAccount);
        return userAccount;
    }

}
