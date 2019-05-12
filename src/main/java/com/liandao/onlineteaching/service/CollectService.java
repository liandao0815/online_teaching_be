package com.liandao.onlineteaching.service;

import com.liandao.onlineteaching.config.CustomException;
import com.liandao.onlineteaching.dao.CollectMapper;
import com.liandao.onlineteaching.entity.Collect;
import com.liandao.onlineteaching.entity.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CollectService {
    private CollectMapper collectMapper;

    @Autowired
    public CollectService(CollectMapper collectMapper) {
        this.collectMapper = collectMapper;
    }

    @Transactional
    public void createCollect(Collect collect) {
        if (this.getCollect(collect) != null)
            throw new CustomException("你已经收藏过该笔记");
        collectMapper.create(collect);
    }

    public Collect getCollect(Collect collect) {
        return collectMapper.findByUserIdAndNoteId(collect);
    }

    public void deleteCollect(int id) {
        collectMapper.delete(id);
    }

    public List<Note> getCollectByUser(int userId) {
        return collectMapper.getCollectListByUser(userId);
    }
}

