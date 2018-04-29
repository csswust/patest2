package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.UserLoginLogDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.UserLoginLog;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class UserLoginLogDaoImpl extends CommonMapper<UserLoginLog, BaseQuery> implements UserLoginLogDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.UserLoginLogDao.";
    }

    @Override
    public void insertInit(UserLoginLog record, Date date) {
        record.setUseLogId(null);
        record.setCreateTime(date);
        // record.setModifyTime(date);
        // record.setModifyUserId(getUserId());
    }

    @Override
    public void updateInit(UserLoginLog record, Date date) {
        // record.setModifyTime(date);
        // record.setModifyUserId(getUserId());
    }
}
