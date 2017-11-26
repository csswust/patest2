package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.UserRole;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.UserRoleDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class UserRoleDaoImpl extends CommonMapper implements UserRoleDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.UserRoleDao.";

    @Override
    public int deleteByPrimaryKey(Integer useRolId) {
        if (useRolId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", useRolId);
    }

    @Override
    public int insert(UserRole record) {
        if (record == null) {
            return 0;
        }
        record.setUseRolId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(UserRole record) {
        if (record == null) {
            return 0;
        }
        record.setUseRolId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public UserRole selectByPrimaryKey(Integer useRolId) {
        if (useRolId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", useRolId);
    }

    @Override
    public int updateByPrimaryKeySelective(UserRole record) {
        if (record == null || record.getUseRolId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(UserRole record) {
        if (record == null || record.getUseRolId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
