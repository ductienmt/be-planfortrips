package com.be_planfortrips.security.userPrincipal;

import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.entity.Admin;
import com.be_planfortrips.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(User user) {
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().getName())
        );
    }


    public UserPrincipal(Admin admin) {
        this.username = admin.getUserName();
        this.password = admin.getPassword();
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority(admin.getRole().getName())
        );
    }

    public UserPrincipal(AccountEnterprise enterprise) {
        this.username = enterprise.getUsername();
        this.password = enterprise.getPassword();
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority(enterprise.getRole().getName())
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserPrincipal create(Object account) {
        if (account instanceof User) {
            return new UserPrincipal((User) account);
        } else if (account instanceof Admin) {
            return new UserPrincipal((Admin) account);
        } else if (account instanceof AccountEnterprise) {
            return new UserPrincipal((AccountEnterprise) account);
        } else {
            throw new IllegalArgumentException("Unsupported account type");
        }
    }
}