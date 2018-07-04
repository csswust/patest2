package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.UserInfo;

import java.util.List;

public interface UserInfoDao extends BaseDao<UserInfo, BaseQuery> {
    int deleteByPrimaryKey(Integer userId);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<UserInfo> selectByCondition(UserInfo record, BaseQuery query);

    int selectByConditionGetCount(UserInfo record, BaseQuery query);

    UserInfo selectByUsername(String username);

    int deleteByExamId(Integer examId);
}