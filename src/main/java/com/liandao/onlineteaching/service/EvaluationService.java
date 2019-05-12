package com.liandao.onlineteaching.service;

import com.liandao.onlineteaching.config.CustomException;
import com.liandao.onlineteaching.dao.EvaluationMapper;
import com.liandao.onlineteaching.entity.Evaluation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvaluationService {
    private EvaluationMapper evaluationMapper;

    @Autowired
    public EvaluationService(EvaluationMapper evaluationMapper) {
        this.evaluationMapper = evaluationMapper;
    }

    @Transactional
    public void createEvaluation(Evaluation evaluation) {
        Evaluation queryEvaluation =
                evaluationMapper.findByUserIdAnCourseId(evaluation.getUserId(), evaluation.getCourseId());

        System.out.println(queryEvaluation);
        if(queryEvaluation != null)
            throw new CustomException("你已经评价过该课程，不可再次评价");

        evaluationMapper.create(evaluation);
    }

    public Map<String, Object> getEvaluationList(int courseId) {
        List<Evaluation> evaluationList = evaluationMapper.getEvaluationList(courseId);

        Map<String, Object> map = new HashMap<>();
        map.put("average", evaluationMapper.getAvgScore(courseId));
        map.put("list", evaluationList);

        return map;
    }
}
