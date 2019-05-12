package com.liandao.onlineteaching.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Note {
    private int id;
    private String title;
    private String content;
    private String type;
    private int userId;
    private int categoryId;
    private int readCount;
    private String categoryName;
    private String author;
    private int collectId;
    private Timestamp createTime;
    private Timestamp updateTime;
}
