package com.liandao.onlineteaching.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Message {
    private int id;
    private String content;
    private String status;
    private int userId;
    private Timestamp createTime;
    private Timestamp updateTime;
}
