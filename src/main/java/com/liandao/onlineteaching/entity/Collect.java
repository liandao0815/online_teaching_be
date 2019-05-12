package com.liandao.onlineteaching.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Collect {
    private int id;
    private int userId;
    private int noteId;
    private Timestamp createTime;
    private Timestamp updateTime;
}
