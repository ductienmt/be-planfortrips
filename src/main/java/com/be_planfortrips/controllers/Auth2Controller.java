package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.request.LoginDto;
import com.be_planfortrips.dto.response.AccountUserResponse;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.AuthResponse;
import com.be_planfortrips.services.interfaces.IAuth2Service;
import com.be_planfortrips.services.interfaces.IAuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("${api.prefix}/auth")
public class Auth2Controller {
    @Autowired
    private IAuth2Service iAuth2Service;

    @Autowired
    private IAuthService authService;

    // Get Auth2 Url
    @GetMapping("/google/url")
    public ResponseEntity<String> getGoogleAuthorizationUrl() {
        String url = iAuth2Service.getGoogleUrl();
        return ResponseEntity.ok(url);
    }

    @GetMapping("/google/callback")
    public ResponseEntity<AuthResponse> callback(
            @RequestParam("code") String code) throws IOException {
        AuthResponse response = iAuth2Service.loginGoogle(code);
        return ResponseEntity.ok(response);
    }

    // Phương thức hỗ trợ để tạo ApiResponse
    private ResponseEntity<ApiResponse<Void>> buildApiResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(
                ApiResponse.<Void>builder()
                        .code(status.value())
                        .message(message)
                        .build());
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) {
        try {
            this.authService.register(userDto);
            return ResponseEntity.ok(ApiResponse.<AccountUserResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Đăng ký thành công.")
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Đăng ký thất bại.");
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            AuthResponse authResponse = this.authService.loginUser(loginDto);
            return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
                    .code(HttpStatus.OK.value())
                    .data(authResponse)
                    .message("Đăng nhập thành công.")
                    .build());
        } catch (Exception e) {
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody LoginDto loginDto) {
        try {
            AuthResponse authResponse = this.authService.loginAdmin(loginDto);
            return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
                    .code(HttpStatus.OK.value())
                    .data(authResponse)
                    .message("Đăng nhập thành công.")
                    .build());
        } catch (Exception e) {
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/enterprise/login")
    public ResponseEntity<?> loginEnterprise(@Valid @RequestBody LoginDto loginDto, @RequestParam("type") Integer type) {
        try {
            AuthResponse authResponse = this.authService.loginEnterprise(loginDto, type);
            return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
                    .code(HttpStatus.OK.value())
                    .data(authResponse)
                    .message("Đăng nhập thành công.")
                    .build());
        } catch (Exception e) {
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
