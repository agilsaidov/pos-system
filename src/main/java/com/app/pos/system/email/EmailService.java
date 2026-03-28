package com.app.pos.system.email;

public interface EmailService {
    void sendOtpWithAttachment(String to, String otp);
}
