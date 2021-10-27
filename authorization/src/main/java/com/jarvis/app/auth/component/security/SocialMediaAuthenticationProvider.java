package com.jarvis.app.auth.component.security;

import com.demon.auth.domain.entity.UserAccount;
import com.demon.auth.domain.request.Login;
import com.demon.auth.domain.response.SocialMediaUserDetails;
import com.demon.auth.service.security.UserSocialMediaService;
import com.jarvis.frmk.core.I18N;
import com.jarvis.frmk.core.exception.HttpClientRequestException;
import com.jarvis.frmk.core.util.ObjectUtil;
import com.jarvis.frmk.hibernate.entity.ref.AuthType;
import com.jarvis.frmk.security.ISecurity;
import com.jarvis.frmk.security.exception.UserAccountNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Base64;
import java.util.Map;
import java.util.function.Function;

/**
 * Created: chheng
 * Date: 13-Oct-2021 Wed
 * Time: 22:49
 */
public class SocialMediaAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private UserSocialMediaService socialMediaService;

    public void setSocialMediaService(UserSocialMediaService socialMediaService) {
        this.socialMediaService = socialMediaService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        try {
            UserAccount userAccount = (UserAccount) userDetails;
            if (AuthType.GOOGLE.equals(userAccount.getAuthType()))
                socialMediaAuthenticationChecks(userAccount, authentication, socialMediaService::googleAuthenticate);
            else if (AuthType.FACEBOOK.equals(userAccount.getAuthType()))
                socialMediaAuthenticationChecks(userAccount, authentication, socialMediaService::facebookAuthenticate);
            else if (AuthType.GITHUB.equals(userAccount.getAuthType()))
                socialMediaAuthenticationChecks(userAccount, authentication, socialMediaService::gitHubAuthenticate);
            else throw new InternalAuthenticationServiceException(I18N.getMessage("error.invalid.authentication"));
        } catch (BadCredentialsException | HttpStatusCodeException | HttpClientRequestException ex) {
            throw new InternalAuthenticationServiceException(ISecurity.I18N_MESSAGE.getMessage("security.bad.credential", "Bad credentials"), ex);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        try {
            if (authentication.getDetails() instanceof Map) {
                Map<String, String> parameter = (Map<String, String>) authentication.getDetails();
                Login request = new Login();
                request.setType(new AuthType(parameter.get("type")));
                request.setUsername(username);
                request.setPassword(authentication.getCredentials().toString());
                request.setDevice((UserDevice) ObjectUtil.toObject(Base64.getDecoder().decode(parameter.get("device"))));
                return socialMediaService.loadUserSocialMedia(request);
            }
            throw new UserAccountNotFoundException(I18N.getMessage("user.not.found", username), "User not found");
        } catch (HttpStatusCodeException | HttpClientRequestException ex) {
            throw new UsernameNotFoundException(ex.getMessage(), ex);
        }
    }

    private void socialMediaAuthenticationChecks(UserAccount userAccount, UsernamePasswordAuthenticationToken authentication, Function<Login, SocialMediaUserDetails> func) {
        Login request = new Login();
        try {
            request.setPassword(userAccount.getPassword());
            SocialMediaUserDetails userDetails = func.apply(request);
            userAccount.setFirstName(userDetails.getFirstName());
            userAccount.setLastName(userDetails.getLastName());
            userAccount.setEmail(userDetails.getEmail());
        } catch (HttpStatusCodeException | HttpClientRequestException ex) {
            if (logger.isDebugEnabled())
                logger.debug("Social media authentication first attempt failed");
            request.setPassword(authentication.getCredentials().toString());
            SocialMediaUserDetails userDetails = func.apply(request);
            userAccount.setFirstName(userDetails.getFirstName());
            userAccount.setLastName(userDetails.getLastName());
            userAccount.setEmail(userDetails.getEmail());
            userAccount.setPassword(userDetails.getPassword());
        }
    }
}
