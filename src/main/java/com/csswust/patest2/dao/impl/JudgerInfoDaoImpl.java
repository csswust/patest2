package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.JudgerInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.JudgerInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class JudgerInfoDaoImpl extends CommonMapper<JudgerInfo, BaseQuery> implements JudgerInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.JudgerInfoDao.";
    }

    @Override
    public void insertInit(JudgerInfo record, Date date) {
        record.setJudId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

    @Override
    public void updateInit(JudgerInfo record, Date date) {
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }
}
