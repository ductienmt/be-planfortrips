package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.mappers.TokenMapper;
import com.be_planfortrips.repositories.AccountEnterpriseRepository;
import com.be_planfortrips.repositories.AdminRepository;
import com.be_planfortrips.repositories.UserRepository;
import com.be_planfortrips.security.userPrincipal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenMapperImpl implements TokenMapper {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final AccountEnterpriseRepository accountEnterpriseRepository;

    @Override
    public Object getUserByToken() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.error("Không thể lấy thông tin người dùng hiện tại");
            throw new MessageDescriptorFormatException("Không thể lấy thông tin người dùng hiện tại");
        }
        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        String userRole = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role không hợp lệ"));

        Object user = new Object();
        if (userRole.equals("ROLE_USER")) {
            user = userRepository.findByUsername(username);
        } else if(userRole.equals("ROLE_ADMIN")) {
            user = adminRepository.findByUsername(username);
        } else if(userRole.equals("ROLE_ENTERPRISE")) {
            user = accountEnterpriseRepository.findByUsername(username);
        }

        if (user == null) {
            log.error("Không tìm thấy username {}", username);
            throw new MessageDescriptorFormatException("Username không tồn tại");
        }

        return user;
    }
}
