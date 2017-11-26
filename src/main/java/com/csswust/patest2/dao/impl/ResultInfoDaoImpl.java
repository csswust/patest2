package com.csswust.patest2.dao.impl;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.common.dao.CommonMapper;
import com.csswust.patest2.dao.ResultInfoDao;
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
    public void insertInit(ResultInfo record) {
        record.setResuId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(ResultInfo record) {
        record.setModifyTime(new Date());
    }
}
