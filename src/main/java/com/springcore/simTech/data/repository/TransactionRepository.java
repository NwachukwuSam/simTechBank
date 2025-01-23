package com.springcore.simTech.data.repository;

import com.springcore.simTech.data.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {


}
