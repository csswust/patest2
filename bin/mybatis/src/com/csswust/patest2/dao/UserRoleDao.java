package com.csswust.patest2.dao;

import com.csswust.patest2.entity.UserRole;

public interface UserRoleDao {
    int deleteByPrimaryKey(Integer useRolId);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(Integer useRolId);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);
}