package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.UserProfile;

import java.util.List;

public interface UserProfileDao extends BaseDao<UserProfile, BaseQuery> {
    int deleteByPrimaryKey(Integer useProId);

    int insert(UserProfile record);

    int insertSelective(UserProfile record);

    UserProfile selectByPrimaryKey(Integer useProId);

    int updateByPrimaryKeySelective(UserProfile record);

    int updateByPrimaryKey(UserProfile record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<UserProfile> selectByCondition(UserProfile record, BaseQuery query);

    int selectByConditionGetCount(UserProfile record, BaseQuery query);

    UserProfile selectByStudentNumber(String studentNumber);
}