package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.SiteInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.SiteInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class SiteInfoDaoImpl extends CommonMapper<SiteInfo, BaseQuery> implements SiteInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.SiteInfoDao.";
    }

    @Override
    public void insertInit(SiteInfo record) {
        record.setSiteId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(SiteInfo record) {
        record.setModifyTime(new Date());
    }
}
