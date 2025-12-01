package com.bank.system2_corebank.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.system2_corebank.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    // This is empty because Spring Boot automatically writes the code 
    // to "Save", "Find", and "Delete" cards for us!
}