package com.csswust.patest2.dao;

import com.csswust.patest2.entity.SubmitSimilarity;

public interface SubmitSimilarityDao {
    int deleteByPrimaryKey(Integer subSimId);

    int insert(SubmitSimilarity record);

    int insertSelective(SubmitSimilarity record);

    SubmitSimilarity selectByPrimaryKey(Integer subSimId);

    int updateByPrimaryKeySelective(SubmitSimilarity record);

    int updateByPrimaryKey(SubmitSimilarity record);
}