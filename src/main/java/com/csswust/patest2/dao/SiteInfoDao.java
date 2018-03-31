package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.SiteInfo;

import java.util.List;

public interface SiteInfoDao {
    int deleteByPrimaryKey(Integer siteId);

    int insert(SiteInfo record);

    int insertSelective(SiteInfo record);

    SiteInfo selectByPrimaryKey(Integer siteId);

    int updateByPrimaryKeySelective(SiteInfo record);

    int updateByPrimaryKeyWithBLOBs(SiteInfo record);

    int updateByPrimaryKey(SiteInfo record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<SiteInfo> selectByCondition(SiteInfo record, BaseQuery query);

    int selectByConditionGetCount(SiteInfo record, BaseQuery query);

    SiteInfo selectByName(String name);
}