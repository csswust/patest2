package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.ResultInfo;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.ResultInfoDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ResultInfoDaoImpl extends CommonMapper implements ResultInfoDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.ResultInfoDao.";

    @Override
    public int deleteByPrimaryKey(Integer resuId) {
        if (resuId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", resuId);
    }

    @Override
    public int insert(ResultInfo record) {
        if (record == null) {
            return 0;
        }
        record.setResuId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(ResultInfo record) {
        if (record == null) {
            return 0;
        }
        record.setResuId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public ResultInfo selectByPrimaryKey(Integer resuId) {
        if (resuId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", resuId);
    }

    @Override
    public int updateByPrimaryKeySelective(ResultInfo record) {
        if (record == null || record.getResuId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(ResultInfo record) {
        if (record == null || record.getResuId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
