package com.csswust.patest2.dao;

import com.csswust.patest2.entity.SubmitInfo;

public interface SubmitInfoDao {
    int deleteByPrimaryKey(Integer submId);

    int insert(SubmitInfo record);

    int insertSelective(SubmitInfo record);

    SubmitInfo selectByPrimaryKey(Integer submId);

    int updateByPrimaryKeySelective(SubmitInfo record);

    int updateByPrimaryKeyWithBLOBs(SubmitInfo record);

    int updateByPrimaryKey(SubmitInfo record);
}