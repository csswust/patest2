package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.dao.ExamNoticeDao;
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
    public void insertInit(ExamNotice record) {
        record.setExaNotId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(ExamNotice record) {
        record.setModifyTime(new Date());
    }
}
