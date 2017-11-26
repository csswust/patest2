package com.csswust.patest2.dao.impl;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.common.dao.CommonMapper;
import com.csswust.patest2.dao.SubmitSimilarityDao;
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
    public void insertInit(SubmitSimilarity record) {
        record.setSubSimId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(SubmitSimilarity record) {
        record.setModifyTime(new Date());
    }
}
