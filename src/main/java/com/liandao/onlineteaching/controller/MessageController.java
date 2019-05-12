package com.liandao.onlineteaching.controller;

import com.liandao.onlineteaching.entity.Message;
import com.liandao.onlineteaching.service.MessageService;
import com.liandao.onlineteaching.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MessageController {
    private MessageService messageService;

    @Autowired
    private MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/admin/message")
    public Map<String, Object> createMessage(@RequestBody Message message) {
        messageService.createMessage(message);
        return ResponseUtil.success(null);
    }

    @GetMapping("/message/list")
    public Map<String, Object> getMessageList(@RequestParam("uid") int userId) {
        List<Message> messageList = messageService.getMessageList(userId);
        return ResponseUtil.success(messageList);
    }

    @GetMapping("/message/{id}")
    public Map<String, Object> getMessage(@RequestParam("uid") int userId, @PathVariable("id") int id) {
        Message message = messageService.getMessage(userId, id);
        return ResponseUtil.success(message);
    }
}
