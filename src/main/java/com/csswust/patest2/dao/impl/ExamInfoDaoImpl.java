package com.csswust.patest2.dao.impl;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.common.dao.CommonMapper;
import com.csswust.patest2.dao.ExamInfoDao;
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
    public void insertInit(ExamInfo record) {
        record.setExamId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(ExamInfo record) {
        record.setModifyTime(new Date());
    }
}
