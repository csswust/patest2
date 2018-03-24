package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.PaperProblem;

import java.util.List;

public interface PaperProblemDao {
    int deleteByPrimaryKey(Integer papProId);

    int insert(PaperProblem record);

    int insertSelective(PaperProblem record);

    PaperProblem selectByPrimaryKey(Integer papProId);

    int updateByPrimaryKeySelective(PaperProblem record);

    int updateByPrimaryKey(PaperProblem record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<PaperProblem> selectByCondition(PaperProblem record, BaseQuery query);

    int selectByConditionGetCount(PaperProblem record, BaseQuery query);

    int deleteByExamId(Integer examId);
}