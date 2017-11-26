package com.csswust.patest2.dao.impl;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.common.dao.CommonMapper;
import com.csswust.patest2.dao.ExamParamDao;
import com.csswust.patest2.entity.ExamParam;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ExamParamDaoImpl extends CommonMapper<ExamParam, BaseQuery> implements ExamParamDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.ExamParamDao.";
    }

    @Override
    public void insertInit(ExamParam record) {
        record.setExaParId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(ExamParam record) {
        record.setModifyTime(new Date());
    }
}
