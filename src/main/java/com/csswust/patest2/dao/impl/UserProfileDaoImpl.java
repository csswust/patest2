package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.UserProfile;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.UserProfileDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class UserProfileDaoImpl extends CommonMapper implements UserProfileDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.UserProfileDao.";

    @Override
    public int deleteByPrimaryKey(Integer useProId) {
        if (useProId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", useProId);
    }

    @Override
    public int insert(UserProfile record) {
        if (record == null) {
            return 0;
        }
        record.setUseProId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(UserProfile record) {
        if (record == null) {
            return 0;
        }
        record.setUseProId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public UserProfile selectByPrimaryKey(Integer useProId) {
        if (useProId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", useProId);
    }

    @Override
    public int updateByPrimaryKeySelective(UserProfile record) {
        if (record == null || record.getUseProId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(UserProfile record) {
        if (record == null || record.getUseProId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
