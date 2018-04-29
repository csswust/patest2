package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.OperateLogDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.OperateLog;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class OperateLogDaoImpl extends CommonMapper<OperateLog, BaseQuery> implements OperateLogDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.OperateLogDao.";
    }

    @Override
    public void insertInit(OperateLog record, Date date) {
        record.setId(null);
        record.setCreateTime(date);
        //record.setModifyTime(date);
        //record.setModifyUserId(getUserId());
    }

    @Override
    public void updateInit(OperateLog record, Date date) {
        // record.setModifyTime(date);
        // record.setModifyUserId(getUserId());
    }
}
