package com.demo.auth.repository;

import com.demo.auth.domain.entity.Status;
import com.demo.auth.domain.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccount, Integer> {

    Optional<UserAccount> findByUsernameAndStatus(String username, Status status);

    Optional<UserAccount> findByCustomerIdAndStatus(Integer id, Status status);
}
