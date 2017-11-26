package com.csswust.patest2.dao.impl;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.common.dao.CommonMapper;
import com.csswust.patest2.dao.ProblemInfoDao;
import com.csswust.patest2.entity.ProblemInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ProblemInfoDaoImpl extends CommonMapper<ProblemInfo, BaseQuery> implements ProblemInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.ProblemInfoDao.";
    }

    @Override
    public void insertInit(ProblemInfo record) {
        record.setProbId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(ProblemInfo record) {
        record.setModifyTime(new Date());
    }
}
