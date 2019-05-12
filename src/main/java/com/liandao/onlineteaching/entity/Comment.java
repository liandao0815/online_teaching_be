package com.liandao.onlineteaching.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Comment {
    private int id;
    private String content;
    private int userId;
    private int noteId;
    private String author;
    private Timestamp createTime;
    private Timestamp updateTime;
}
