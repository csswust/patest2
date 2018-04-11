package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.SubmitInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.SubmitInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class SubmitInfoDaoImpl extends CommonMapper<SubmitInfo, BaseQuery> implements SubmitInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.SubmitInfoDao.";
    }

    @Override
    public void insertInit(SubmitInfo record, Date date) {
        record.setSubmId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

    @Override
    public void updateInit(SubmitInfo record, Date date) {
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }
}
