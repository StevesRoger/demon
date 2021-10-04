package org.jarvis.ws.medicine.component;

import org.jarvis.core.util.JsonUtil;
import org.jarvis.security.oauth2.service.RemoteTokenServiceInterceptor;
import org.jarvis.ws.medicine.model.entity.UserEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created: chheng
 * Date: 27-Jun-2020 Sat
 * Time: 21:55
 */
@Component
public class AuthenticationRemoteTokenInterceptor implements RemoteTokenServiceInterceptor {

    @Override
    public void postLoadAuthentication(OAuth2Authentication authentication) throws AuthenticationException, InvalidTokenException {
        Map<String, Object> detail = (Map<String, Object>) authentication.getDetails();
        UserEntity userEntity = JsonUtil.mapTo(detail, UserEntity.class);
        authentication.setDetails(userEntity);
    }
}
