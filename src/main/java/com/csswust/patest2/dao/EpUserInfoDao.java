package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.EpUserInfo;

import java.util.List;

public interface EpUserInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EpUserInfo record);

    int insertSelective(EpUserInfo record);

    EpUserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EpUserInfo record);

    int updateByPrimaryKeyWithBLOBs(EpUserInfo record);

    int updateByPrimaryKey(EpUserInfo record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<EpUserInfo> selectByCondition(EpUserInfo record, BaseQuery query);

    int selectByConditionGetCount(EpUserInfo record, BaseQuery query);

    List<EpUserInfo> selectByIds(String ids);

    List<EpUserInfo> selectByIdsList(List<Integer> idsList);

    int insertBatch(List<EpUserInfo> recordList);

    EpUserInfo selectByUsername(String username);

    EpUserInfo selectByEmail(String email);
}
