package com.bank.system2_corebank.repository;

import com.bank.system2_corebank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // This allows us to save and search through transaction history
}