package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.ProblemSubmitDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.ProblemSubmit;
import org.springframework.stereotype.Repository;

@Repository
public class ProblemSubmitDaoImpl extends CommonMapper<ProblemSubmit, BaseQuery> implements ProblemSubmitDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.ProblemSubmitDao.";
    }

    @Override
    public void insertInit(ProblemSubmit record) {
        record.setId(null);
        // record.setCreateTime(new Date());
        // record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(ProblemSubmit record) {
        // record.setModifyTime(new Date());
    }
}
