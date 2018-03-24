package com.csswust.patest2.dao;

import com.csswust.patest2.entity.PaperProblem;

public interface PaperProblemDao {
    int deleteByPrimaryKey(Integer papProId);

    int insert(PaperProblem record);

    int insertSelective(PaperProblem record);

    PaperProblem selectByPrimaryKey(Integer papProId);

    int updateByPrimaryKeySelective(PaperProblem record);

    int updateByPrimaryKey(PaperProblem record);
}