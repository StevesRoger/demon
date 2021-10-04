package com.jarvis.app.auth.component.security;

import com.jarvis.app.auth.model.entity.UserAccount;
import com.jarvis.app.auth.model.entity.UserPersonalInfo;
import com.jarvis.frmk.security.oauth2.TokenEnhanceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created: KimChheng
 * Date: 30-Nov-2020 Mon
 * Time: 11:09 AM
 */
@Component
public class UserTokenEnhanceDetails implements TokenEnhanceDetails {

    @Override
    public void enhanceDetail(OAuth2AccessToken accessToken, OAuth2Authentication authentication, Object principal, Map<String, Object> additional) {
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
    }
}
