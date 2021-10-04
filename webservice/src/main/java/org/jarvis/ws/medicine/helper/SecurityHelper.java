package org.jarvis.ws.medicine.helper;

import org.jarvis.security.util.SecurityUtil;
import org.jarvis.ws.medicine.model.entity.UserEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

/**
 * Created: KimChheng
 * Date: 18-Jun-2020 Thu
 * Time: 11:57
 */
public interface SecurityHelper {

    static int getUserId() {
        return getUser().getId();
    }

    static String getUsername() {
        UserEntity userEntity = getUser();
        return userEntity.getFirstName() +
                " " +
                userEntity.getLastName();
    }

    static String getUserEmail() {
        return getUser().getEmail();
    }

    static String getUserType() {
        return getUser().getAuthType();
    }

    static String getFCMToken() {
        return getUser().getDevice().getFcmToken();
    }

    static UserEntity getUser() {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityUtil.getAuthentication();
        OAuth2AuthenticationDetails authenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        return (UserEntity) authenticationDetails.getDecodedDetails();
    }

}
