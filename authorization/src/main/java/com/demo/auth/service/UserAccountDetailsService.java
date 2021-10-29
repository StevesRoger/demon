package com.demo.auth.service;

import com.demo.auth.domain.entity.Status;
import com.demo.auth.domain.entity.UserAccount;
import com.demo.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
