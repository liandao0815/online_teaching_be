package com.liandao.onlineteaching.config;

import com.liandao.onlineteaching.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public Map handleCustomException(CustomException e) {
        return ResponseUtil.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public Map handleBindException(BindException e) {

        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        List<String> errorList = new ArrayList<>();

        for (ObjectError error : errors) {
            errorList.add(error.getDefaultMessage());
        }

        return ResponseUtil.fail(errorList);
    }

    @ExceptionHandler
    public Map handleException(Exception e) {
        if(e instanceof AccessDeniedException)
            return ResponseUtil.fail("没有访问该接口的权限");

        log.error("错误信息：", e);
        return ResponseUtil.fail("服务器内部异常");
    }
}
