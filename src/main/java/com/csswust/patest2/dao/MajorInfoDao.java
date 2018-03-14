package com.csswust.patest2.dao;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.entity.MajorInfo;

import java.util.List;

public interface MajorInfoDao {
    int deleteByPrimaryKey(Integer majId);

    int insert(MajorInfo record);

    int insertSelective(MajorInfo record);

    MajorInfo selectByPrimaryKey(Integer majId);

    int updateByPrimaryKeySelective(MajorInfo record);

    int updateByPrimaryKey(MajorInfo record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<MajorInfo> selectByCondition(MajorInfo record, BaseQuery query);

    int selectByConditionGetCount(MajorInfo record, BaseQuery query);
}