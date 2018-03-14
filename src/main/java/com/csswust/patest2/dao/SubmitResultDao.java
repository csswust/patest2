package com.csswust.patest2.dao;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.entity.SubmitResult;

import java.util.List;

public interface SubmitResultDao {
    int deleteByPrimaryKey(Integer subResId);

    int insert(SubmitResult record);

    int insertSelective(SubmitResult record);

    SubmitResult selectByPrimaryKey(Integer subResId);

    int updateByPrimaryKeySelective(SubmitResult record);

    int updateByPrimaryKeyWithBLOBs(SubmitResult record);

    int updateByPrimaryKey(SubmitResult record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<SubmitResult> selectByCondition(SubmitResult record, BaseQuery query);

    int selectByConditionGetCount(SubmitResult record, BaseQuery query);
}