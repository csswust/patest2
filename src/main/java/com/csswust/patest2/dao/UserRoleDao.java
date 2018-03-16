package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.UserRole;

import java.util.List;

public interface UserRoleDao {
    int deleteByPrimaryKey(Integer useRolId);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(Integer useRolId);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<UserRole> selectByCondition(UserRole record, BaseQuery query);

    int selectByConditionGetCount(UserRole record, BaseQuery query);
}