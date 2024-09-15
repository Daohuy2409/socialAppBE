package com.example.web.service.impl;

import com.example.web.entity.Account;
import com.example.web.entity.UserInfo;
import com.example.web.repository.UserInfoRepository;
import com.example.web.service.EmailService;
import com.example.web.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired private UserInfoRepository userInfoRepository;
    @Autowired private EmailService emailService;



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
    public void saveUserInfo(UserInfo userInfo) {
        addUserInfo(userInfo);
    }

    private UserInfo addUserInfo(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }

}
