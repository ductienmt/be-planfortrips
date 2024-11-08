package com.be_planfortrips.controllers;

import com.be_planfortrips.services.interfaces.IEmailService;
import com.be_planfortrips.services.interfaces.IOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/email")
public class EmailController {
    @Autowired
    private IEmailService emailService;

    @Autowired
    private IOTPService otpService;

    @PostMapping("/send")
    public ResponseEntity<String> sendOTP(@RequestParam String email, @RequestParam String content) {
        String otp = otpService.generateOTP(email);
        emailService.sendOTPEmail(email, otp, content);
        return ResponseEntity.ok("OTP đã được gửi tới " + email);
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateOTP(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = otpService.validateOTP(email, otp);
        if (isValid) {
            return ResponseEntity.ok("OTP hợp lệ");
        } else {
            return ResponseEntity.status(400).body("OTP không hợp lệ hoặc đã hết hạn");
        }
    }
}
