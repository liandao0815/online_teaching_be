package com.liandao.onlineteaching.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class LiveRoom {
    private int id;
    private String title;
    private String poster;
    private String banner;
    private String status;
    private String living;
    private int userId;
    private String author;
    private Timestamp liveTime;
    private Timestamp createTime;
    private Timestamp updateTime;
}
