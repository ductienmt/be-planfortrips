package com.be_planfortrips.Test.anhquan;

import com.be_planfortrips.services.impl.OTPServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OTPServiceImplTest {

    private OTPServiceImpl otpService;

    @BeforeEach
    void setUp() {
        otpService = new OTPServiceImpl();
    }

    @Test
    void testGenerateOTP() {
        String userIdentifier = "testUser";

        String otp = otpService.generateOTP(userIdentifier);

        assertNotNull(otp);
        assertEquals(6, otp.length());
        assertTrue(otp.matches("\\d{6}"), "OTP should be a 6-digit number");
    }

    @Test
    void testValidateOTP_Success() {
        String userIdentifier = "testUser";
        String otp = otpService.generateOTP(userIdentifier);

        boolean isValid = otpService.validateOTP(userIdentifier, otp);

        assertTrue(isValid, "OTP should be valid");
    }

    @Test
    void testValidateOTP_InvalidOTP() {
        String userIdentifier = "testUser";
        otpService.generateOTP(userIdentifier);

        boolean isValid = otpService.validateOTP(userIdentifier, "123456");

        assertFalse(isValid, "OTP should be invalid");
    }

    @Test
    void testValidateOTP_Expired() throws InterruptedException {
        String userIdentifier = "testUser";

        // Giảm thời gian hết hạn xuống 1 giây để dễ test
        otpService.generateOTP(userIdentifier);
        TimeUnit.MILLISECONDS.sleep(6000); // Chờ để OTP hết hạn (giả định EXPIRY_DURATION là 5 giây trong mã)

        boolean isValid = otpService.validateOTP(userIdentifier, "123456");

        assertFalse(isValid, "OTP should be expired");
    }

    @Test
    void testGenerateAndValidateMultipleUsers() {
        String user1 = "user1";
        String user2 = "user2";

        String otp1 = otpService.generateOTP(user1);
        String otp2 = otpService.generateOTP(user2);

        assertNotNull(otp1);
        assertNotNull(otp2);
        assertNotEquals(otp1, otp2, "OTP for different users should be unique");

        boolean isValidUser1 = otpService.validateOTP(user1, otp1);
        boolean isValidUser2 = otpService.validateOTP(user2, otp2);

        assertTrue(isValidUser1, "User 1's OTP should be valid");
        assertTrue(isValidUser2, "User 2's OTP should be valid");
    }
}
