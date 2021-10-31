package com.demo.auth;

import com.demo.auth.component.RsaBCryptPasswordEncoder;
import com.demo.auth.domain.entity.UserAccount;
import com.demo.auth.domain.entity.UserRole;
import com.demo.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class AuthApp implements CommandLineRunner {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RsaBCryptPasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void run(String... args) throws Exception {
        UserRole role = new UserRole();
        role.setRole("USER");
        role.setDescription("Normal user role");
        role.setCreatedBy("System");
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername("bucky");
        userAccount.setPassword(passwordEncoder.encode("1234"));
        userAccount.setCustomerId(1);
        userAccount.setCreatedBy("System");
        userAccount.getRoles().add(role);
        repository.save(userAccount);
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthApp.class, args);
    }
}
