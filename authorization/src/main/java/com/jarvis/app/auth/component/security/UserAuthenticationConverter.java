package com.jarvis.app.auth.component.security;

import com.jarvis.app.auth.model.entity.UserAccount;
import com.jarvis.app.auth.model.entity.UserPersonalInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Map;

/**
 * Created: KimChheng
 * Date: 15-Jun-2020 Mon
 * Time: 22:31
 */
public class UserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = (Map<String, Object>) super.convertUserAuthentication(authentication);
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        UserPersonalInfo personalInfo = userAccount.getPersonalInfo();
        response.put("first_name", personalInfo.getFirstName());
        response.put("last_name", personalInfo.getLastName());
        response.put("primary_phone", personalInfo.getPrimaryPhone());
        response.put("dob", personalInfo.getDob());
        //response.put("device", deviceInfo(userAccount));
        return response;
    }

    /*private Map<String, Object> deviceInfo(UserAccount userAccount) {
        UserDevice userDevice = userAccount.getUserDevice();
        Map<String, Object> map = new HashMap<>();
       if (userDevice != null) {
            map.put("id", userDevice.getId());
            map.put("device_id", userDevice.getId());
            map.put("fcm_token", userDevice.getToken());
            map.put("platform", userDevice.getPlatform());
            map.put("app_version", userDevice.getAppVersion());
            map.put("model", userDevice.getModel());
        }
        return map;
    }*/
}