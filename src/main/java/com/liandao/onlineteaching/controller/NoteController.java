package com.liandao.onlineteaching.controller;

import com.liandao.onlineteaching.entity.Note;
import com.liandao.onlineteaching.param.QueryNote;
import com.liandao.onlineteaching.service.NoteService;
import com.liandao.onlineteaching.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class NoteController {
    private NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/note")
    public Map<String, Object> createNote(@RequestParam("uid") int id, @RequestBody Note note) {
        note.setUserId(id);
        noteService.createNote(note);

        return ResponseUtil.success(null);
    }

    @GetMapping("/note/listWithCollect")
    public Map<String, Object> getNoteListWithCollect(@RequestParam("uid") int userId,
                                                      @RequestParam("pageNum") int pageNum,
                                                      @RequestParam("pageSize") int pageSize) {
        List<Note> noteList = noteService.getNoteListWithCollect(userId, pageNum, pageSize);
        return ResponseUtil.success(noteList);
    }

    @GetMapping("/note/{id}")
    public Map<String, Object> getNote(@RequestParam("uid") int userId, @PathVariable("id") int noteId) {
        Note note = noteService.getNote(userId, noteId);
        return ResponseUtil.success(note);
    }

    @GetMapping("/note/listByUser")
    public Map<String, Object> getNoteListByUser(@RequestParam("uid") int userId) {
        List<Note> noteList = noteService.getNoteListByUser(userId);
        return ResponseUtil.success(noteList);
    }

    @DeleteMapping("/admin/note/{id}")
    public Map<String, Object> deleteCourse(@PathVariable("id") int id) {
        noteService.deleteNote(id);
        return ResponseUtil.success(null);
    }

    @GetMapping("/admin/note/list")
    public Map<String, Object> getNoteList(@ModelAttribute QueryNote queryNoteParams) {
        Map<String, Object> noteList = noteService.getNoteList(queryNoteParams);
        return ResponseUtil.success(noteList);
    }
}
