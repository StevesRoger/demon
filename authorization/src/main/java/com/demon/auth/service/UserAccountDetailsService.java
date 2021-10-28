package com.demon.auth.service;

import com.demon.auth.domain.entity.Status;
import com.demon.auth.domain.entity.UserAccount;
import com.demon.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created: KimChheng
 * Date: 20-Dec-2020 Sun
 * Time: 11:37 AM
 */
@Service
public class UserAccountDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = repository.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (userAccount.getAuthorities().isEmpty()) throw new BadCredentialsException("User has no role");
        return userAccount;
    }
}
