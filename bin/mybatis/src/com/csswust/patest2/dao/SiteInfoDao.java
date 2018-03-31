package com.csswust.patest2.dao;

import com.csswust.patest2.entity.SiteInfo;

public interface SiteInfoDao {
    int deleteByPrimaryKey(Integer siteId);

    int insert(SiteInfo record);

    int insertSelective(SiteInfo record);

    SiteInfo selectByPrimaryKey(Integer siteId);

    int updateByPrimaryKeySelective(SiteInfo record);

    int updateByPrimaryKeyWithBLOBs(SiteInfo record);

    int updateByPrimaryKey(SiteInfo record);
}