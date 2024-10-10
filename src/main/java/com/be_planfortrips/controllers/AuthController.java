package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.request.LoginDto;
import com.be_planfortrips.dto.response.AccountUserResponse;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.AuthResponse;
import com.be_planfortrips.services.interfaces.IAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) {
        try {
            this.authService.register(userDto);
            return ResponseEntity.ok(ApiResponse.<AccountUserResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Đăng ký thành công.")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message(e.getMessage())
                            .build()
            );
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message(e.getMessage())
                            .build()
            );
        }
    }
}
