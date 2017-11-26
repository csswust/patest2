package com.csswust.patest2.dao.impl;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.common.dao.CommonMapper;
import com.csswust.patest2.dao.ExamProblemDao;
import com.csswust.patest2.entity.ExamProblem;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ExamProblemDaoImpl extends CommonMapper<ExamProblem, BaseQuery> implements ExamProblemDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.ExamProblemDao.";
    }

    @Override
    public void insertInit(ExamProblem record) {
        record.setExaProId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(ExamProblem record) {
        record.setModifyTime(new Date());
    }
}
