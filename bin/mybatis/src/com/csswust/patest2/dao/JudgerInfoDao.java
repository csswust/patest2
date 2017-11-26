package com.csswust.patest2.dao;

import com.csswust.patest2.entity.JudgerInfo;

public interface JudgerInfoDao {
    int deleteByPrimaryKey(Integer judId);

    int insert(JudgerInfo record);

    int insertSelective(JudgerInfo record);

    JudgerInfo selectByPrimaryKey(Integer judId);

    int updateByPrimaryKeySelective(JudgerInfo record);

    int updateByPrimaryKey(JudgerInfo record);
}