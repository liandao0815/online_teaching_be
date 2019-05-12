package com.liandao.onlineteaching.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liandao.onlineteaching.config.CustomException;
import com.liandao.onlineteaching.dao.MessageMapper;
import com.liandao.onlineteaching.dao.NoteMapper;
import com.liandao.onlineteaching.entity.Message;
import com.liandao.onlineteaching.entity.Note;
import com.liandao.onlineteaching.param.QueryNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoteService {
    private NoteMapper noteMapper;
    private MessageMapper messageMapper;

    @Autowired
    public NoteService(NoteMapper noteMapper, MessageMapper messageMapper) {
        this.noteMapper = noteMapper;
        this.messageMapper = messageMapper;
    }

    @Transactional
    public void createNote(Note note) {
        noteMapper.create(note);
    }

    public List<Note> getNoteListWithCollect(int userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Note> noteList = noteMapper.getNoteListWithCollect(userId);

        List<Note> newNoteList = new ArrayList<>();
        for (Note note : noteList) {
            String content = note.getContent();
            if (content.length() > 50)
                note.setContent(content.substring(0, 50));
            newNoteList.add(note);
        }

        return newNoteList;
    }

    @Transactional
    public Note getNote(int userId, int noteId) {
        Note note = new Note();
        note.setId(noteId);
        note.setUserId(userId);

        Note retNote = noteMapper.getNote(note);
        if(retNote == null) return null;
        if(retNote.getType().equals("0") && retNote.getUserId() != userId)
            throw new CustomException("无法访问他人私有笔记");

        retNote.setReadCount(retNote.getReadCount() + 1);
        noteMapper.updateReadCount(retNote);

        return retNote;
    }

    public List<Note> getNoteListByUser(int userId) {
        return noteMapper.getNoteListByUser(userId);
    }

    public Map<String, Object> getNoteList(QueryNote queryNoteParams) {
        Note note = new Note();
        note.setId(queryNoteParams.getId());
        note.setTitle(queryNoteParams.getTitle());

        PageHelper.startPage(queryNoteParams.getPageNum(), queryNoteParams.getPageSize());
        List<Note> noteList = noteMapper.getNoteList(note);

        PageInfo<Note> page = new PageInfo<>(noteList);

        Map<String, Object> map = new HashMap<>();
        map.put("list", noteList);
        map.put("total", page.getTotal());

        return map;
    }

    @Transactional
    public void deleteNote(int id) {
        Note note = noteMapper.findById(id);
        int userId = note.getUserId();
        String title = note.getTitle();

        Message message = new Message();
        message.setUserId(userId);
        message.setContent("很遗憾，由于您发布的公开笔记【" + title + "】存在不可描述的信息，所以管理员已经将其删除");
        messageMapper.create(message);

        noteMapper.delete(id);
    }
}
