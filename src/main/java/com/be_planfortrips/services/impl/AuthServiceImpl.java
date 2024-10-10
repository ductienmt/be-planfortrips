package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.request.LoginDto;
import com.be_planfortrips.dto.response.AccountUserResponse;
import com.be_planfortrips.dto.response.AuthResponse;
import com.be_planfortrips.services.interfaces.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserServiceImpl userService;

    @Override
    public void register(UserDto userDto) {
        this.userService.createUser(userDto);
    }

    @Override
    public AuthResponse login(LoginDto loginDto) {
        if (loginDto == null)
            throw new RuntimeException("Vui lòng nhập thông tin đăng nhập");
        AccountUserResponse accountUserResponse = this.userService.getUserByUsername(loginDto.getUserName());
        if (accountUserResponse == null)
            throw new RuntimeException("Username không tồn tại");
        if (!accountUserResponse.getPassword().equals(loginDto.getPassword())) {
            throw new RuntimeException("Mật khẩu không chính xác");
        }
        return new AuthResponse(
                accountUserResponse.getUserName(),
                accountUserResponse.getPhoneNumber(),
                accountUserResponse.getFullName(),
                accountUserResponse.getEmail()
        );
    }
}
