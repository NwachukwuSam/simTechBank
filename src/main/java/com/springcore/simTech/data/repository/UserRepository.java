package com.springcore.simTech.data.repository;

import com.springcore.simTech.data.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByAccountNumber(String accountNumber);
    Users findByAccountNumber(String accountNumber);
    Optional<Users> findUserByEmail(String email);

}
