package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.ExamNoticeDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.ExamNotice;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ExamNoticeDaoImpl extends CommonMapper<ExamNotice, BaseQuery> implements ExamNoticeDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.ExamNoticeDao.";
    }

    @Override
    public void insertInit(ExamNotice record, Date date) {
        record.setExaNotId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

    @Override
    public void updateInit(ExamNotice record, Date date) {
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }
}
