package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.OauthModel.TypeLogin;
import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.dto.request.LoginDto;
import com.be_planfortrips.dto.response.AuthResponse;
import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.entity.Admin;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.repositories.AccountEnterpriseRepository;
import com.be_planfortrips.repositories.AdminRepository;
import com.be_planfortrips.repositories.UserRepository;
import com.be_planfortrips.security.jwt.JwtProvider;
import com.be_planfortrips.security.userPrincipal.UserPrincipal;
import com.be_planfortrips.services.interfaces.IAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountEnterpriseRepository enterpriseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserPrincipal loadUserByUsernameAndRole(String role, String username) throws UsernameNotFoundException {
        if (role.equals("ROLE_USER")) {
            Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
            if (user.isPresent()) {
                GrantedAuthority authority = new SimpleGrantedAuthority(user.get().getRole().getName());
                List<GrantedAuthority> authorities = Collections.singletonList(authority);

                return new UserPrincipal(user.get());
            }
        }

        if (role.equals("ROLE_ADMIN")) {
            Optional<Admin> admin = Optional.ofNullable(adminRepository.findByUsername(username));
            if (admin.isPresent()) {
                GrantedAuthority authority = new SimpleGrantedAuthority(admin.get().getRole().getName());
                List<GrantedAuthority> authorities = Collections.singletonList(authority);
                return new UserPrincipal(admin.get());
            }
        }

        if (role.equals("ROLE_ENTERPRISE")) {
            Optional<AccountEnterprise> enterprise = Optional.ofNullable(enterpriseRepository.findByUsername(username));
            if (enterprise.isPresent()) {
                GrantedAuthority authority = new SimpleGrantedAuthority(enterprise.get().getRole().getName());
                List<GrantedAuthority> authorities = Collections.singletonList(authority);
                return new UserPrincipal(enterprise.get());
            }
        }

        throw new UsernameNotFoundException("Không tìm thấy tài khoản có username: " + username + " trong hệ thống");
    }

    @Override
    public void register(UserDto userDto) {
        userService.createUser(userDto);
    }

    @Override
    public AuthResponse loginUser(LoginDto loginDto) {
//        System.out.println(loginDto);
        if (loginDto == null) {
            throw new RuntimeException("Vui lòng nhập thông tin đăng nhập");
        }

        User user = userRepository.findByUsername(loginDto.getUserName());
        if (user == null) {
            throw new RuntimeException("Tài khoản không tồn tại trong hệ thống");
        }

        String storedPassword = user.getPassword();

        if (passwordEncoder.matches(loginDto.getPassword(), storedPassword)) {
            String token = jwtProvider.createToken(loginDto.getUserName(), "ROLE_USER", TypeLogin.LOGIN_NORMAL);
            return new AuthResponse(loginDto.getUserName(), token, "ROLE_USER", false);
        } else {
            throw new RuntimeException("Kiểm tra lại thông tin tài khoản và mật khẩu");
        }
    }

    @Override
    public AuthResponse loginAdmin(LoginDto loginDto) {
        if (loginDto == null) {
            throw new RuntimeException("Vui lòng nhập thông tin đăng nhập");
        }
        Admin admin = adminRepository.findByUsername(loginDto.getUserName());
        if (admin == null) {
            throw new RuntimeException("Tài khoản không tồn tại trong hệ thống");
        }
        String storedPassword = admin.getPassword();
        if (passwordEncoder.matches(loginDto.getPassword(), storedPassword)) {
            String token = jwtProvider.createToken(loginDto.getUserName(), "ROLE_ADMIN", TypeLogin.LOGIN_NORMAL);
            return new AuthResponse(loginDto.getUserName(), token, "ROLE_ADMIN", false);
        } else {
            throw new RuntimeException("Kiểm tra lại thông tin tài khoản và mật khẩu");
        }
    }

    @Override
    public AuthResponse loginEnterprise(LoginDto loginDto, Integer type) {
        if (loginDto == null) {
            throw new RuntimeException("Vui lòng nhập thông tin đăng nhập");
        }
        if (type == null) {
            throw new RuntimeException("Vui lòng chọn loại dịch vụ");
        }
        AccountEnterprise enterprise = enterpriseRepository.findByUsername(loginDto.getUserName());
        if (enterprise == null) {
            throw new RuntimeException("Tài khoản không tồn tại trong hệ thống");
        }
        if (!type.equals((enterprise.getTypeEnterpriseDetail().getId()).intValue())) {
            throw new RuntimeException("Tài khoản của bạn không kinh doanh dịch vụ này");
        }
        String storedPassword = enterprise.getPassword();
        if (passwordEncoder.matches(loginDto.getPassword(), storedPassword)) {
            String token = jwtProvider.createToken(loginDto.getUserName(), "ROLE_ENTERPRISE", TypeLogin.LOGIN_NORMAL);
            return new AuthResponse(loginDto.getUserName(), token, "ROLE_ENTERPRISE", false);
        } else {
            throw new RuntimeException("Kiểm tra lại thông tin tài khoản và mật khẩu");
        }
    }


}