package com.liandao.onlineteaching.dao;

import com.liandao.onlineteaching.entity.Note;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoteMapper {
    void create(Note note);
    List<Note> getNoteListWithCollect(int userId);
    Note getNote(Note note);
    void updateReadCount(Note note);
    List<Note> getNoteList(Note note);
    List<Note> getNoteListByUser(int userId);
    void delete(int id);
    Note findById(int id);
}
