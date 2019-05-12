package com.liandao.onlineteaching.param;

import lombok.Data;

@Data
public class QueryCourse {
    private int id;
    private String name;
    private int categoryId;
    private String priority;
    private int pageNum = 1;
    private int pageSize = 10;
}
