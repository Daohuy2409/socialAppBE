package com.example.web.service.impl;

import com.example.web.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.web.repository.AccountRepository;
import com.example.web.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Boolean login(String username, String password) {
        Account result = accountRepository.findAccountByUsername(username);
        if (result == null) {
            return false;
        }
        return result.getPassword().equals(password);
    }

    @Override
    public Account addAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        return accountRepository.save(account);
    }
}
