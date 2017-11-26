package com.csswust.patest2.dao;

import com.csswust.patest2.entity.SubmitResult;

public interface SubmitResultDao {
    int deleteByPrimaryKey(Integer subResId);

    int insert(SubmitResult record);

    int insertSelective(SubmitResult record);

    SubmitResult selectByPrimaryKey(Integer subResId);

    int updateByPrimaryKeySelective(SubmitResult record);

    int updateByPrimaryKeyWithBLOBs(SubmitResult record);

    int updateByPrimaryKey(SubmitResult record);
}