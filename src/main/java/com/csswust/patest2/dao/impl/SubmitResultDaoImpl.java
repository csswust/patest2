package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.SubmitResult;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.SubmitResultDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class SubmitResultDaoImpl extends CommonMapper implements SubmitResultDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.SubmitResultDao.";

    @Override
    public int deleteByPrimaryKey(Integer subResId) {
        if (subResId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", subResId);
    }

    @Override
    public int insert(SubmitResult record) {
        if (record == null) {
            return 0;
        }
        record.setSubResId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(SubmitResult record) {
        if (record == null) {
            return 0;
        }
        record.setSubResId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public SubmitResult selectByPrimaryKey(Integer subResId) {
        if (subResId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", subResId);
    }

    @Override
    public int updateByPrimaryKeySelective(SubmitResult record) {
        if (record == null || record.getSubResId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(SubmitResult record) {
        if (record == null || record.getSubResId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeyWithBLOBs", record);
    }

    @Override
    public int updateByPrimaryKey(SubmitResult record) {
        if (record == null || record.getSubResId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
