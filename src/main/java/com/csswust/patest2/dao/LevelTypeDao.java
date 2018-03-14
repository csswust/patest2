package com.csswust.patest2.dao;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.entity.LevelType;

import java.util.List;

public interface LevelTypeDao {
    int deleteByPrimaryKey(Integer levTypId);

    int insert(LevelType record);

    int insertSelective(LevelType record);

    LevelType selectByPrimaryKey(Integer levTypId);

    int updateByPrimaryKeySelective(LevelType record);

    int updateByPrimaryKey(LevelType record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<LevelType> selectByCondition(LevelType record, BaseQuery query);

    int selectByConditionGetCount(LevelType record, BaseQuery query);
}