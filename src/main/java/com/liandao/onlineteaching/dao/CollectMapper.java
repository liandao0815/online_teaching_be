package com.liandao.onlineteaching.dao;

import com.liandao.onlineteaching.entity.Collect;
import com.liandao.onlineteaching.entity.Note;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CollectMapper {
    Collect findByUserIdAndNoteId(Collect collect);
    void create(Collect collect);
    void delete(int id);
    List<Note> getCollectListByUser(int userId);
}
