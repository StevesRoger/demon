package com.jarvis.app.auth.component.security;

import com.demon.auth.domain.response.AuthenticationAccessToken;
import com.jarvis.frmk.security.oauth2.token.JarvisTokenEnhancer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * Created: KimChheng
 * Date: 30-Nov-2020 Mon
 * Time: 11:09 AM
 */
public class UserTokenEnhanceDetails extends JarvisTokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken token, OAuth2Authentication authentication) {
        OAuth2AccessToken accessToken = super.enhance(token, authentication);
        return new AuthenticationAccessToken(accessToken, authentication);
    }

    /*public void enhanceDetail(OAuth2AccessToken accessToken, OAuth2Authentication authentication, Object principal, Map<String, Object> additional) {
        if (principal instanceof UserAccount) {
            UserAccount userAccount = (UserAccount) principal;
            UserPersonalInfo personalInfo = userAccount.getPersonalInfo();
            additional.put("first_name", personalInfo.getFirstName());
            additional.put("last_name", personalInfo.getLastName());
            additional.put("primary_phone", personalInfo.getPrimaryPhone());
            additional.put("dob", personalInfo.getDob());
            additional.put("email", personalInfo.getEmail());
            additional.put("auth_type", userAccount.getAuthType());
            additional.put("created_date", userAccount.getCreatedDate());
        }
    }*/
}
