package com.csswust.patest2.dao;

import com.csswust.patest2.entity.EpApplyInfo;

public interface EpApplyInfoDao {
    int deleteByPrimaryKey(Integer applyId);

    int insert(EpApplyInfo record);

    int insertSelective(EpApplyInfo record);

    EpApplyInfo selectByPrimaryKey(Integer applyId);

    int updateByPrimaryKeySelective(EpApplyInfo record);

    int updateByPrimaryKey(EpApplyInfo record);
}