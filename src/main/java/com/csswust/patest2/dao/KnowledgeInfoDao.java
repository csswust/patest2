package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.KnowledgeInfo;

import java.util.List;

public interface KnowledgeInfoDao {
    int deleteByPrimaryKey(Integer knowId);

    int insert(KnowledgeInfo record);

    int insertSelective(KnowledgeInfo record);

    KnowledgeInfo selectByPrimaryKey(Integer knowId);

    int updateByPrimaryKeySelective(KnowledgeInfo record);

    int updateByPrimaryKey(KnowledgeInfo record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<KnowledgeInfo> selectByCondition(KnowledgeInfo record, BaseQuery query);

    int selectByConditionGetCount(KnowledgeInfo record, BaseQuery query);
}