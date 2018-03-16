package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ExamInfo;

import java.util.List;

public interface ExamInfoDao {
    int deleteByPrimaryKey(Integer examId);

    int insert(ExamInfo record);

    int insertSelective(ExamInfo record);

    ExamInfo selectByPrimaryKey(Integer examId);

    int updateByPrimaryKeySelective(ExamInfo record);

    int updateByPrimaryKeyWithBLOBs(ExamInfo record);

    int updateByPrimaryKey(ExamInfo record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<ExamInfo> selectByCondition(ExamInfo record, BaseQuery query);

    int selectByConditionGetCount(ExamInfo record, BaseQuery query);
}