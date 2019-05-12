package com.liandao.onlineteaching.config;

import lombok.Data;

@Data
public class CustomException extends RuntimeException {
    private int code;

    public CustomException(){}

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(String message) {
        super(message);
        this.code = 1;
    }
}
