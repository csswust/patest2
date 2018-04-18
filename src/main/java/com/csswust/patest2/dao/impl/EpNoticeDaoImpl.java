package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.EpNoticeDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.EpNotice;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class EpNoticeDaoImpl extends CommonMapper<EpNotice, BaseQuery> implements EpNoticeDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.EpNoticeDao.";
    }

    @Override
    public void insertInit(EpNotice record, Date date) {
        record.setEpnoId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

    @Override
    public void updateInit(EpNotice record, Date date) {
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }
}
