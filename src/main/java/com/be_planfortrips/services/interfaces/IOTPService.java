package com.be_planfortrips.services.interfaces;

public interface IOTPService {
    String generateOTP(String userIdentifier);
    boolean validateOTP(String userIdentifier, String userOtp);
}
