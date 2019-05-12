package com.liandao.onlineteaching.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Evaluation {
    private int id;
    private String content;
    private double score;
    private int userId;
    private int courseId;
    private String author;
    private Timestamp createTime;
    private Timestamp updateTime;
}
