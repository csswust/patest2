package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.OperateLog;

import java.util.List;

public interface OperateLogDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OperateLog record);

    int insertSelective(OperateLog record);

    OperateLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OperateLog record);

    int updateByPrimaryKeyWithBLOBs(OperateLog record);

    int updateByPrimaryKey(OperateLog record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<OperateLog> selectByCondition(OperateLog record, BaseQuery query);

    int selectByConditionGetCount(OperateLog record, BaseQuery query);

    List<OperateLog> selectByIds(String ids);

    List<OperateLog> selectByIdsList(List<Integer> idsList);

    int insertBatch(List<OperateLog> recordList);
}
