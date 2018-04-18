package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.EpApplyInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.EpApplyInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class EpApplyInfoDaoImpl extends CommonMapper<EpApplyInfo, BaseQuery> implements EpApplyInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.EpApplyInfoDao.";
    }

    @Override
    public void insertInit(EpApplyInfo record, Date date) {
        record.setApplyId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

    @Override
    public void updateInit(EpApplyInfo record, Date date) {
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }
}
