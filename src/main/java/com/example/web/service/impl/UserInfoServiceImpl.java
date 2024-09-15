package com.example.web.service.impl;

import com.example.web.dto.AccountInfoDTO;
import com.example.web.entity.Account;
import com.example.web.entity.UserInfo;

import com.example.web.repository.UserInfoRepository;
import com.example.web.service.AccountService;
import com.example.web.service.EmailService;
import com.example.web.service.UserInfoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired private UserInfoRepository userInfoRepository;
    @Autowired private EmailService emailService;
    @Autowired private AccountService accountService;

    //  this is the method that send otp to user through email or phone
    @Override
    public void sendOtpToUser(String name, String username, String otp) {
        // send otp to user
        try {
            emailService.sendHTMLMail(name, username,
                    "The OTP is: " + otp + " for registration");
            System.out.println("Email sent successfully");
        } catch (Exception e) {
            System.out.println("This is error: " + e.getMessage());
        }


    }

//    @Override
//    public void regiterUser

    @Override
    @Transactional
    public void saveUser(AccountInfoDTO accountInfoDTO) {
        // add user info
        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName(accountInfoDTO.getFirstName());
        userInfo.setLastName(accountInfoDTO.getLastName());
        userInfo.setContactIdentify(accountInfoDTO.getContactIdentify());
        userInfo.setAddress(accountInfoDTO.getAddress());
        userInfo.setDateOfBirth(accountInfoDTO.getDateOfBirth());
        userInfo.setSex(accountInfoDTO.getSex());

        addUserInfo(userInfo);
        userInfoRepository.flush();

        // add account
        String password = accountInfoDTO.getPassword();
        Account account = new Account();
        account.setUsername(accountInfoDTO.getContactIdentify());
        account.setPassword(password);
        accountService.addAccount(accountInfoDTO.getContactIdentify(), password);

    }

    private Boolean addUserInfo(UserInfo userInfo) {
        try {
            userInfoRepository.save(userInfo);
            return true;
        } catch (Exception e) {
            System.out.println("This is error: " + e.getMessage());
            return false;
        }
    }

}
