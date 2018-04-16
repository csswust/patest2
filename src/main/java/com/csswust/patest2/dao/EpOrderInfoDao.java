package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.EpOrderInfo;

import java.util.List;

public interface EpOrderInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EpOrderInfo record);

    int insertSelective(EpOrderInfo record);

    EpOrderInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EpOrderInfo record);

    int updateByPrimaryKeyWithBLOBs(EpOrderInfo record);

    int updateByPrimaryKey(EpOrderInfo record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<EpOrderInfo> selectByCondition(EpOrderInfo record, BaseQuery query);

    int selectByConditionGetCount(EpOrderInfo record, BaseQuery query);

    List<EpOrderInfo> selectByIds(String ids);

    List<EpOrderInfo> selectByIdsList(List<Integer> idsList);

    int insertBatch(List<EpOrderInfo> recordList);
}
