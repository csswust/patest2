package com.csswust.patest2.dao.impl;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.common.dao.CommonMapper;
import com.csswust.patest2.dao.UserLoginLogDao;
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
    public void insertInit(UserLoginLog record) {
        record.setUseLogId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(UserLoginLog record) {
        record.setModifyTime(new Date());
    }
}
