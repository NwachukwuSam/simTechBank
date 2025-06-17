package com.springcore.simTech.data.repository;

import com.springcore.simTech.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByAccountNumber(String accountNumber);
    User findByAccountNumber(String accountNumber);
    Optional<User> findUserByEmail(String email);

}
