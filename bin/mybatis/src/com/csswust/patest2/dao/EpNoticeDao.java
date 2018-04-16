package com.csswust.patest2.dao;

import com.csswust.patest2.entity.EpNotice;

public interface EpNoticeDao {
    int deleteByPrimaryKey(Integer epnoId);

    int insert(EpNotice record);

    int insertSelective(EpNotice record);

    EpNotice selectByPrimaryKey(Integer epnoId);

    int updateByPrimaryKeySelective(EpNotice record);

    int updateByPrimaryKeyWithBLOBs(EpNotice record);

    int updateByPrimaryKey(EpNotice record);
}