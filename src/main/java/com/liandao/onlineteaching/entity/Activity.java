package com.liandao.onlineteaching.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Activity {
    private int id;
    private int year;
    private int month;
    private int count;
    private Timestamp createTime;
    private Timestamp updateTime;
}
