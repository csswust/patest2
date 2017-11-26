package com.csswust.patest2.dao;

import com.csswust.patest2.entity.ExamParam;

public interface ExamParamDao {
    int deleteByPrimaryKey(Integer exaParId);

    int insert(ExamParam record);

    int insertSelective(ExamParam record);

    ExamParam selectByPrimaryKey(Integer exaParId);

    int updateByPrimaryKeySelective(ExamParam record);

    int updateByPrimaryKey(ExamParam record);
}