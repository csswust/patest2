package com.csswust.patest2.dao;

import com.csswust.patest2.entity.KnowledgeInfo;

public interface KnowledgeInfoDao {
    int deleteByPrimaryKey(Integer knowId);

    int insert(KnowledgeInfo record);

    int insertSelective(KnowledgeInfo record);

    KnowledgeInfo selectByPrimaryKey(Integer knowId);

    int updateByPrimaryKeySelective(KnowledgeInfo record);

    int updateByPrimaryKey(KnowledgeInfo record);
}