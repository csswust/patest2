package com.csswust.patest2.dao.impl;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.common.dao.CommonMapper;
import com.csswust.patest2.dao.SubmitResultDao;
import com.csswust.patest2.entity.SubmitResult;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class SubmitResultDaoImpl extends CommonMapper<SubmitResult, BaseQuery> implements SubmitResultDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.SubmitResultDao.";
    }

    @Override
    public void insertInit(SubmitResult record) {
        record.setSubResId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(SubmitResult record) {
        record.setModifyTime(new Date());
    }
}
