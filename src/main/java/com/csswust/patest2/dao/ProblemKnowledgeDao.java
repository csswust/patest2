package com.csswust.patest2.dao;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.entity.ProblemKnowledge;

import java.util.List;

public interface ProblemKnowledgeDao {
    int deleteByPrimaryKey(Integer proKnoId);

    int insert(ProblemKnowledge record);

    int insertSelective(ProblemKnowledge record);

    ProblemKnowledge selectByPrimaryKey(Integer proKnoId);

    int updateByPrimaryKeySelective(ProblemKnowledge record);

    int updateByPrimaryKey(ProblemKnowledge record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<ProblemKnowledge> selectByCondition(ProblemKnowledge record, BaseQuery query);

    int selectByConditionGetCount(ProblemKnowledge record, BaseQuery query);
}