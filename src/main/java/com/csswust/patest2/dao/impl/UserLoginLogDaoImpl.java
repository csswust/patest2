package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.UserLoginLog;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.UserLoginLogDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class UserLoginLogDaoImpl extends CommonMapper implements UserLoginLogDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.UserLoginLogDao.";

    @Override
    public int deleteByPrimaryKey(Integer useLogId) {
        if (useLogId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", useLogId);
    }

    @Override
    public int insert(UserLoginLog record) {
        if (record == null) {
            return 0;
        }
        record.setUseLogId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(UserLoginLog record) {
        if (record == null) {
            return 0;
        }
        record.setUseLogId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public UserLoginLog selectByPrimaryKey(Integer useLogId) {
        if (useLogId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", useLogId);
    }

    @Override
    public int updateByPrimaryKeySelective(UserLoginLog record) {
        if (record == null || record.getUseLogId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(UserLoginLog record) {
        if (record == null || record.getUseLogId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
