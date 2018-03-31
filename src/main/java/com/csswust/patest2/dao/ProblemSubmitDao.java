package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ProblemSubmit;

import java.util.List;

public interface ProblemSubmitDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ProblemSubmit record);

    int insertSelective(ProblemSubmit record);

    ProblemSubmit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProblemSubmit record);

    int updateByPrimaryKeyWithBLOBs(ProblemSubmit record);

    int updateByPrimaryKey(ProblemSubmit record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<ProblemSubmit> selectByCondition(ProblemSubmit record, BaseQuery query);

    int selectByConditionGetCount(ProblemSubmit record, BaseQuery query);

    List<ProblemSubmit> selectByIds(String ids);

    List<ProblemSubmit> selectByIdsList(List<Integer> idsList);


}