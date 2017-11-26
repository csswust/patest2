package com.csswust.patest2.dao;

import com.csswust.patest2.entity.ProblemKnowledge;

public interface ProblemKnowledgeDao {
    int deleteByPrimaryKey(Integer proKnoId);

    int insert(ProblemKnowledge record);

    int insertSelective(ProblemKnowledge record);

    ProblemKnowledge selectByPrimaryKey(Integer proKnoId);

    int updateByPrimaryKeySelective(ProblemKnowledge record);

    int updateByPrimaryKey(ProblemKnowledge record);
}