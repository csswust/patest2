package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.dao.EpUserInfoDao;
import com.csswust.patest2.entity.EpUserInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class EpUserInfoDaoImpl extends CommonMapper<EpUserInfo, BaseQuery> implements EpUserInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.EpUserInfoDao.";
    }

    @Override
    public void insertInit(EpUserInfo record, Date date) {
        record.setUserId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

    @Override
    public void updateInit(EpUserInfo record, Date date) {
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }
}
