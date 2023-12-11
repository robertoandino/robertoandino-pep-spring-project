package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{

    @Query(value = "SELECT * FROM account WHERE account_id = ?1", nativeQuery = true)
    Optional<Account> findAccountByID(int id);

    @Query(value = "SELECT * FROM account WHERE username = ?1", nativeQuery = true)
    Optional<Account> findAccountByUsername(String username);
}

