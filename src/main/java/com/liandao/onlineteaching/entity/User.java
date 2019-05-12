package com.liandao.onlineteaching.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class User {
    private int id;
    private String account;
    private String password;
    private String username;
    private String avatar;
    private String role;
    private String token;
    private Timestamp loginTime;
    private Timestamp createTime;
    private Timestamp updateTime;
}
