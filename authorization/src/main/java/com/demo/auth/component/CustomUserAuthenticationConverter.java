package com.demo.auth.component;


import com.demo.auth.domain.entity.UserAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Map;

public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> map = (Map<String, Object>) super.convertUserAuthentication(authentication);
        UserAccount principal = (UserAccount) authentication.getPrincipal();
        map.put("customer_id", principal.getCustomerId());
        return map;
    }
}
