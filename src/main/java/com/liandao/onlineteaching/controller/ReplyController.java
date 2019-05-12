package com.liandao.onlineteaching.controller;

import com.liandao.onlineteaching.entity.Reply;
import com.liandao.onlineteaching.service.ReplyService;
import com.liandao.onlineteaching.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ReplyController {
    private ReplyService replyService;

    @Autowired
    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/reply")
    public Map<String, Object> createReply(@RequestParam("uid") int userId, @RequestBody Reply reply) {
        reply.setUserId(userId);
        replyService.createReply(reply);
        return ResponseUtil.success(null);
    }

    @GetMapping("/reply/list")
    public Map<String, Object> getReplyList(@RequestParam("problemId") int problemId) {
        List<Reply> replyList = replyService.getReplyList(problemId);
        return ResponseUtil.success(replyList);
    }

    @PatchMapping("/reply/status/{id}")
    public Map<String, Object> updateStatus(@RequestParam("uid") int userId,
                                            @PathVariable("id") int replyId,
                                            @RequestBody Reply reply) {
        reply.setId(replyId);
        replyService.updateReply(userId, reply);
        return ResponseUtil.success(null);
    }
}
