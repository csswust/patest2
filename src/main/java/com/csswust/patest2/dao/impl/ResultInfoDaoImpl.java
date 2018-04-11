package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.ResultInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.ResultInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ResultInfoDaoImpl extends CommonMapper<ResultInfo, BaseQuery> implements ResultInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.ResultInfoDao.";
    }

    @Override
    public void insertInit(ResultInfo record, Date date) {
        record.setResuId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

    @Override
    public void updateInit(ResultInfo record, Date date) {
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }
}
