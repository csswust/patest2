package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ExamNotice;

import java.util.List;

public interface ExamNoticeDao {
    int deleteByPrimaryKey(Integer exaNotId);

    int insert(ExamNotice record);

    int insertSelective(ExamNotice record);

    ExamNotice selectByPrimaryKey(Integer exaNotId);

    int updateByPrimaryKeySelective(ExamNotice record);

    int updateByPrimaryKeyWithBLOBs(ExamNotice record);

    int updateByPrimaryKey(ExamNotice record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<ExamNotice> selectByCondition(ExamNotice record, BaseQuery query);

    int selectByConditionGetCount(ExamNotice record, BaseQuery query);
}