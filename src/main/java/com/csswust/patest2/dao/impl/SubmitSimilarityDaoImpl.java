package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.SubmitSimilarityDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.SubmitSimilarity;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class SubmitSimilarityDaoImpl extends CommonMapper<SubmitSimilarity, BaseQuery> implements SubmitSimilarityDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.SubmitSimilarityDao.";
    }

    @Override
    public void insertInit(SubmitSimilarity record, Date date) {
        record.setSubSimId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
    }

    @Override
    public void updatInit(SubmitSimilarity record, Date date) {
        record.setModifyTime(date);
    }
}
