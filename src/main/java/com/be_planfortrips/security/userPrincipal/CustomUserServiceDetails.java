package com.be_planfortrips.security.userPrincipal;

import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.entity.Admin;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.repositories.AccountEnterpriseRepository;
import com.be_planfortrips.repositories.AdminRepository;
import com.be_planfortrips.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserServiceDetails implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AccountEnterpriseRepository enterpriseRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
        if (user.isPresent()) {
            GrantedAuthority authority = new SimpleGrantedAuthority(user.get().getRole().getName());
            List<GrantedAuthority> authorities = Collections.singletonList(authority);

            return new UserPrincipal(user.get());
        }
        Optional<Admin> admin = Optional.ofNullable(adminRepository.findByUsername(username));
        if (admin.isPresent()) {
            GrantedAuthority authority = new SimpleGrantedAuthority(admin.get().getRole().getName());
            List<GrantedAuthority> authorities = Collections.singletonList(authority);
            return new UserPrincipal(admin.get());
        }
        Optional<AccountEnterprise> enterprise = Optional.ofNullable(enterpriseRepository.findByUsername(username));
        if (enterprise.isPresent()) {
            GrantedAuthority authority = new SimpleGrantedAuthority(enterprise.get().getRole().getName());
            List<GrantedAuthority> authorities = Collections.singletonList(authority);
            return new UserPrincipal(enterprise.get());
        }

        throw new UsernameNotFoundException("Không tìm thấy tài khoản có username: " + username + " trong hệ thống");
    }
}