package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.request.LoginDto;
import com.be_planfortrips.dto.response.AuthResponse;

public interface IAuthService {
    void register(UserDto userDto);
    AuthResponse login(LoginDto loginDto);
}
