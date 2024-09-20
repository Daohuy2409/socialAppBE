package com.example.web.controller;

import com.example.web.entity.OtpDetail;
import com.example.web.response.EHttpStatus;
import com.example.web.response.Response;
import com.example.web.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.web.service.AccountService;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired private AccountService accountService;
    @Autowired private EmailService emailService;

    private final ConcurrentHashMap<String, OtpDetail> otpStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> changePassState = new ConcurrentHashMap<>();

    @GetMapping("/login")
    public Response<?> login(@RequestParam String username, String password) {
        if (accountService.login(username, password)) {
            return new Response<>(EHttpStatus.OK);
        } else {
            return new Response<>(EHttpStatus.INVALID_INFORMATION);
        }
    }

    @GetMapping("/forgotPass")
    public Response<?> forgotPass(@RequestParam String username) {
        if (accountService.findAccountByUsername(username)) {
            return new Response<>(EHttpStatus.INCORRECT_INFORMATION, "Username does not exist!");
        }
        //check if user received otp in last 5 minutes
        if (otpStore.containsKey(username) && otpStore.get(username).getExpiryTime().isAfter(LocalDateTime.now())) {
            return new Response<>(EHttpStatus.BAD_REQUEST, "OTP already sent. Please verify!");
        }
        //generate otp
        Random random = new Random();
        int otp = random.nextInt(900000) + 100000; // Generate 6-digit OTP
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(1); // Set expiry time to 1 minutes from now
        otpStore.put(username, new OtpDetail(String.valueOf(otp), expiryTime));

        // send otp to user
        try {
            emailService.sendHTMLMail("", username,
                    "The OTP is: " + otp + " for reset your password");
            System.out.println("Email sent successfully");
        } catch (Exception e) {
            System.out.println("This is error: " + e.getMessage());
        }
        changePassState.put(username, "OTP_SENT");
        return new Response<>(EHttpStatus.OK, "OTP sent to your email. Please verify to reset password.");
    }

    @GetMapping("/checkOtp")
    public Response<?> checkOtp(@RequestParam String username, String otp) {
        if (!changePassState.containsKey(username) || !changePassState.get(username).equals("OTP_SENT")) {
            return new Response<>(EHttpStatus.BAD_REQUEST, "You must give email to send OTP.");
        }
        if (!otpStore.containsKey(username)) {
            return new Response<>(EHttpStatus.BAD_REQUEST, "OTP not found");
        }
        OtpDetail otpDetail = otpStore.get(username);
        if (otpDetail.getExpiryTime().isBefore(LocalDateTime.now())) {
            return new Response<>(EHttpStatus.BAD_REQUEST, "OTP has expired. Please request a new one.");
        }
        if (!otpDetail.getOtp().equals(otp)) {
            return new Response<>(EHttpStatus.INCORRECT_INFORMATION, "Invalid OTP");
        }
        changePassState.put(username, "OTP_VERIFIED");
        return new Response<>(EHttpStatus.OK, "Verify otp successfully!");
    }
    @GetMapping("/resetPass")
    public Response<?> resetPass(@RequestParam String username, String newPassword) {
        if (!changePassState.containsKey(username) || !changePassState.get(username).equals("OTP_VERIFIED")) {
            return new Response<>(EHttpStatus.BAD_REQUEST, "You must verify OTP first.");
        }
        accountService.changePassword(username, newPassword);
        changePassState.remove(username);
        return new Response<>(EHttpStatus.OK, "Change password successfully");
    }
    @GetMapping("/getAccounts")
    public Response<?> getAccounts() {
        return new Response<>(EHttpStatus.OK, accountService.getAllAccounts());
    }
}
