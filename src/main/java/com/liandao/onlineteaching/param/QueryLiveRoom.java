package com.liandao.onlineteaching.param;

import lombok.Data;

@Data
public class QueryLiveRoom {
    private int id;
    private String status;
    private String living;
    private int pageNum = 1;
    private int pageSize = 10;
}
