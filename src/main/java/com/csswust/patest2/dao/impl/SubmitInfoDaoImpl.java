package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.SubmitInfo;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.SubmitInfoDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class SubmitInfoDaoImpl extends CommonMapper implements SubmitInfoDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.SubmitInfoDao.";

    @Override
    public int deleteByPrimaryKey(Integer submId) {
        if (submId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", submId);
    }

    @Override
    public int insert(SubmitInfo record) {
        if (record == null) {
            return 0;
        }
        record.setSubmId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(SubmitInfo record) {
        if (record == null) {
            return 0;
        }
        record.setSubmId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public SubmitInfo selectByPrimaryKey(Integer submId) {
        if (submId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", submId);
    }

    @Override
    public int updateByPrimaryKeySelective(SubmitInfo record) {
        if (record == null || record.getSubmId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(SubmitInfo record) {
        if (record == null || record.getSubmId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeyWithBLOBs", record);
    }

    @Override
    public int updateByPrimaryKey(SubmitInfo record) {
        if (record == null || record.getSubmId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
