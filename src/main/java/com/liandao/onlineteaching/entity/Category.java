package com.liandao.onlineteaching.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Category {
    private int id;
    private String name;
    private Timestamp createTime;
    private Timestamp updateTime;
}
