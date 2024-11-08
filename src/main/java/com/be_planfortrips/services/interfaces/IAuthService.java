package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.request.LoginDto;
import com.be_planfortrips.dto.response.AuthResponse;
import com.be_planfortrips.security.userPrincipal.UserPrincipal;

public interface IAuthService {
    void register(UserDto userDto);
    AuthResponse loginUser(LoginDto loginDto);
    AuthResponse loginAdmin(LoginDto loginDto);
    AuthResponse loginEnterprise(LoginDto loginDto, Integer type);
    UserPrincipal loadUserByUsernameAndRole(String role, String username);
}
