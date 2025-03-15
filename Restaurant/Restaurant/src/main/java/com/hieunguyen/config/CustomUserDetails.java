package com.hieunguyen.config;

import com.hieunguyen.entity.User;
import com.hieunguyen.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Gán quyền từ vai trò (role) của user
        return Collections.singleton(() -> "ROLE_" + user.getRole().name());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // Spring Security dùng "username" để xác định user
        // nhưng mình có thể dùng email hoặc phone tùy cách triển khai
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // bạn có thể thêm logic kiểm tra nếu muốn
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // kiểm tra nếu tài khoản bị khóa
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // kiểm tra nếu mật khẩu hết hạn
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus().name().equals("ACTIVE");
    }

    public String getPhone() {
        return user.getPhone();
    }

    public Role getRole() {
        return user.getRole();
    }
}

