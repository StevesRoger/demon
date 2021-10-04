package com.jarvis.app.auth.component.security;

import com.jarvis.app.auth.model.entity.UserAccount;
import com.jarvis.app.auth.model.request.PasswordGrant;
import com.jarvis.app.auth.service.AuthenticationService;
import com.jarvis.frmk.core.I18N;
import com.jarvis.frmk.security.ISecurity;
import com.jarvis.frmk.security.JarvisAuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created: KimChheng
 * Date: 21-Jun-2021 Mon
 * Time: 10:29 PM
 */
public class UserAuthenticationProvider extends JarvisAuthenticationProvider {

    private AuthenticationService authService;

    public UserAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        super(userDetailsService, passwordEncoder);
    }

    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authService = authenticationService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken credential) throws AuthenticationException {
        if (userDetails instanceof UserAccount) {
            try {
                switch (((UserAccount) userDetails).getAuthType().getId()) {
                    case "GOOGLE":
                        authService.googleAuthenticate(createUser(credential));
                        break;
                    case "FACEBOOK":
                        authService.facebookAuthenticate(createUser(credential));
                        break;
                    case "GITHUB":
                        break;
                    case "ACCOUNT":
                        super.additionalAuthenticationChecks(userDetails, credential);
                        break;
                    default:
                        throw new InternalAuthenticationServiceException(I18N.getMessage("error.invalid.authentication"));
                }
                return;
            } catch (InternalAuthenticationServiceException e) {
                throw e;
            } catch (Exception e) {
                throw new InternalAuthenticationServiceException(ISecurity.I18N_MESSAGE.getMessage("security.bad.credential", "Bad credentials"), e);
            }
        }
        super.additionalAuthenticationChecks(userDetails, credential);
    }

    private PasswordGrant createUser(UsernamePasswordAuthenticationToken credential) {
        PasswordGrant passwordGrant = new PasswordGrant();
        passwordGrant.setPassword(credential.getCredentials().toString());
        return passwordGrant;
    }
}
