package com.csswust.patest2.dao;

import com.csswust.patest2.entity.UserLoginLog;

public interface UserLoginLogDao {
    int deleteByPrimaryKey(Integer useLogId);

    int insert(UserLoginLog record);

    int insertSelective(UserLoginLog record);

    UserLoginLog selectByPrimaryKey(Integer useLogId);

    int updateByPrimaryKeySelective(UserLoginLog record);

    int updateByPrimaryKey(UserLoginLog record);
}