package com.csswust.patest2.dao.impl;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.common.dao.CommonMapper;
import com.csswust.patest2.dao.ExamPaperDao;
import com.csswust.patest2.entity.ExamPaper;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ExamPaperDaoImpl extends CommonMapper<ExamPaper, BaseQuery> implements ExamPaperDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.ExamPaperDao.";
    }

    @Override
    public void insertInit(ExamPaper record) {
        record.setExaPapId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(ExamPaper record) {
        record.setModifyTime(new Date());
    }
}
