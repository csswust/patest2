package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.entity.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by 972536780 on 2017/11/19.
 */
@Repository
public class UserInfoDaoImpl extends CommonMapper implements UserInfoDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.UserInfoDao.";

    @Override
    public int deleteByPrimaryKey(Integer userId) {
        if (userId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", userId);
    }

    @Override
    public int insert(UserInfo record) {
        if (record == null) {
            return 0;
        }
        record.setUserId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(UserInfo record) {
        if (record == null) {
            return 0;
        }
        record.setUserId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public UserInfo selectByPrimaryKey(Integer userId) {
        if (userId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", userId);
    }

    @Override
    public int updateByPrimaryKeySelective(UserInfo record) {
        if (record == null || record.getUserId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(UserInfo record) {
        if (record == null || record.getUserId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
