package com.liandao.onlineteaching.controller;

import com.liandao.onlineteaching.entity.User;
import com.liandao.onlineteaching.param.Login;
import com.liandao.onlineteaching.param.Password;
import com.liandao.onlineteaching.param.QueryUser;
import com.liandao.onlineteaching.param.UpdateUser;
import com.liandao.onlineteaching.service.UserService;
import com.liandao.onlineteaching.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody Login loginParams) {
        User user = userService.login(loginParams);
        return ResponseUtil.success(user);
    }

    @GetMapping("/user")
    public Map<String, Object> getUser(@RequestParam("uid") int id) {
        User user = userService.getUser(id, true);
        return ResponseUtil.success(user);
    }

    @PostMapping("/admin/user")
    public Map<String, Object> addUser(@RequestParam(value = "avatar", required = false) MultipartFile avatar,
                       @RequestParam("account") String account,
                       @RequestParam("username") String username,
                       @RequestParam("role") String role) {
        userService.addUser(avatar, account, username, role);
        return ResponseUtil.success(null);
    }

    @PostMapping("/admin/user/batch")
    public Map<String, Object> batchAddUser(@RequestParam(value = "file", required = false) MultipartFile file) {
        userService.batchAddUser(file);
        return ResponseUtil.success(null);
    }

    @GetMapping("/admin/user/list")
    public Map<String, Object> getUserList(@ModelAttribute QueryUser queryUserParams) {
        Map<String, Object> userListData = userService.getUserList(queryUserParams);
        return ResponseUtil.success(userListData);
    }

    @GetMapping("/admin/user/{id}")
    public Map<String, Object> getUserById(@PathVariable("id") int id) {
        User user = userService.getUser(id, false);
        return ResponseUtil.success(user);
    }

    @DeleteMapping("/admin/user/{id}")
    public Map<String, Object> deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return ResponseUtil.success(null);
    }

    @PatchMapping("/admin/user/{id}")
    public Map<String, Object> updateUserById(@PathVariable("id") int id, @RequestBody UpdateUser updateUserParams) {
        userService.updateUserById(id, updateUserParams);
        return ResponseUtil.success(null);
    }

    @PatchMapping("/admin/user")
    public Map<String, Object> updateUser(@RequestParam(value = "avatar", required = false) MultipartFile avatar,
                          @RequestParam("id") int id,
                          @RequestParam("account") String account,
                          @RequestParam("username") String username,
                          @RequestParam("password") String password) {
        userService.updateUser(avatar, id, account, username, password);
        return ResponseUtil.success(null);
    }

    @PatchMapping("/user/password")
    public Map<String, Object> updatePassword(@RequestParam("uid") int id, @RequestBody Password passwordParams) {
        userService.updatePassword(id, passwordParams.getOldPassword(), passwordParams.getNewPassword());
        return ResponseUtil.success(null);
    }
}
