package com.csswust.patest2.dao;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.entity.CourseInfo;

import java.util.List;

public interface CourseInfoDao {
    int deleteByPrimaryKey(Integer couId);

    int insert(CourseInfo record);

    int insertSelective(CourseInfo record);

    CourseInfo selectByPrimaryKey(Integer couId);

    int updateByPrimaryKeySelective(CourseInfo record);

    int updateByPrimaryKey(CourseInfo record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<CourseInfo> selectByCondition(CourseInfo record, BaseQuery query);

    int selectByConditionGetCount(CourseInfo record, BaseQuery query);
}