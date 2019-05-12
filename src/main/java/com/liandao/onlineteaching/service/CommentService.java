package com.liandao.onlineteaching.service;

import com.liandao.onlineteaching.dao.CommentMapper;
import com.liandao.onlineteaching.dao.NoteMapper;
import com.liandao.onlineteaching.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private CommentMapper commentMapper;

    @Autowired
    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Transactional
    public void createComment(Comment comment) {
        commentMapper.create(comment);
    }

    public List<Comment> getCommentList(int noteId) {
        return commentMapper.getCommentList(noteId);
    }
}
