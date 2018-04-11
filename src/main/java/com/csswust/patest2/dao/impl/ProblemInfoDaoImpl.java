package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.ProblemInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
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
    public void insertInit(ProblemInfo record, Date date) {
        record.setProbId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

    @Override
    public void updateInit(ProblemInfo record, Date date) {
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }
}
