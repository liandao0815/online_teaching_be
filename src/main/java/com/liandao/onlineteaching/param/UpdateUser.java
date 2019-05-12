package com.liandao.onlineteaching.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateUser {
    @NotBlank(message = "用户账号不能为空")
    private String account;
    @NotBlank(message = "用户名不能为空")
    private String username;
    private String role;
}
