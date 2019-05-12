package com.liandao.onlineteaching.service;

import com.liandao.onlineteaching.config.CustomException;
import com.liandao.onlineteaching.dao.MessageMapper;
import com.liandao.onlineteaching.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {
    private MessageMapper messageMapper;

    @Autowired
    public MessageService(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Transactional
    public void createMessage(Message message) {
        messageMapper.create(message);
    }

    public List<Message> getMessageList(int userId) {
        return messageMapper.getMessageList(userId);
    }

    @Transactional
    public Message getMessage(int userId, int id) {
        Message message = messageMapper.findByIdAndUserId(userId, id);

        if(message == null) throw new CustomException("没有找到相关信息");

        message.setStatus("1");
        messageMapper.update(message);

        return message;
    }
}
