package com.liandao.onlineteaching.controller;

import com.liandao.onlineteaching.entity.Collect;
import com.liandao.onlineteaching.entity.Note;
import com.liandao.onlineteaching.service.CollectService;
import com.liandao.onlineteaching.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CollectController {
    private CollectService collectService;

    @Autowired
    public CollectController(CollectService collectService) {
        this.collectService = collectService;
    }

    @PostMapping("/collect")
    public Map<String, Object> createCollect(@RequestParam("uid") int userId, @RequestBody Collect collect) {
        System.out.println(userId);
        collect.setUserId(userId);
        collect.setNoteId(collect.getNoteId());
        collectService.createCollect(collect);

        return ResponseUtil.success(null);
    }

    @GetMapping("/collect")
    public Map<String, Object> getCollect(@RequestParam("uid") int userId, @RequestParam("noteId") int noteId) {
        Collect collectParams = new Collect();
        collectParams.setUserId(userId);
        collectParams.setNoteId(noteId);

        Collect collect = collectService.getCollect(collectParams);
        return ResponseUtil.success(collect);
    }

    @DeleteMapping("/collect/{id}")
    public Map<String, Object> deleteCollect(@PathVariable("id") int id) {
        collectService.deleteCollect(id);
        return ResponseUtil.success(null);
    }

    @GetMapping("/collect/listByUser")
    public Map<String, Object> getNoteListByUser(@RequestParam("uid") int userId) {
        List<Note> noteList = collectService.getCollectByUser(userId);
        return ResponseUtil.success(noteList);
    }
}
