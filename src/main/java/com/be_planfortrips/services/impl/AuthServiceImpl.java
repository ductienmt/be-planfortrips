package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.OauthModel.TypeLogin;
import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.request.LoginDto;
import com.be_planfortrips.dto.response.AuthResponse;
import com.be_planfortrips.security.jwt.JwtProvider;
import com.be_planfortrips.services.interfaces.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public void register(UserDto userDto) {
        userService.createUser(userDto);
    }

    @Override
    public AuthResponse login(LoginDto loginDto) {
        if (loginDto == null) {
            throw new RuntimeException("Vui lòng nhập thông tin đăng nhập");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
        String role = authorities.isEmpty() ? null : authorities.get(0).getAuthority();
        System.out.println("Role: " + role);

        if (role == null) {
            throw new RuntimeException("User has no role assigned");
        }

        String token = jwtProvider.createToken(userDetails.getUsername(), role, TypeLogin.LOGIN_NORMAL);

        return new AuthResponse(userDetails.getUsername(), token, role, false);
    }
}