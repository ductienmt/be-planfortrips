package com.be_planfortrips.Auth2Controller;

import com.be_planfortrips.controllers.Auth2Controller;
import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.request.LoginDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.AuthResponse;
import com.be_planfortrips.services.interfaces.IAuth2Service;
import com.be_planfortrips.services.interfaces.IAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class Auth2ControllerTest {

    @InjectMocks
    private Auth2Controller auth2Controller;

    @Mock
    private IAuth2Service iAuth2Service;

    @Mock
    private IAuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test cho việc lấy URL xác thực Google
    @Test
    void getGoogleAuthorizationUrl() {
        String expectedUrl = "https://google.com/auth";
        when(iAuth2Service.getGoogleUrl()).thenReturn(expectedUrl);

        ResponseEntity<?> response = auth2Controller.getGoogleAuthorizationUrl();

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(expectedUrl, response.getBody());
        verify(iAuth2Service, times(1)).getGoogleUrl();
    }

    // Test cho việc đăng ký thành công
    @Test
    void registerSuccess() {
        UserDto userDto = new UserDto();
        userDto.setUserName("newUser");
        userDto.setPassword("securePassword");

        doNothing().when(authService).register(userDto);

        ResponseEntity<?> response = auth2Controller.register(userDto);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("Đăng ký thành công.", apiResponse.getMessage());
        verify(authService, times(1)).register(userDto);
    }

    // Test cho việc đăng ký thất bại
    @Test
    void registerFail() {
        UserDto userDto = new UserDto();
        userDto.setUserName("newUser");
        userDto.setPassword("securePassword");

        doThrow(new RuntimeException("Service error")).when(authService).register(userDto);

        ResponseEntity<?> response = auth2Controller.register(userDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("Đăng ký thất bại.", apiResponse.getMessage());
        verify(authService, times(1)).register(userDto);
    }

    // Test cho việc đăng nhập thành công
    @Test
    void loginUserSuccess() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUserName("user");
        loginDto.setPassword("userPassword");

        AuthResponse mockAuthResponse = new AuthResponse();
        when(authService.loginUser(loginDto)).thenReturn(mockAuthResponse);

        ResponseEntity<?> response = auth2Controller.login(loginDto);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        ApiResponse<AuthResponse> apiResponse = (ApiResponse<AuthResponse>) response.getBody();
        assertEquals("Đăng nhập thành công.", apiResponse.getMessage());
        assertEquals(mockAuthResponse, apiResponse.getData());
        verify(authService, times(1)).loginUser(loginDto);
    }

    // Test cho việc đăng nhập thất bại
    @Test
    void loginUserFail() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUserName("user");
        loginDto.setPassword("userPassword");

        when(authService.loginUser(loginDto)).thenThrow(new RuntimeException("Đăng nhập không thành công."));

        ResponseEntity<?> response = auth2Controller.login(loginDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("Đăng nhập không thành công.", apiResponse.getMessage());
        verify(authService, times(1)).loginUser(loginDto);
    }

    // Test cho việc đăng nhập admin thành công
    @Test
    void loginAdminSuccess() {
        LoginDto adminLoginDto = new LoginDto();
        adminLoginDto.setUserName("admin");
        adminLoginDto.setPassword("securePassword");

        AuthResponse mockAuthResponse = new AuthResponse();
        when(authService.loginAdmin(adminLoginDto)).thenReturn(mockAuthResponse);

        ResponseEntity<?> response = auth2Controller.loginAdmin(adminLoginDto);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        ApiResponse<AuthResponse> apiResponse = (ApiResponse<AuthResponse>) response.getBody();
        assertEquals("Đăng nhập thành công.", apiResponse.getMessage());
        assertEquals(mockAuthResponse, apiResponse.getData());
        verify(authService, times(1)).loginAdmin(adminLoginDto);
    }

    // Test cho việc đăng nhập admin thất bại
    @Test
    void loginAdminFail() {
        LoginDto adminLoginDto = new LoginDto();
        adminLoginDto.setUserName("admin");
        adminLoginDto.setPassword("securePassword");

        when(authService.loginAdmin(adminLoginDto)).thenThrow(new RuntimeException("Đăng nhập không thành công."));

        ResponseEntity<?> response = auth2Controller.loginAdmin(adminLoginDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
        assertEquals("Đăng nhập không thành công.", apiResponse.getMessage());
        verify(authService, times(1)).loginAdmin(adminLoginDto);
    }
}