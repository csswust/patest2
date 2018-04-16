package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.EpNotice;

import java.util.List;

public interface EpNoticeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EpNotice record);

    int insertSelective(EpNotice record);

    EpNotice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EpNotice record);

    int updateByPrimaryKeyWithBLOBs(EpNotice record);

    int updateByPrimaryKey(EpNotice record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<EpNotice> selectByCondition(EpNotice record, BaseQuery query);

    int selectByConditionGetCount(EpNotice record, BaseQuery query);

    List<EpNotice> selectByIds(String ids);

    List<EpNotice> selectByIdsList(List<Integer> idsList);

    int insertBatch(List<EpNotice> recordList);
}
