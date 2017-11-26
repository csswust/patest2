package com.csswust.patest2.dao;

import com.csswust.patest2.entity.UserProfile;

public interface UserProfileDao {
    int deleteByPrimaryKey(Integer useProId);

    int insert(UserProfile record);

    int insertSelective(UserProfile record);

    UserProfile selectByPrimaryKey(Integer useProId);

    int updateByPrimaryKeySelective(UserProfile record);

    int updateByPrimaryKey(UserProfile record);
}