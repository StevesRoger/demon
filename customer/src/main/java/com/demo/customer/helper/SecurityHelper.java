package com.demo.customer.helper;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.Map;

public interface SecurityHelper {

    static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    static String getUsername() {
        return getAuthentication().getName();
    }

    @SuppressWarnings("unchecked")
    static Integer getCustomerId() {
        OAuth2Authentication authentication = (OAuth2Authentication) getAuthentication();
        Authentication userAuthentication = authentication.getUserAuthentication();
        Map<String, Object> map = (Map<String, Object>) userAuthentication.getDetails();
        return (Integer) map.get("customer_id");
    }
}
