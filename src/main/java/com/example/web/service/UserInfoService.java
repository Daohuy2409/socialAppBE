package com.example.web.service;

import com.example.web.entity.UserInfo;
import org.springframework.stereotype.Service;

@Service
public interface UserInfoService {
    //  this is the method that send otp to user through email or phone
    void sendOtpToUser(String name, String username, String otp);

    boolean verifyEmailByOtp(String username, String otp);

    void saveUserInfo(UserInfo userInfo);
}
