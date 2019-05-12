package com.liandao.onlineteaching.param;

import lombok.Data;

@Data
public class QueryUser {
    private String account;
    private String username;
    private String role;
    private int pageNum = 1;
    private int pageSize = 10;
}
