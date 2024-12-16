package com.be_planfortrips.CouponController;

import com.be_planfortrips.controllers.CouponController;
import com.be_planfortrips.dto.CouponDto;
import com.be_planfortrips.dto.response.CouponResponse;
import com.be_planfortrips.services.interfaces.ICouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponControllerTest {

    @Mock
    private ICouponService couponService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private CouponController couponController;

    private CouponDto couponDto;
    private CouponResponse couponResponse;

    private static final String REQUIRED_FIELD_ERROR = "Mã coupon là bắt buộc";
    private static final Integer VALID_COUPON_ID = 1;
    private static final Integer INVALID_COUPON_ID = 999;

    @BeforeEach
    void setUp() {
        couponDto = MockCouponDto();
        couponResponse = MockCouponResponse();
    }

    private CouponDto MockCouponDto() {
        return CouponDto.builder()
                .code("TESTCODE")
                .discountType("")
                .discountValue(10.0)
                .useLimit(100)
                .isActive(true)
                .build();
    }

    private CouponResponse MockCouponResponse() {
        return CouponResponse.builder()
                .id(VALID_COUPON_ID)
                .code("TESTCODE")
                .discountType("")
                .discountValue(10.0)
                .useLimit(100)
                .isActive(true)
                .build();
    }

    @Test
    void createCoupon_Success() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(couponService.createCoupon(couponDto)).thenReturn(couponResponse);

        ResponseEntity<?> response = couponController.createCoupon(couponDto, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(couponResponse, response.getBody());
        verify(couponService, times(1)).createCoupon(couponDto);
    }

    @Test
    void createCoupon_Failure_ValidationErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(
                new FieldError("couponDto", "code", REQUIRED_FIELD_ERROR)));

        ResponseEntity<?> response = couponController.createCoupon(couponDto, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        List<String> errors = (List<String>) response.getBody();
        assertEquals(1, errors.size());
        assertEquals(REQUIRED_FIELD_ERROR, errors.get(0));
    }

    @Test
    void createCoupon_Failure_ExceptionThrown() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(couponService.createCoupon(couponDto)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = couponController.createCoupon(couponDto, bindingResult);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error", response.getBody());
    }

    @Test
    void updateCoupon_Success() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(couponService.updateCoupon(VALID_COUPON_ID, couponDto)).thenReturn(couponResponse);

        ResponseEntity<?> response = couponController.updateCoupon(VALID_COUPON_ID, couponDto, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(couponResponse, response.getBody());
        verify(couponService, times(1)).updateCoupon(VALID_COUPON_ID, couponDto);
    }

    @Test
    void updateCoupon_Failure_ValidationErrors() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(
                new FieldError("couponDto", "code", REQUIRED_FIELD_ERROR)));

        ResponseEntity<?> response = couponController.updateCoupon(VALID_COUPON_ID, couponDto, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        List<String> errors = (List<String>) response.getBody();
        assertEquals(1, errors.size());
        assertEquals(REQUIRED_FIELD_ERROR, errors.get(0));
    }

    @Test
    void deleteCouponById_Success() throws Exception {
        doNothing().when(couponService).deleteCouponById(VALID_COUPON_ID);

        ResponseEntity<?> response = couponController.deleteCouponById(VALID_COUPON_ID);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(couponService, times(1)).deleteCouponById(VALID_COUPON_ID);
    }

    @Test
    void deleteCouponById_Failure_NotFound() throws Exception {
        doThrow(new RuntimeException("Không tìm thấy mã coupon"))
                .when(couponService).deleteCouponById(INVALID_COUPON_ID);

        ResponseEntity<?> response = couponController.deleteCouponById(INVALID_COUPON_ID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Không tìm thấy mã coupon", response.getBody());
    }

    @Test
    void getCouponById_Success() throws Exception {
        when(couponService.getByCouponId(VALID_COUPON_ID)).thenReturn(couponResponse);

        ResponseEntity<?> response = couponController.getCouponById(VALID_COUPON_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(couponResponse, response.getBody());
    }

    @Test
    void getCouponById_Failure_NotFound() throws Exception {
        when(couponService.getByCouponId(INVALID_COUPON_ID)).thenThrow(new RuntimeException("Không tìm thấy mã coupon"));

        ResponseEntity<?> response = couponController.getCouponById(INVALID_COUPON_ID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Không tìm thấy mã coupon", response.getBody());
    }

    @Test
    void getCouponByCode_Success() {
        String code = "DISCOUNT20";
        when(couponService.getByCouponCode(code, "true")).thenReturn(couponResponse);

        ResponseEntity<?> response = couponController.getCouponByCode(code, "true");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(couponResponse, response.getBody());
    }

    @Test
    void getCouponByCode_Failure_NotFound() {
        String code = "INVALIDCODE";
        when(couponService.getByCouponCode(code, "true")).thenThrow(new RuntimeException("Không tìm thấy mã coupon"));

        ResponseEntity<?> response = couponController.getCouponByCode(code, "true");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Không tìm thấy mã coupon", response.getBody());
    }
}