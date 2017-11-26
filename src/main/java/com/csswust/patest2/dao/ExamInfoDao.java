package com.csswust.patest2.dao;

import com.csswust.patest2.entity.ExamInfo;

public interface ExamInfoDao {
    int deleteByPrimaryKey(Integer examId);

    int insert(ExamInfo record);

    int insertSelective(ExamInfo record);

    ExamInfo selectByPrimaryKey(Integer examId);

    int updateByPrimaryKeySelective(ExamInfo record);

    int updateByPrimaryKeyWithBLOBs(ExamInfo record);

    int updateByPrimaryKey(ExamInfo record);
}