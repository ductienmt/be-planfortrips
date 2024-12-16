package com.be_planfortrips.Test.anhquan;


import com.be_planfortrips.controllers.VnPayController;
import com.be_planfortrips.dto.VnPayDTO;
import com.be_planfortrips.dto.response.VnpPayResponse;
import com.be_planfortrips.services.interfaces.IVnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VnPayControllerTest {

    @InjectMocks
    VnPayController vnPayController;

    @Mock
    IVnPayService iVnPayService;

    @Mock
    BindingResult bindingResult;

    @Mock
    HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPayment_Success() throws Exception {
        // Arrange
        VnPayDTO vnPayDTO = new VnPayDTO();
        VnpPayResponse mockResponse = new VnpPayResponse();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(iVnPayService.createPayment(any(VnPayDTO.class), any(HttpServletRequest.class))).thenReturn(mockResponse);

        // Act
        ResponseEntity<?> response = vnPayController.createPayment(vnPayDTO, bindingResult, httpServletRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void createPayment_ValidationErrors() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        // Act
        ResponseEntity<?> response = vnPayController.createPayment(new VnPayDTO(), bindingResult, httpServletRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void returnPage_Success() throws Exception {
        // Arrange
        Map<String, String> requestParams = Map.of("param1", "value1");
        when(iVnPayService.returnPage(requestParams)).thenReturn("00");

        // Act
        ResponseEntity<?> response = vnPayController.returnPage(requestParams);

        // Assert
        assertEquals(303, response.getStatusCodeValue());
        assertTrue(response.getHeaders().getLocation().toString().contains("success"));
    }

    @Test
    void returnPage_Failure() throws Exception {
        // Arrange
        Map<String, String> requestParams = Map.of("param1", "value1");
        when(iVnPayService.returnPage(requestParams)).thenReturn("01");

        // Act
        ResponseEntity<?> response = vnPayController.returnPage(requestParams);

        // Assert
        assertEquals(303, response.getStatusCodeValue());
        assertTrue(response.getHeaders().getLocation().toString().contains("fail"));
    }
}
