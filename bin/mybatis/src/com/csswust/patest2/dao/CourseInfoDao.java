package com.csswust.patest2.dao;

import com.csswust.patest2.entity.CourseInfo;

public interface CourseInfoDao {
    int deleteByPrimaryKey(Integer couId);

    int insert(CourseInfo record);

    int insertSelective(CourseInfo record);

    CourseInfo selectByPrimaryKey(Integer couId);

    int updateByPrimaryKeySelective(CourseInfo record);

    int updateByPrimaryKey(CourseInfo record);
}