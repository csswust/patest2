package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.AcademyInfo;

import java.util.List;

public interface AcademyInfoDao {
    int deleteByPrimaryKey(Integer acaId);

    int insert(AcademyInfo record);

    int insertSelective(AcademyInfo record);

    AcademyInfo selectByPrimaryKey(Integer acaId);

    int updateByPrimaryKeySelective(AcademyInfo record);

    int updateByPrimaryKey(AcademyInfo record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<AcademyInfo> selectByCondition(AcademyInfo record, BaseQuery query);

    int selectByConditionGetCount(AcademyInfo record, BaseQuery query);

    AcademyInfo selectByAcademyName(String academyName);

    int insertBatch(List<AcademyInfo> recordList);
}