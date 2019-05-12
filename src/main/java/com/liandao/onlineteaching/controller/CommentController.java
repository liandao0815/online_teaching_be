package com.liandao.onlineteaching.controller;

import com.liandao.onlineteaching.entity.Comment;
import com.liandao.onlineteaching.service.CommentService;
import com.liandao.onlineteaching.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public Map<String, Object> createComment(@RequestParam("uid") int userId, @RequestBody Comment comment) {
        comment.setUserId(userId);
        commentService.createComment(comment);

        return ResponseUtil.success(null);
    }

    @GetMapping("/comment/list")
    public Map<String, Object> getCommentList(@RequestParam("noteId") int noteId) {
        List<Comment> commentList = commentService.getCommentList(noteId);
        return ResponseUtil.success(commentList);
    }
}
