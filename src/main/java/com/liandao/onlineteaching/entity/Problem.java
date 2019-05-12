package com.liandao.onlineteaching.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Problem {
    private int id;
    private String title;
    private String content;
    private String status;
    private int userId;
    private int categoryId;
    private String author;
    private String categoryName;
    private Timestamp createTime;
    private Timestamp updateTime;
}
