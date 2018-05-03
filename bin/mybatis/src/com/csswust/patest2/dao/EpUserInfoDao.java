package com.csswust.patest2.dao;

import com.csswust.patest2.entity.EpUserInfo;

public interface EpUserInfoDao {
    int deleteByPrimaryKey(Integer userId);

    int insert(EpUserInfo record);

    int insertSelective(EpUserInfo record);

    EpUserInfo selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(EpUserInfo record);

    int updateByPrimaryKey(EpUserInfo record);
}