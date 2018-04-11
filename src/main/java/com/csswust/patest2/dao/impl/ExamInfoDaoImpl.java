package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.ExamInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.ExamInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ExamInfoDaoImpl extends CommonMapper<ExamInfo, BaseQuery> implements ExamInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.ExamInfoDao.";
    }

    @Override
    public void insertInit(ExamInfo record, Date date) {
        record.setExamId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

    @Override
    public void updateInit(ExamInfo record, Date date) {
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }
}
