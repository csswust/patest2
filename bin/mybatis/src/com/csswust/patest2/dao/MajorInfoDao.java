package com.csswust.patest2.dao;

import com.csswust.patest2.entity.MajorInfo;

public interface MajorInfoDao {
    int deleteByPrimaryKey(Integer majId);

    int insert(MajorInfo record);

    int insertSelective(MajorInfo record);

    MajorInfo selectByPrimaryKey(Integer majId);

    int updateByPrimaryKeySelective(MajorInfo record);

    int updateByPrimaryKey(MajorInfo record);
}