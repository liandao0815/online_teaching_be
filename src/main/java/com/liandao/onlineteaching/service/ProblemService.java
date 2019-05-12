package com.liandao.onlineteaching.service;

import com.github.pagehelper.PageHelper;
import com.liandao.onlineteaching.dao.ProblemMapper;
import com.liandao.onlineteaching.entity.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProblemService {
    private ProblemMapper problemMapper;

    @Autowired
    public ProblemService(ProblemMapper problemMapper) {
        this.problemMapper = problemMapper;
    }

    @Transactional
    public void createProblem(Problem problem) {
        problemMapper.create(problem);
    }

    public List<Problem> getProblemList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return problemMapper.getProblemList();
    }

    public Problem getProblem(int id) {
        return problemMapper.getProblem(id);
    }

    public List<Problem> getProblemListByUser(int userId) {
        return problemMapper.getProblemListByUser(userId);
    }
}
