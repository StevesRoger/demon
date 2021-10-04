package com.jarvis.app.auth.component;

import com.jarvis.frmk.core.component.JarvisAuditorProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * Created: KimChheng
 * Date: 03-Oct-2021 Sun
 * Time: 11:14 PM
 */
@Component
public class AuditorProviderImpl implements JarvisAuditorProvider {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(auth) && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            return Optional.ofNullable(((User) principal).getUsername());
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getCurrentAuthority() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(auth) && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            Collection<GrantedAuthority> authorities = ((User) principal).getAuthorities();
            return Optional.ofNullable(new ArrayList<>(authorities).get(0).getAuthority());
        }
        return Optional.empty();
    }
}
