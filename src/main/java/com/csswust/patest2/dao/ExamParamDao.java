package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ExamParam;

import java.util.List;

public interface ExamParamDao {
    int deleteByPrimaryKey(Integer exaParId);

    int insert(ExamParam record);

    int insertSelective(ExamParam record);

    ExamParam selectByPrimaryKey(Integer exaParId);

    int updateByPrimaryKeySelective(ExamParam record);

    int updateByPrimaryKey(ExamParam record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<ExamParam> selectByCondition(ExamParam record, BaseQuery query);

    int selectByConditionGetCount(ExamParam record, BaseQuery query);
}