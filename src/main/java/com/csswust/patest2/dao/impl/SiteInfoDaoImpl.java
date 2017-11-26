package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.SiteInfo;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.SiteInfoDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class SiteInfoDaoImpl extends CommonMapper implements SiteInfoDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.SiteInfoDao.";

    @Override
    public int deleteByPrimaryKey(Integer siteId) {
        if (siteId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", siteId);
    }

    @Override
    public int insert(SiteInfo record) {
        if (record == null) {
            return 0;
        }
        record.setSiteId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(SiteInfo record) {
        if (record == null) {
            return 0;
        }
        record.setSiteId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public SiteInfo selectByPrimaryKey(Integer siteId) {
        if (siteId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", siteId);
    }

    @Override
    public int updateByPrimaryKeySelective(SiteInfo record) {
        if (record == null || record.getSiteId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(SiteInfo record) {
        if (record == null || record.getSiteId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeyWithBLOBs", record);
    }

    @Override
    public int updateByPrimaryKey(SiteInfo record) {
        if (record == null || record.getSiteId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
