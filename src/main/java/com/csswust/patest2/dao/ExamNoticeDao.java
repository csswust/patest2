package com.csswust.patest2.dao;

import com.csswust.patest2.entity.ExamNotice;

public interface ExamNoticeDao {
    int deleteByPrimaryKey(Integer exaNotId);

    int insert(ExamNotice record);

    int insertSelective(ExamNotice record);

    ExamNotice selectByPrimaryKey(Integer exaNotId);

    int updateByPrimaryKeySelective(ExamNotice record);

    int updateByPrimaryKeyWithBLOBs(ExamNotice record);

    int updateByPrimaryKey(ExamNotice record);
}