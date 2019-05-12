package com.liandao.onlineteaching.dao;

import com.liandao.onlineteaching.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    User findByAccount(String account);
    User findById(int id);
    void update(User user);
    void create(User user);
    void batchCreate(List<User> list);
    List<User> getUserList(User user);
    void delete(int id);
}
