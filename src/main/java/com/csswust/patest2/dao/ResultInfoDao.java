package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ResultInfo;

import java.util.List;

public interface ResultInfoDao {
    int deleteByPrimaryKey(Integer resuId);

    int insert(ResultInfo record);

    int insertSelective(ResultInfo record);

    ResultInfo selectByPrimaryKey(Integer resuId);

    int updateByPrimaryKeySelective(ResultInfo record);

    int updateByPrimaryKey(ResultInfo record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<ResultInfo> selectByCondition(ResultInfo record, BaseQuery query);

    int selectByConditionGetCount(ResultInfo record, BaseQuery query);
}