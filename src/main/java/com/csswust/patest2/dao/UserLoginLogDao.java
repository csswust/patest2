package com.csswust.patest2.dao;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.entity.UserLoginLog;

import java.util.List;

public interface UserLoginLogDao {
    int deleteByPrimaryKey(Integer useLogId);

    int insert(UserLoginLog record);

    int insertSelective(UserLoginLog record);

    UserLoginLog selectByPrimaryKey(Integer useLogId);

    int updateByPrimaryKeySelective(UserLoginLog record);

    int updateByPrimaryKey(UserLoginLog record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<UserLoginLog> selectByCondition(UserLoginLog record, BaseQuery query);

    int selectByConditionGetCount(UserLoginLog record, BaseQuery query);
}