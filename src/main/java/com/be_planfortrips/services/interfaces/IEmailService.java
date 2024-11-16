package com.be_planfortrips.services.interfaces;

public interface IEmailService {
    void sendOTPEmail(String toEmail, String otp, String content);

    void sendEmail(String toEmail, String pass, String content);
}
