package com.liandao.onlineteaching.param;

import lombok.Data;

@Data
public class QueryNote {
    private int id;
    private String title;
    private int pageNum = 1;
    private int pageSize = 10;
}
