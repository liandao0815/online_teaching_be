package com.liandao.onlineteaching.config.security;

import com.liandao.onlineteaching.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserFactory {
    public static CustomUserDetails create(User user) {
        return new CustomUserDetails(
                user.getAccount(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRole()));
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(String role) {
        String[] roleArr = role.split(",");
        return Arrays.stream(roleArr).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
