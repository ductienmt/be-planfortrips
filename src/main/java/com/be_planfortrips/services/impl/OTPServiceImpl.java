package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.OTP;
import com.be_planfortrips.services.interfaces.IOTPService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OTPServiceImpl implements IOTPService {
    private final Map<String, OTP> otpStorage = new HashMap<>();
//    vnaq gabm viir tnos
    private final Random random = new Random();

    private static final long EXPIRY_DURATION = TimeUnit.MINUTES.toMillis(5);

    @Override
    public String generateOTP(String userIdentifier) {
        String otp = String.format("%06d", random.nextInt(1000000));
        OTP otpObj = new OTP(otp, System.currentTimeMillis() + EXPIRY_DURATION);
        otpStorage.put(userIdentifier, otpObj);
        return otp;
    }

    @Override
    public boolean validateOTP(String userIdentifier, String userOtp) {
        OTP otp = otpStorage.get(userIdentifier);
        if (otp != null && otp.getCode().equals(userOtp) && otp.getExpiryTime() >= System.currentTimeMillis()) {
            otpStorage.remove(userIdentifier);
            return true;
        }
        return false;
    }
}
