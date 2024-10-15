package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.request.LoginDto;
import com.be_planfortrips.dto.response.AccountUserResponse;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.AuthResponse;
import com.be_planfortrips.services.interfaces.IAuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@Slf4j
public class AuthController {

    @Autowired
    private IAuthService authService;

    // Phương thức hỗ trợ để tạo ApiResponse
    private ResponseEntity<ApiResponse<Void>> buildApiResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(
                ApiResponse.<Void>builder()
                        .code(status.value())
                        .message(message)
                        .build()
        );
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

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            AuthResponse authResponse = this.authService.login(loginDto);
            return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
                    .code(HttpStatus.OK.value())
                    .data(authResponse)
                    .message("Đăng nhập thành công.")
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            return buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Đăng nhập thất bại.");
        }
    }
}
