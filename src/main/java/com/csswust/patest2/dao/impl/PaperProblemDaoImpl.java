package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.dao.PaperProblemDao;
import com.csswust.patest2.entity.PaperProblem;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class PaperProblemDaoImpl extends CommonMapper<PaperProblem, BaseQuery> implements PaperProblemDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.PaperProblemDao.";
    }

    @Override
    public void insertInit(PaperProblem record) {
        record.setPapProId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(PaperProblem record) {
        record.setModifyTime(new Date());
    }
}
