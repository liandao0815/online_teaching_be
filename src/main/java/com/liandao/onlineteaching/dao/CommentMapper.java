package com.liandao.onlineteaching.dao;

import com.liandao.onlineteaching.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    void create(Comment comment);
    List<Comment> getCommentList(int noteId);
}
