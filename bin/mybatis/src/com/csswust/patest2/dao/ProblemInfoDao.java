package com.csswust.patest2.dao;

import com.csswust.patest2.entity.ProblemInfo;

public interface ProblemInfoDao {
    int deleteByPrimaryKey(Integer probId);

    int insert(ProblemInfo record);

    int insertSelective(ProblemInfo record);

    ProblemInfo selectByPrimaryKey(Integer probId);

    int updateByPrimaryKeySelective(ProblemInfo record);

    int updateByPrimaryKeyWithBLOBs(ProblemInfo record);

    int updateByPrimaryKey(ProblemInfo record);
}