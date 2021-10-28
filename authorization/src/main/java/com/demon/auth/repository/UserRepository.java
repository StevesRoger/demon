package com.demon.auth.repository;

import com.demon.auth.domain.entity.Status;
import com.demon.auth.domain.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Integer> {

    Optional<UserAccount> findByUsernameAndStatus(String username, Status status);
}
