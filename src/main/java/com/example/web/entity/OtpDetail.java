package com.example.web.entity;
import java.time.LocalDateTime;

public class OtpDetail {
    private String otp;
    private LocalDateTime expiryTime;

    public OtpDetail(String otp, LocalDateTime expiryTime) {
        this.otp = otp;
        this.expiryTime = expiryTime;
    }

    public String getOtp() {
        return otp;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }
}
