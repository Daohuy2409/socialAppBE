package com.example.web.controller;

import com.example.web.response.EHttpStatus;
import com.example.web.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.web.service.AccountService;

@CrossOrigin
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/login")
    public Response<?> login(@RequestParam String username, String password) {
        if (accountService.login(username, password)) {
            return new Response<>(EHttpStatus.OK);
        } else {
            return new Response<>(EHttpStatus.INVALID_INFORMATION);
        }
    }
}
