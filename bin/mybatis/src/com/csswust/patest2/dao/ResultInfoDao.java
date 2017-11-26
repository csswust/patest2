package com.csswust.patest2.dao;

import com.csswust.patest2.entity.ResultInfo;

public interface ResultInfoDao {
    int deleteByPrimaryKey(Integer resuId);

    int insert(ResultInfo record);

    int insertSelective(ResultInfo record);

    ResultInfo selectByPrimaryKey(Integer resuId);

    int updateByPrimaryKeySelective(ResultInfo record);

    int updateByPrimaryKey(ResultInfo record);
}