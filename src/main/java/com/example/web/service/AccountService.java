package com.example.web.service;

import com.example.web.entity.Account;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    Boolean login(String username, String password);

    Boolean addAccount(String username, String password);

    Boolean findAccountByUsername(String username);

    void changePassword(String username, String newPassword);

    Account[] getAllAccounts();
}
