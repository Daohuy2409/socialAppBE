package com.example.web.controller;

import com.example.web.dto.AccountInfoDTO;
import com.example.web.entity.OtpDetail;
import com.example.web.response.EHttpStatus;
import com.example.web.response.Response;
import com.example.web.service.AccountService;
import com.example.web.service.UserInfoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private AccountService accountService;

    private ConcurrentHashMap<String, OtpDetail> otpStore = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> userState = new ConcurrentHashMap<>();

    @PostMapping("/sendOtpToUser")
    public Response<?> sendOtpToUser(@RequestParam String name, String username) {
        // check if user already exists
        if (!accountService.findAccountByUsername(username) ) {
            return new Response<>(EHttpStatus.BAD_REQUEST, "Username already exists!");
        }

        //check if user received otp in last 5 minutes
        if (otpStore.containsKey(username) && otpStore.get(username).getExpiryTime().isAfter(LocalDateTime.now())) {
            return new Response<>(EHttpStatus.BAD_REQUEST, "OTP already sent. Please verify!");
        }

        //generate otp
        Random random = new Random();
        int otp = random.nextInt(900000) + 100000; // Generate 6-digit OTP
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5); // Set expiry time to 5 minutes from now
        otpStore.put(username, new OtpDetail(String.valueOf(otp), expiryTime));

        userInfoService.sendOtpToUser(name, username, String.valueOf(otp));
        userState.put(username, "OTP_SENT");

        return new Response<>(EHttpStatus.OK, "OTP sent to your email. Please verify to complete registration.");
    }

    @PostMapping("/verifyByOtp")
    public Response<?> verifyByOtp(@RequestParam String username, String otp) {
        if (!userState.containsKey(username) || !userState.get(username).equals("OTP_SENT")) {
            return new Response<>(EHttpStatus.BAD_REQUEST, "You must request an OTP first.");
        }

        if (!otpStore.containsKey(username)) {
            return new Response<>(EHttpStatus.OK, "OTP not found");
        }

        OtpDetail otpDetail = otpStore.get(username);
        if (otpDetail.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpStore.remove(username);
            return new Response<>(EHttpStatus.BAD_REQUEST,"OTP expired!");
        }

        if (!otpDetail.getOtp().equals(otp)) {
            return new Response<>(EHttpStatus.BAD_REQUEST,"Invalid OTP!");
        }

        otpStore.remove(username); // Remove OTP after verification
        userState.put(username, "OTP_VERIFIED");
        return new Response<>(EHttpStatus.OK, "OTP verified successfully!");
    }

    @PostMapping("/registerUser")
    @Transactional
    public Response<?> addUser(@RequestBody AccountInfoDTO accountInfoDTO) {
        String username = accountInfoDTO.getContactIdentify();
        if (!userState.containsKey(username) || !userState.get(username).equals("OTP_VERIFIED")) {
            return new Response<>(EHttpStatus.BAD_REQUEST, "You must verify OTP first.");
        }

        userInfoService.saveUser(accountInfoDTO);
        userState.remove(username);

        return new Response<>(EHttpStatus.OK);
    }

}
