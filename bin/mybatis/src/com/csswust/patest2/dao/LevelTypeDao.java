package com.csswust.patest2.dao;

import com.csswust.patest2.entity.LevelType;

public interface LevelTypeDao {
    int deleteByPrimaryKey(Integer levTypId);

    int insert(LevelType record);

    int insertSelective(LevelType record);

    LevelType selectByPrimaryKey(Integer levTypId);

    int updateByPrimaryKeySelective(LevelType record);

    int updateByPrimaryKey(LevelType record);
}