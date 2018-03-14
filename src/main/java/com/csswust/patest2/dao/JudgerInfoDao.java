package com.csswust.patest2.dao;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.entity.JudgerInfo;

import java.util.List;

public interface JudgerInfoDao {
    int deleteByPrimaryKey(Integer judId);

    int insert(JudgerInfo record);

    int insertSelective(JudgerInfo record);

    JudgerInfo selectByPrimaryKey(Integer judId);

    int updateByPrimaryKeySelective(JudgerInfo record);

    int updateByPrimaryKey(JudgerInfo record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<JudgerInfo> selectByCondition(JudgerInfo record, BaseQuery query);

    int selectByConditionGetCount(JudgerInfo record, BaseQuery query);
}