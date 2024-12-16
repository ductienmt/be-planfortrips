package com.be_planfortrips.controller;

import com.be_planfortrips.controllers.AccountEnterpriseController;
import com.be_planfortrips.dto.AccountEnterpriseDto;
import com.be_planfortrips.dto.response.AccountEnterpriseResponse;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.services.interfaces.IAccountEnterpriseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountEnterpriseControllerTest {

    @Mock
    private IAccountEnterpriseService accountEnterpriseService;

    @InjectMocks
    private AccountEnterpriseController accountEnterpriseController;

    private AccountEnterpriseResponse accountEnterpriseResponse;



    @Test
    void getAccountEnterpriseById_Success() {
        Long id = 1L;
        when(accountEnterpriseService.getAccountEnterpriseById(id)).thenReturn(accountEnterpriseResponse);

        ResponseEntity<AccountEnterpriseResponse> response = accountEnterpriseController.getAccountEnterpriseById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountEnterpriseResponse, response.getBody());
        verify(accountEnterpriseService, times(1)).getAccountEnterpriseById(id);
    }

    @Test
    void getAccountEnterpriseById_NotFound() {
        Long id = 1L;
        when(accountEnterpriseService.getAccountEnterpriseById(id)).thenThrow(new AppException(ErrorType.notFound));

        ResponseEntity<AccountEnterpriseResponse> response = accountEnterpriseController.getAccountEnterpriseById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(accountEnterpriseService, times(1)).getAccountEnterpriseById(id);
    }

    @Test
    void createAccountEnterprise_Success() {
        AccountEnterpriseDto mockDto = new AccountEnterpriseDto();
        when(accountEnterpriseService.createAccountEnterprise(mockDto)).thenReturn(accountEnterpriseResponse);

        ResponseEntity<AccountEnterpriseResponse> response = accountEnterpriseController.createAccountEnterprise(mockDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(accountEnterpriseResponse, response.getBody());
        verify(accountEnterpriseService, times(1)).createAccountEnterprise(mockDto);
    }

    @Test
    void createAccountEnterprise_Failure() {
        AccountEnterpriseDto mockDto = new AccountEnterpriseDto();
        when(accountEnterpriseService.createAccountEnterprise(mockDto)).thenThrow(new AppException(ErrorType.inputFieldInvalid));

        ResponseEntity<AccountEnterpriseResponse> response = accountEnterpriseController.createAccountEnterprise(mockDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(accountEnterpriseService, times(1)).createAccountEnterprise(mockDto);
    }

    @Test
    void updateAccountEnterprise_Success() {
        AccountEnterpriseDto mockDto = new AccountEnterpriseDto();
        when(accountEnterpriseService.updateAccountEnterprise(mockDto)).thenReturn(accountEnterpriseResponse);

        ResponseEntity<AccountEnterpriseResponse> response = accountEnterpriseController.updateAccountEnterprise(mockDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountEnterpriseResponse, response.getBody());
        verify(accountEnterpriseService, times(1)).updateAccountEnterprise(mockDto);
    }

    @Test
    void updateAccountEnterprise_Failure() {
        AccountEnterpriseDto mockDto = new AccountEnterpriseDto();
        when(accountEnterpriseService.updateAccountEnterprise(mockDto)).thenThrow(new AppException(ErrorType.notFound));

        ResponseEntity<AccountEnterpriseResponse> response = accountEnterpriseController.updateAccountEnterprise(mockDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(accountEnterpriseService, times(1)).updateAccountEnterprise(mockDto);
    }

    @Test
    void deleteAccountEnterpriseById() {
        Long id = 1L;
        doNothing().when(accountEnterpriseService).deleteAccountEnterpriseById(id);

        ResponseEntity<Void> response = accountEnterpriseController.deleteAccountEnterpriseById(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(accountEnterpriseService, times(1)).deleteAccountEnterpriseById(id);
    }

    @Test
    void toggleStage_Success() {
        Long id = 1L;
        when(accountEnterpriseService.toggleStage(id)).thenReturn(true);

        ResponseEntity<Boolean> response = accountEnterpriseController.toggleStage(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        verify(accountEnterpriseService, times(1)).toggleStage(id);
    }

    @Test
    void validateUsername_Success() {
        String username = "validUsername";
        doNothing().when(accountEnterpriseService).validateUsername(username);

        ResponseEntity<?> response = accountEnterpriseController.validateUsername(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tên đăng nhập hợp lệ.", ((Map<?, ?>) response.getBody()).get("message"));
        verify(accountEnterpriseService, times(1)).validateUsername(username);
    }

    @Test
    void validateUsername_AlreadyExists() {
        String username = "existingUsername";
        doThrow(new AppException(ErrorType.usernameExisted)).when(accountEnterpriseService).validateUsername(username);

        ResponseEntity<?> response = accountEnterpriseController.validateUsername(username);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Tên đăng nhập không hợp lệ.", ((Map<?, ?>) response.getBody()).get("message"));
        verify(accountEnterpriseService, times(1)).validateUsername(username);
    }

    @Test
    void validateEmail_Success() {
        String email = "example@gmail.com";
        doNothing().when(accountEnterpriseService).validateEmail(email);

        ResponseEntity<?> response = accountEnterpriseController.validateEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email hợp lệ.", ((Map<?, ?>) response.getBody()).get("message"));
        verify(accountEnterpriseService, times(1)).validateEmail(email);
    }

    @Test
    void validateEmail_AlreadyExists() {
        String email = "existing@example.com";
        doThrow(new AppException(ErrorType.emailExisted)).when(accountEnterpriseService).validateEmail(email);

        ResponseEntity<?> response = accountEnterpriseController.validateEmail(email);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email không hợp lệ.", ((Map<?, ?>) response.getBody()).get("message"));
        verify(accountEnterpriseService, times(1)).validateEmail(email);
    }

    @Test
    void validatePhone_Success() {
        String phone = "0123456789";
        doNothing().when(accountEnterpriseService).validatePhone(phone);

        ResponseEntity<?> response = accountEnterpriseController.validatePhone(phone);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Phone hợp lệ.", ((Map<?, ?>) response.getBody()).get("message"));
        verify(accountEnterpriseService, times(1)).validatePhone(phone);
    }

    @Test
    void validatePhone_AlreadyExists() {
        String phone = "1234567890";
        doThrow(new AppException(ErrorType.phoneExisted)).when(accountEnterpriseService).validatePhone(phone);

        ResponseEntity<?> response = accountEnterpriseController.validatePhone(phone);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Phone không hợp lệ.", ((Map<?, ?>) response.getBody()).get("message"));
        verify(accountEnterpriseService, times(1)).validatePhone(phone);
    }
}
