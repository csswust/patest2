package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ExamPaper;

import java.util.List;

public interface ExamPaperDao {
    int deleteByPrimaryKey(Integer exaPapId);

    int insert(ExamPaper record);

    int insertSelective(ExamPaper record);

    ExamPaper selectByPrimaryKey(Integer exaPapId);

    int updateByPrimaryKeySelective(ExamPaper record);

    int updateByPrimaryKey(ExamPaper record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<ExamPaper> selectByCondition(ExamPaper record, BaseQuery query);

    int selectByConditionGetCount(ExamPaper record, BaseQuery query);
}