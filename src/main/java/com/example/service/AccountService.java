package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public void addAccount(Account newAccount){
        accountRepository.save(newAccount);
    }

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    public Optional<Account> retrieveAccountByUsername(String checkAcctByName){
        Optional<Account> checkAcctByNameOptional = accountRepository.findAccountByUsername(checkAcctByName);
        return checkAcctByNameOptional;
    }

    public Optional<Account> retrieveAccountById(int checkAcctById){
        Optional<Account> checkAcctByIdOptional = accountRepository.findAccountByID(checkAcctById);
        return checkAcctByIdOptional;
    }
}
