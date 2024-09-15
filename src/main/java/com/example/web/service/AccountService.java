package com.example.web.service;

import com.example.web.entity.Account;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    Boolean login(String username, String password);

    Account addAccount(String username, String password);

    Boolean findAccountByUsername(String username);
}
