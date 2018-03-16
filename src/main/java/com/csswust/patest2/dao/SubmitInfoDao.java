package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.SubmitInfo;

import java.util.List;

public interface SubmitInfoDao {
    int deleteByPrimaryKey(Integer submId);

    int insert(SubmitInfo record);

    int insertSelective(SubmitInfo record);

    SubmitInfo selectByPrimaryKey(Integer submId);

    int updateByPrimaryKeySelective(SubmitInfo record);

    int updateByPrimaryKeyWithBLOBs(SubmitInfo record);

    int updateByPrimaryKey(SubmitInfo record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<SubmitInfo> selectByCondition(SubmitInfo record, BaseQuery query);

    int selectByConditionGetCount(SubmitInfo record, BaseQuery query);
}