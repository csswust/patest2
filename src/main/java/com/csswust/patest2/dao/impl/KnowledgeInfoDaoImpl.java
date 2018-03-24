package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.KnowledgeInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.KnowledgeInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class KnowledgeInfoDaoImpl extends CommonMapper<KnowledgeInfo, BaseQuery> implements KnowledgeInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.KnowledgeInfoDao.";
    }

    @Override
    public void insertInit(KnowledgeInfo record) {
        record.setKnowId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(KnowledgeInfo record) {
        record.setModifyTime(new Date());
    }
}
