package com.csswust.patest2.dao;

import com.csswust.patest2.entity.EpOrderInfo;

public interface EpOrderInfoDao {
    int deleteByPrimaryKey(Integer orderId);

    int insert(EpOrderInfo record);

    int insertSelective(EpOrderInfo record);

    EpOrderInfo selectByPrimaryKey(Integer orderId);

    int updateByPrimaryKeySelective(EpOrderInfo record);

    int updateByPrimaryKey(EpOrderInfo record);
}