package com.csswust.patest2.dao;

import com.csswust.patest2.entity.ExamProblem;

public interface ExamProblemDao {
    int deleteByPrimaryKey(Integer exaProId);

    int insert(ExamProblem record);

    int insertSelective(ExamProblem record);

    ExamProblem selectByPrimaryKey(Integer exaProId);

    int updateByPrimaryKeySelective(ExamProblem record);

    int updateByPrimaryKey(ExamProblem record);
}