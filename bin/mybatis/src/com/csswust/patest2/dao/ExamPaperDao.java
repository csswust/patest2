package com.csswust.patest2.dao;

import com.csswust.patest2.entity.ExamPaper;

public interface ExamPaperDao {
    int deleteByPrimaryKey(Integer exaPapId);

    int insert(ExamPaper record);

    int insertSelective(ExamPaper record);

    ExamPaper selectByPrimaryKey(Integer exaPapId);

    int updateByPrimaryKeySelective(ExamPaper record);

    int updateByPrimaryKey(ExamPaper record);
}