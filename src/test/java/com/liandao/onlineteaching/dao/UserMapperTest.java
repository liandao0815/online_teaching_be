package com.liandao.onlineteaching.dao;

import com.liandao.onlineteaching.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void createUser() {
        User user = new User();
        user.setAccount("3115004308");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole("TEACHER");
        user.setUsername("黄老师");

        userMapper.create(user);
    }
}