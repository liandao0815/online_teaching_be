package com.liandao.onlineteaching.controller;

import com.liandao.onlineteaching.entity.Problem;
import com.liandao.onlineteaching.service.ProblemService;
import com.liandao.onlineteaching.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ProblemController {
    private ProblemService problemService;

    @Autowired
    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @PostMapping("/problem")
    public Map<String, Object> createProblem(@RequestParam("uid") int userId, @RequestBody Problem problem) {
        problem.setUserId(userId);
        problemService.createProblem(problem);

        return ResponseUtil.success(null);
    }

    @GetMapping("/problem/list")
    public Map<String, Object> getProblemList(@RequestParam("pageNum") int pageNum,
                                              @RequestParam("pageSize") int pageSize) {
        List<Problem> problemList = problemService.getProblemList(pageNum, pageSize);
        return ResponseUtil.success(problemList);
    }

    @GetMapping("/problem/{id}")
    public Map<String, Object> getProblem(@PathVariable("id") int id) {
        Problem problem = problemService.getProblem(id);
        return ResponseUtil.success(problem);
    }

    @GetMapping("/problem/listByUser")
    public Map<String, Object> getProblemListByUser(@RequestParam("uid") int userId) {
        List<Problem> problemList = problemService.getProblemListByUser(userId);
        return ResponseUtil.success(problemList);
    }
}
