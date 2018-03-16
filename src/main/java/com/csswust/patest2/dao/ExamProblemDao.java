package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ExamProblem;

import java.util.List;

public interface ExamProblemDao {
    int deleteByPrimaryKey(Integer exaProId);

    int insert(ExamProblem record);

    int insertSelective(ExamProblem record);

    ExamProblem selectByPrimaryKey(Integer exaProId);

    int updateByPrimaryKeySelective(ExamProblem record);

    int updateByPrimaryKey(ExamProblem record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<ExamProblem> selectByCondition(ExamProblem record, BaseQuery query);

    int selectByConditionGetCount(ExamProblem record, BaseQuery query);
}