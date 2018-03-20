package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ProblemInfo;

import java.util.List;

public interface ProblemInfoDao {
    int deleteByPrimaryKey(Integer probId);

    int insert(ProblemInfo record);

    int insertSelective(ProblemInfo record);

    ProblemInfo selectByPrimaryKey(Integer probId);

    int updateByPrimaryKeySelective(ProblemInfo record);

    int updateByPrimaryKeyWithBLOBs(ProblemInfo record);

    int updateByPrimaryKey(ProblemInfo record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<ProblemInfo> selectByCondition(ProblemInfo record, BaseQuery query);

    int selectByConditionGetCount(ProblemInfo record, BaseQuery query);

    List<ProblemInfo> selectByIds(String ids);

    List<ProblemInfo> selectByIdsList(List<Integer> idsList);
}