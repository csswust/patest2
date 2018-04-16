package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.EpApplyInfo;

import java.util.List;

public interface EpApplyInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EpApplyInfo record);

    int insertSelective(EpApplyInfo record);

    EpApplyInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EpApplyInfo record);

    int updateByPrimaryKeyWithBLOBs(EpApplyInfo record);

    int updateByPrimaryKey(EpApplyInfo record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<EpApplyInfo> selectByCondition(EpApplyInfo record, BaseQuery query);

    int selectByConditionGetCount(EpApplyInfo record, BaseQuery query);

    List<EpApplyInfo> selectByIds(String ids);

    List<EpApplyInfo> selectByIdsList(List<Integer> idsList);

    int insertBatch(List<EpApplyInfo> recordList);
}
