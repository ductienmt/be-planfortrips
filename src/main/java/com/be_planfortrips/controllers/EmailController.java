package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.response.AccountUserResponse;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.services.impl.UserServiceImpl;
import com.be_planfortrips.services.interfaces.IEmailService;
import com.be_planfortrips.services.interfaces.IOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/send")
    public ResponseEntity<String> sendOTP(@RequestParam String email, @RequestParam String content) {
        try {
            String otp = otpService.generateOTP(email);
            emailService.sendOTPEmail(email, otp, content);

            return ResponseEntity.ok("OTP đã được gửi tới " + email);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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
