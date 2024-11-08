package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.entity.Admin;
import com.be_planfortrips.entity.User;
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
    public Long getIdUserByToken() {
        return getIdByToken("ROLE_USER");
    }

    @Override
    public Long getIdAdminByToken() {
        return getIdByToken("ROLE_ADMIN");
    }

    @Override
    public Long getIdEnterpriseByToken() {
        return getIdByToken("ROLE_ENTERPRISE");
    }

    private Long getIdByToken(String role) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.error("Không thể lấy thông tin người dùng hiện tại");
            throw new MessageDescriptorFormatException("Không thể lấy thông tin người dùng hiện tại");
        }

        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        String userRole = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role không hợp lệ"));

        if (!userRole.equals(role)) {
            log.error("Role của người dùng không khớp với yêu cầu: {}", role);
            throw new MessageDescriptorFormatException("Role không khớp yêu cầu");
        }

        return findIdByRoleAndUsername(role, username);
    }

    private Long findIdByRoleAndUsername(String role, String username) {
        switch (role) {
            case "ROLE_USER":
                User user = userRepository.findByUsername(username);
                if (user == null) {
                    log.error("Không tìm thấy username {}", username);
                    throw new MessageDescriptorFormatException("Username không tồn tại");
                }
                return user.getId();
            case "ROLE_ADMIN":
                Admin admin = adminRepository.findByUsername(username);
                if (admin == null) {
                    log.error("Không tìm thấy username {}", username);
                    throw new MessageDescriptorFormatException("Username không tồn tại");
                }
                return admin.getId();
            case "ROLE_ENTERPRISE":
                AccountEnterprise enterprise = accountEnterpriseRepository.findByUsername(username);
                if (enterprise == null) {
                    log.error("Không tìm thấy username {}", username);
                    throw new MessageDescriptorFormatException("Username không tồn tại");
                }
                return enterprise.getAccountEnterpriseId();
            default:
                throw new MessageDescriptorFormatException("Role không hợp lệ");
        }
    }
}
